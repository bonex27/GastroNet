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

public class OrderControllerTest {

    private OrderController orderController;
    private ProductController productController;
    private CustomerController customerController;
    private CategoryController categoryController;
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


        orderController = new OrderController(orderDAO, productDAO, customerDAO);
        productController = new ProductController(productDAO);
        customerController = new CustomerController(customerDAO);
        categoryController = new CategoryController(categoryDAO);

        category = categoryController.CreateCategory("Primi");
        customerController.addCustomer("Mario", "Rossi", "RSSMRA80A01H501X", "Cash");
        customer = customerController.getPerson("RSSMRA80A01H501X");

        int productId = productController.AddProduct("Lasagna", "Pasta al forno", 9.99, category, 10);
        product = productController.getProduct(productId);
    }

    /*@Test
    void testCreateOrder_Success() throws Exception {
        //int orderId = orderController.createOrder(customer.getCf());

        Order retrieved = orderController.getOrder(orderId);
        assertNotNull(retrieved);
        assertEquals(orderId, retrieved.getId());
        assertEquals(customer.getCf(), retrieved.get().getCf());
    }*/

    @Test
    void testGetOrder_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());

        Order retrieved = orderController.getOrder(orderId);
        assertNotNull(retrieved);
        assertEquals(orderId, retrieved.getId());
    }

    @Test
    void testGetOrders_Success() throws Exception {
        orderController.createOrder(customer.getCf());
        orderController.createOrder(customer.getCf());

        List<Order> orders = orderController.getOrders();
        assertEquals(2, orders.size());
    }

    @Test
    void testGetOrdersByCustomer_Success() throws Exception {
        orderController.createOrder(customer.getCf());
        orderController.createOrder(customer.getCf());

        List<Order> orders = orderController.getOrders(customer.getCf());
        assertEquals(2, orders.size());
    }

    @Test
    void testDeleteOrder_CustomerChoosing_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());

        assertDoesNotThrow(() -> orderController.deleteOrder(orderId));

        Order retrieved = orderController.getOrder(orderId);
        assertNull(retrieved);
    }

    @Test
    void testDeleteOrder_WithoutIssue() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        orderController.confirmOrder(orderId);
        orderController.startPreparation(orderId);

        var deleteResult = orderController.deleteOrder(orderId);

        assertTrue(deleteResult.cancellato());
        assertFalse(deleteResult.reso());
        //Se provo ad eliminare due volte ottenog un eccezione dato che non esiste più l'oggetto
        assertThrows(RuntimeException.class, () ->orderController.deleteOrder(orderId));
    }

    @Test
    void testConfirmOrder_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());

        assertDoesNotThrow(() -> orderController.confirmOrder(orderId));

        Order retrieved = orderController.getOrder(orderId);
        assertEquals("Pending", retrieved.getState());
    }

    @Test
    void testConfirmOrder_InvalidState_ThrowsException() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        orderController.confirmOrder(orderId);

        assertThrows(RuntimeException.class, () -> orderController.confirmOrder(orderId));
    }

    @Test
    void testStartPreparation_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        orderController.confirmOrder(orderId);

        assertDoesNotThrow(() -> orderController.startPreparation(orderId));

        Order retrieved = orderController.getOrder(orderId);
        assertEquals("Preparation", retrieved.getState());
    }

    @Test
    void testStartPreparation_InvalidState_ThrowsException() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());

        assertThrows(RuntimeException.class, () -> orderController.startPreparation(orderId));
    }

    @Test
    void testEndPreparation_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        orderController.confirmOrder(orderId);
        orderController.startPreparation(orderId);

        assertDoesNotThrow(() -> orderController.endPreparation(orderId));

        Order retrieved = orderController.getOrder(orderId);
        assertEquals("Ready", retrieved.getState());
    }

    @Test
    void testCollectOrder_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        orderController.confirmOrder(orderId);
        orderController.startPreparation(orderId);
        orderController.endPreparation(orderId);

        assertDoesNotThrow(() -> orderController.collectOrder(orderId));

        Order retrieved = orderController.getOrder(orderId);
        assertEquals("Delivered", retrieved.getState());
    }

    @Test
    void testOrderStateTransitions_FullFlow() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        Order order = orderController.getOrder(orderId);
        assertEquals("CustomerChoosing", order.getState());

        orderController.confirmOrder(orderId);
        order = orderController.getOrder(orderId);
        assertEquals("Pending", order.getState());

        orderController.startPreparation(orderId);
        order = orderController.getOrder(orderId);
        assertEquals("Preparation", order.getState());

        orderController.endPreparation(orderId);
        order = orderController.getOrder(orderId);
        assertEquals("Ready", order.getState());

        orderController.collectOrder(orderId);
        order = orderController.getOrder(orderId);
        assertEquals("Delivered", order.getState());
    }

    @Test
    void testAddProductToOrder_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());

        assertDoesNotThrow(() -> orderController.addProductToOrder(product.getId(), orderId));

        Order retrieved = orderController.getOrder(orderId);
        var a  = retrieved.getProducts().contains(product);
        assertTrue(retrieved.getProducts().contains(product));
    }

    @Test
    void testRemoveProductFromOrder_Success() throws Exception {
        int orderId = orderController.createOrder(customer.getCf());
        orderController.addProductToOrder(product.getId(), orderId);

        assertDoesNotThrow(() -> orderController.removeProductFromOrder(product.getId(), orderId));

        Order retrieved = orderController.getOrder(orderId);
        assertFalse(retrieved.getProducts().contains(product));
    }
}
