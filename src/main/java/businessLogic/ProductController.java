package businessLogic;

import dao.ProductDAO;
import domainModel.Category;
import domainModel.Product;

public class ProductController {
    ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public int AddProduct(String name, String description, double price, Category category, int stock) throws Exception {
        Product product = new Product(productDAO.GetNextId(),name, description, price, stock,category);
        productDAO.insert(product);
        return product.getId();
    }
}
