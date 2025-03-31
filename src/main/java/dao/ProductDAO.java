package dao;

import domainModel.Product;

import java.util.List;

public interface ProductDAO extends DAO<Product,Integer> {
    public int GetNextId() throws Exception;
}
