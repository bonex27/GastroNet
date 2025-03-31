package businessLogic;

import dao.CategoryDAO;
import domainModel.Category;

public class CategoryController {
    public final CategoryDAO categoryDAO;

    public CategoryController(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category CreateCategory(String description) throws Exception {
        Category category = new Category(categoryDAO.getNewId(), description);
        categoryDAO.insert(category);
        return category;
    }
}
