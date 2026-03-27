package businessLogic;

import dao.ProductDAO;
import domainModel.Category;
import domainModel.Product;
import domainModel.Search.Search;
import domainModel.Search.SearchQuery;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    /**
     * Builds a service with the given product data access.
     *
     * @param productDAO DAO used to load and persist products.
     */
    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * Fetches a product by id.
     *
     * @param id product id.
     * @return product instance if found.
     * @throws Exception if the DAO operation fails.
     */
    public Product getProduct(int id) throws Exception {
        return productDAO.get(id);
    }

    /**
     * Deletes a product by id.
     *
     * @param id product id.
     * @return true if the product was deleted.
     * @throws Exception if the DAO operation fails.
     */
    public boolean deleteProduct(int id) throws Exception {
        return productDAO.delete(id);
    }

    /**
     * Creates and persists a new product, returning its generated id.
     *
     * @param name product name.
     * @param description product description.
     * @param price product price.
     * @param category product category.
     * @param stock initial stock quantity.
     * @return id of the created product.
     * @throws Exception if the DAO operation fails.
     */
    public int AddProduct(String name, String description, double price, Category category, int stock) throws Exception {
        Product product = new Product(productDAO.GetNextId(),name, description, price, stock,category);
        productDAO.insert(product);
        return product.getId();
    }

    /**
     * Increases the stock for the given product.
     *
     * @param idProduct product id.
     * @param quantity amount to add.
     * @throws Exception if the DAO operation fails.
     */
    public void IncreaseProductQuantity(int idProduct, int quantity) throws Exception {
        Product product = productDAO.get(idProduct);
        productDAO.IncreaseStock(product,quantity);
    }

    /**
     * Decreases the stock for the given product.
     *
     * @param idProduct product id.
     * @param quantity amount to remove.
     * @return true if the stock was decreased.
     * @throws Exception if the DAO operation fails.
     */
    public boolean DecreaseProductQuantity(int idProduct, int quantity) throws Exception {
        Product product = productDAO.get(idProduct);
        return productDAO.DecreaseStock(product,quantity);
    }

    /**
     * Returns the full list of products.
     *
     * @return list of all products.
     * @throws Exception if the DAO operation fails.
     */
    public List<Product> GetProductList() throws Exception {
        return productDAO.getAll();
    }

    /**
     * Executes a search and returns matching products.
     *
     * @param search search strategy.
     * @return list of matching products.
     * @throws Exception if the DAO operation fails.
     */
    public List<Product> Search(Search search) throws Exception {
        SearchQuery query = search.getSearchQuery();
        System.out.println(query.getSql());
        return productDAO.search(query);
    }
}
