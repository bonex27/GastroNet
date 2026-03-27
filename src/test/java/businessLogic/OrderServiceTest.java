package businessLogic;

import dao.*;
import domainModel.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderServiceTest {

    private OrderService orderService;
    private ProductService productService;
    private CustomerService customerService;
    private CategoryService categoryService;
    private Customer customer;
    private Product product;
    private Category category;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp() throws Exception {
        Database.getConnection().prepareStatement("DELETE FROM main.ProductOrder").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM main.Orders").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM main.Products").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM main.Customers").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM main.Category").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();

        SQLiteCategoryDAO categoryDAO = new SQLiteCategoryDAO();
        SQLiteProductDAO productDAO = new SQLiteProductDAO(categoryDAO);

        SQLiteCustomerDAO customerDAO = new SQLiteCustomerDAO();
        SQLiteOrderDAO orderDAO = new SQLiteOrderDAO(customerDAO,productDAO);


        orderService = new OrderService(orderDAO, productDAO, customerDAO);
        productService = new ProductService(productDAO);
        customerService = new CustomerService(customerDAO);
        categoryService = new CategoryService(categoryDAO);

        category = categoryService.CreateCategory("Primi");
        customerService.addCustomer("Mario", "Rossi", "RSSMRA80A01H501X", "Cash");
        customer = customerService.getPerson("RSSMRA80A01H501X");

        int productId = productService.AddProduct("Lasagna", "Pasta al forno", 9.99, category, 10);
        product = productService.getProduct(productId);
    }

    /*@Test
    void testCreateOrder_Success() throws Exception {
        //int orderId = orderService.createOrder(customer.getCf());

        Order retrieved = orderService.getOrder(orderId);
        assertNotNull(retrieved);
        assertEquals(orderId, retrieved.getId());
        assertEquals(customer.getCf(), retrieved.get().getCf());
    }*/

    @Test
    void testGetOrder_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());

        Order retrieved = orderService.getOrder(orderId);
        assertNotNull(retrieved);
        assertEquals(orderId, retrieved.getId());
    }

    @Test
    void testGetOrders_Success() throws Exception {
        orderService.createOrder(customer.getCf());
        orderService.createOrder(customer.getCf());

        List<Order> orders = orderService.getOrders();
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrdersByCustomer_Success() throws Exception {
        orderService.createOrder(customer.getCf());
        orderService.createOrder(customer.getCf());

        List<Order> orders = orderService.getOrders(customer.getCf());
        assertEquals(2, orders.size());
    }

    @Test
    void testDeleteOrder_CustomerChoosing_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());

        CancelResult result = assertDoesNotThrow(() -> orderService.deleteOrder(orderId));
        assertTrue(result.cancellato());
        assertTrue(result.reso());

        Order retrieved = orderService.getOrder(orderId);
        assertNull(retrieved);
    }

    @Test
    void testDeleteOrder_WithoutIssue() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);
        orderService.startPreparation(orderId);

        var deleteResult = orderService.deleteOrder(orderId);

        assertTrue(deleteResult.cancellato());
        assertFalse(deleteResult.reso());
        //Se provo ad eliminare due volte ottenog un eccezione dato che non esiste più l'oggetto
        assertThrows(RuntimeException.class, () ->orderService.deleteOrder(orderId));
    }

    @Test
    void testDeleteOrder_Pending_ReturnsRefund() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);

        CancelResult deleteResult = orderService.deleteOrder(orderId);

        assertTrue(deleteResult.cancellato());
        assertTrue(deleteResult.reso());
    }

    @Test
    void testDeleteOrder_Ready_WithoutRefund() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);
        orderService.startPreparation(orderId);
        orderService.endPreparation(orderId);

        CancelResult deleteResult = orderService.deleteOrder(orderId);

        assertTrue(deleteResult.cancellato());
        assertFalse(deleteResult.reso());
    }

    @Test
    void testDeleteOrder_Delivered_ThrowsException() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);
        orderService.startPreparation(orderId);
        orderService.endPreparation(orderId);
        orderService.collectOrder(orderId);

        assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(orderId));
    }

    @Test
    void testConfirmOrder_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());

        assertDoesNotThrow(() -> orderService.confirmOrder(orderId));

        Order retrieved = orderService.getOrder(orderId);
        assertEquals("Pending", retrieved.getState());
    }

    @Test
    void testConfirmOrder_InvalidState_ThrowsException() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);

        assertThrows(RuntimeException.class, () -> orderService.confirmOrder(orderId));
    }

    @Test
    void testStartPreparation_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);

        assertDoesNotThrow(() -> orderService.startPreparation(orderId));

        Order retrieved = orderService.getOrder(orderId);
        assertEquals("Preparation", retrieved.getState());
    }

    @Test
    void testStartPreparation_InvalidState_ThrowsException() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());

        assertThrows(RuntimeException.class, () -> orderService.startPreparation(orderId));
    }

    @Test
    void testEndPreparation_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);
        orderService.startPreparation(orderId);

        assertDoesNotThrow(() -> orderService.endPreparation(orderId));

        Order retrieved = orderService.getOrder(orderId);
        assertEquals("Ready", retrieved.getState());
    }

    @Test
    void testCollectOrder_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);
        orderService.startPreparation(orderId);
        orderService.endPreparation(orderId);

        assertDoesNotThrow(() -> orderService.collectOrder(orderId));

        Order retrieved = orderService.getOrder(orderId);
        assertEquals("Delivered", retrieved.getState());
    }

    @Test
    void testOrderStateTransitions_FullFlow() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        Order order = orderService.getOrder(orderId);
        assertEquals("CustomerChoosing", order.getState());

        orderService.confirmOrder(orderId);
        order = orderService.getOrder(orderId);
        assertEquals("Pending", order.getState());

        orderService.startPreparation(orderId);
        order = orderService.getOrder(orderId);
        assertEquals("Preparation", order.getState());

        orderService.endPreparation(orderId);
        order = orderService.getOrder(orderId);
        assertEquals("Ready", order.getState());

        orderService.collectOrder(orderId);
        order = orderService.getOrder(orderId);
        assertEquals("Delivered", order.getState());
    }

    @Test
    void testAddProductToOrder_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());

        assertDoesNotThrow(() -> orderService.addProductToOrder(product.getId(), orderId));

        Order retrieved = orderService.getOrder(orderId);
        var a  = retrieved.getProducts().contains(product);
        assertTrue(retrieved.getProducts().contains(product));
    }

    @Test
    void testRemoveProductFromOrder_Success() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.addProductToOrder(product.getId(), orderId);

        assertDoesNotThrow(() -> orderService.removeProductFromOrder(product.getId(), orderId));

        Order retrieved = orderService.getOrder(orderId);
        assertFalse(retrieved.getProducts().contains(product));
    }

    @Test
    void testAddProductToOrder_InvalidState_ThrowsException() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.confirmOrder(orderId);

        assertThrows(IllegalStateException.class, () -> orderService.addProductToOrder(product.getId(), orderId));
    }

    @Test
    void testRemoveProductFromOrder_InvalidState_ThrowsException() throws Exception {
        int orderId = orderService.createOrder(customer.getCf());
        orderService.addProductToOrder(product.getId(), orderId);
        orderService.confirmOrder(orderId);

        assertThrows(IllegalStateException.class, () -> orderService.removeProductFromOrder(product.getId(), orderId));
    }
}
