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

    public void IncreaseProductQuantity(int idProduct, int quantity) throws Exception {
        Product product = productDAO.get(idProduct);
        productDAO.IncreaseStock(product,quantity);
    }
    public boolean Decrease(int idProduct, int quantity) throws Exception {
        Product product = productDAO.get(idProduct);
        return productDAO.DecreaseStock(product,quantity);
    }
}
