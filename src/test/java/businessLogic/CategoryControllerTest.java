package businessLogic;

import dao.SQLiteCategoryDAO;
import dao.Database;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import domainModel.Category;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class CategoryControllerTest {

    private CategoryController categoryController;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp() throws SQLException {
        Database.getConnection().prepareStatement("DELETE FROM main.Category").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();

        SQLiteCategoryDAO categoryDAO = new SQLiteCategoryDAO();
        categoryController = new CategoryController(categoryDAO);
    }

    @Test
    void testCreateCategory_Success() throws SQLException {
        Category category = categoryController.CreateCategory("Elettronica");

        assertNotNull(category);
        assertEquals("Elettronica", category.getDescription());
    }

    @Test
    void testCreateCategory_Multiple() throws SQLException {
        categoryController.CreateCategory("Elettronica");
        categoryController.CreateCategory("Abbigliamento");
        categoryController.CreateCategory("Alimentari");

        List<Category> categories = categoryController.categoryDAO.getAll();
        assertEquals(3, categories.size());
    }

    @Test
    void testCreateCategory_EmptyDescription() throws SQLException {
        Category category = categoryController.CreateCategory("");
        assertNotNull(category);
        assertEquals("", category.getDescription());
    }
}
