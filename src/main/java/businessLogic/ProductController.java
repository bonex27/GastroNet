package businessLogic;

import dao.ProductDAO;
import domainModel.Category;
import domainModel.Product;
import domainModel.Search.Search;

import java.util.ArrayList;
import java.util.List;

public class ProductController {
    ProductDAO productDAO;

    public ProductController(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product getProduct(int id) throws Exception {
        return productDAO.get(id);
    }

    public boolean deleteProduct(int id) throws Exception {
        return productDAO.delete(id);
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
    public boolean DecreaseProductQuantity(int idProduct, int quantity) throws Exception {
        Product product = productDAO.get(idProduct);
        return productDAO.DecreaseStock(product,quantity);
    }

    public List<Product> GetProductList() throws Exception {
        return productDAO.getAll();
    }

    public List<Product> Search(Search search) throws Exception {
        //TODO da implementare ricerca, il param search sarà un extends di decorator. Per effettuare una ricarca filtrata. (Price, availab)
        return null;
    }
}
