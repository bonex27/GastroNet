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


public class CategoryServiceTest {

    private CategoryService categoryService;

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
        categoryService = new CategoryService(categoryDAO);
    }

    @Test
    void testCreateCategory_Success() throws SQLException {
        Category category = categoryService.CreateCategory("Primi");

        assertNotNull(category);
        assertEquals("Primi", category.getDescription());
    }

    @Test
    void testCreateCategory_Multiple() throws SQLException {
        categoryService.CreateCategory("Primi");
        categoryService.CreateCategory("Secondi");
        categoryService.CreateCategory("Dolci");

        List<Category> categories = categoryService.GetAllCategories();
        assertEquals(3, categories.size());
    }

    @Test
    void testCreateCategory_EmptyDescription() throws SQLException {
        Category category = categoryService.CreateCategory("");
        assertNotNull(category);
        assertEquals("", category.getDescription());
    }
}
