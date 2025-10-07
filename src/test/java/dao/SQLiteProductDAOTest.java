package dao;

import domainModel.Product;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SQLiteProductDAOTest {

    private CategoryDAO categoryDAO;
    private SQLiteProductDAO productDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        //Set up database before test
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp () throws SQLException {
        categoryDAO = new SQLiteCategoryDAO();
        productDAO = new SQLiteProductDAO(categoryDAO);
        Database.getConnection().prepareStatement("DELETE FROM Products").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM Category").executeUpdate();

        Database.getConnection().prepareStatement("INSERT INTO Category (description) values ('Dessert')").executeUpdate();
        Database.getConnection().prepareStatement("INSERT INTO Category (description) values ('First Course')").executeUpdate();

        Database.getConnection()
                .prepareStatement("INSERT INTO Products (id, name, description, price, descCategory, stock) values (1,'Chioccolate cake','Chioccolate cake with some' ,5,'Dessert',10)").executeUpdate();
        Database.getConnection()
                .prepareStatement("INSERT INTO Products (id, name, description, price, descCategory, stock) values (2,'Pasta with pesto','Pasta  with  pesto' ,10,'First Course',5)").executeUpdate();

    }

    @Test
    void getAllProducts () throws SQLException {
        List<Product> products;
        products = productDAO.getAll();
        Assertions.assertNotNull(products);
        Assertions.assertEquals(2,products.size());
    }

    @Test
    void getProductById () throws SQLException {
        Product product;
        product = productDAO.get(1);
        Assertions.assertNotNull(product);
        Assertions.assertEquals("Chioccolate cake",product.getName());
        Assertions.assertEquals("Chioccolate cake with some",product.getDescription());
        Assertions.assertEquals("Dessert",product.getCategory().getDescription());
        Assertions.assertEquals(10,product.getStock());
        Assertions.assertEquals(5,product.getPrice());
    }

    @Test
    void insertProduct () throws SQLException {
        Product product = new Product(productDAO.GetNextId(),"Chioccolate muffin", "Muffin with some chioccolate", 7.5,14,categoryDAO.get("Dessert"));
        productDAO.insert(product);

        Product insertedProduct = productDAO.get(product.getId());
        Assertions.assertNotNull(insertedProduct);
        Assertions.assertEquals(product.getName(),insertedProduct.getName());
        Assertions.assertEquals(product.getDescription(),insertedProduct.getDescription());
        Assertions.assertEquals(product.getStock(),insertedProduct.getStock());
        Assertions.assertEquals(product.getCategory().getDescription(),insertedProduct.getCategory().getDescription());
        Assertions.assertEquals(product.getPrice(),insertedProduct.getPrice());
    }

    @Test
    void updateProduct () throws SQLException {
        Product product = new Product(1,"Chioccolate muffin", "Muffin with some chioccolate", 7.5,14,categoryDAO.get("Dessert"));
        productDAO.update(product);
        Product updatedProduct = productDAO.get(1);
        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals("Chioccolate muffin",updatedProduct.getName());
        Assertions.assertEquals("Muffin with some chioccolate",updatedProduct.getDescription());
        Assertions.assertEquals(7.5,updatedProduct.getPrice());
        Assertions.assertEquals(categoryDAO.get("Dessert").getDescription(),updatedProduct.getCategory().getDescription());
        Assertions.assertEquals(14,updatedProduct.getStock());
    }

    @Test
    void deleteProduct () throws SQLException {
        productDAO.delete(1);
        Product deletedProduct = productDAO.get(1);
        Assertions.assertNull(deletedProduct);
    }

    @Test
    void testDecreaseStock () throws SQLException {
        Product product;
        product = productDAO.get(1);
        productDAO.DecreaseStock(product,5);
        product = productDAO.get(1);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(5,product.getStock());
    }

    @Test
    void testIncreaseStock () throws SQLException {
        Product product;
        product = productDAO.get(1);
        productDAO.IncreaseStock(product,5);
        product = productDAO.get(1);

        Assertions.assertNotNull(product);
        Assertions.assertEquals(15,product.getStock());
    }

}
