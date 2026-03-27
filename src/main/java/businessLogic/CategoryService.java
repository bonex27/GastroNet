package businessLogic;

import dao.CategoryDAO;
import domainModel.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    /**
     * Builds a service with the given category DAO.
     *
     * @param categoryDAO DAO used to manage categories.
     */
    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    /**
     * Creates and persists a new category.
     *
     * @param description category description.
     * @return created category instance.
     * @throws SQLException if the DAO operation fails.
     */
    public Category CreateCategory(String description) throws SQLException {
        Category category = new Category(description);
        categoryDAO.insert(category);
        return category;
    }

    public List<Category> GetAllCategories() throws SQLException {
        List<Category> categories = categoryDAO.getAll();
        return categories;
    }
}
