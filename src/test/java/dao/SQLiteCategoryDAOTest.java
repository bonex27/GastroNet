package dao;

import domainModel.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

class SQLiteCategoryDAOTest {
    private SQLiteCategoryDAO categoryDao;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        //Set up database before test
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp () throws SQLException {
        categoryDao = new SQLiteCategoryDAO();
        Database.getConnection().prepareStatement("DELETE FROM category").executeUpdate();
    }

    @Test
    void testGetTag() throws SQLException {
        Category catToAdd = new  Category("Dessert");
        categoryDao.insert(catToAdd);

        Category addedCategory = categoryDao.get("Dessert");
        Assertions.assertNotNull(addedCategory);
        Assertions.assertEquals("Dessert",addedCategory.getDescription());
    }

    @Test
    void testGetAll() throws SQLException {
        Category category = new  Category("Dessert");
        Category category2= new  Category("Drink");
        categoryDao.insert(category);
        categoryDao.insert(category2);

        List<Category> allCategories = categoryDao.getAll();
        Assertions.assertEquals(2, allCategories.size());
    }

    @Test
    void testInsert() throws SQLException {
        Category category = new  Category("Dessert");
        categoryDao.insert(category);

        Category addedCategory = categoryDao.get("Dessert");
        Assertions.assertNotNull(addedCategory);
        Assertions.assertEquals("Dessert",addedCategory.getDescription());
    }

    @Test
    void  testDelete() throws SQLException {
        Category category = new  Category("Dessert");
        categoryDao.insert(category);

        boolean removed = categoryDao.delete("Dessert");

        Assertions.assertTrue(removed);
        Category removedCategory = categoryDao.get("Dessert");
        Assertions.assertNull(removedCategory);
    }


}
