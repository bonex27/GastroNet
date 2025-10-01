package businessLogic;

import dao.CategoryDAO;
import domainModel.Category;

import java.sql.SQLException;

public class CategoryController {
    public final CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category CreateCategory(String description) throws SQLException {
        Category category = new Category(description);
        categoryDAO.insert(category);
        return category;
    }
}
