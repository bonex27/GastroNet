package businessLogic;
import dao.CategoryDAO;
import dao.SQLiteCategoryDAO;
import dao.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.SQLiteProductDAO;
import domainModel.Category;
import domainModel.Product;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductControllerTest {

    private ProductController productController;
    private CategoryController categoryController;
    private Category category;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp() throws SQLException {
        Database.getConnection().prepareStatement("DELETE FROM main.Products").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM main.Category").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();

        SQLiteCategoryDAO categoryDAO = new SQLiteCategoryDAO();
        SQLiteProductDAO productDAO = new SQLiteProductDAO(categoryDAO);


        productController = new ProductController(productDAO);
        categoryController = new CategoryController(categoryDAO);

        category = categoryController.CreateCategory("Elettronica");
    }

    @Test
    void testAddProduct_Success() throws Exception {
        int productId = productController.AddProduct("Laptop", "Gaming laptop", 999.99, category, 10);

        Product retrieved = productController.getProduct(productId);
        assertNotNull(retrieved);
        assertEquals("Laptop", retrieved.getName());
        assertEquals("Gaming laptop", retrieved.getDescription());
        assertEquals(999.99, retrieved.getPrice());
        assertEquals(10, retrieved.getStock());
    }

    @Test
    void testGetProduct_Success() throws Exception {
        int productId = productController.AddProduct("Mouse", "Wireless mouse", 29.99, category, 50);

        Product retrieved = productController.getProduct(productId);
        assertNotNull(retrieved);
        assertEquals(productId, retrieved.getId());
    }

    @Test
    void testGetProduct_NotFound_ReturnsNull() throws Exception {
        Product retrieved = productController.getProduct(9999);
        assertNull(retrieved);
    }

    @Test
    void testDeleteProduct_Success() throws Exception {
        int productId = productController.AddProduct("Keyboard", "Mechanical", 89.99, category, 20);

        boolean result = productController.deleteProduct(productId);
        assertTrue(result);

        Product retrieved = productController.getProduct(productId);
        assertNull(retrieved);
    }

    @Test
    void testIncreaseProductQuantity_Success() throws Exception {
        int productId = productController.AddProduct("Monitor", "4K Monitor", 399.99, category, 15);

        productController.IncreaseProductQuantity(productId, 10);

        Product retrieved = productController.getProduct(productId);
        assertEquals(25, retrieved.getStock());
    }

    @Test
    void testDecreaseProductQuantity_Success() throws Exception {
        int productId = productController.AddProduct("Headphones", "Wireless", 79.99, category, 30);

        boolean result = productController.DecreaseProductQuantity(productId, 5);
        assertTrue(result);

        Product retrieved = productController.getProduct(productId);
        assertEquals(25, retrieved.getStock());
    }

    @Test
    void testDecreaseProductQuantity_InsufficientStock_ReturnsFalse() throws Exception {
        int productId = productController.AddProduct("Cable", "USB-C", 9.99, category, 3);

        boolean result = productController.DecreaseProductQuantity(productId, 10);
        assertFalse(result);
    }

    @Test
    void testGetProductList_Success() throws Exception {
        productController.AddProduct("Product1", "Desc1", 10.0, category, 5);
        productController.AddProduct("Product2", "Desc2", 20.0, category, 10);
        productController.AddProduct("Product3", "Desc3", 30.0, category, 15);

        List<Product> products = productController.GetProductList();
        assertEquals(3, products.size());
    }

    @Test
    void testGetProductList_Empty() throws Exception {
        List<Product> products = productController.GetProductList();
        assertEquals(0, products.size());
    }
}