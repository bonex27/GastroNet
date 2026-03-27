package dao;

import dao.query.SearchQuery;
import domainModel.Product;

import java.util.List;

public interface ProductDAO extends DAO<Product,Integer> {
    public int GetNextId() throws Exception;

    public void IncreaseStock(Product product,int quantity) throws Exception;

    public boolean DecreaseStock(Product product,int quantity) throws Exception;

    public List<Product> search(SearchQuery query) throws Exception;
}
