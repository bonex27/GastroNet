package dao;

import domainModel.Order;
import domainModel.Product;
import domainModel.OrderState.OrderState;

import java.util.List;

public interface OrderDAO extends DAO<Order, Integer> {
    public int GetNextId() throws Exception;

    public List<Product> getProducts(int OrderId) throws Exception;

    public void addProductToOrder(int ProductId, int OrderId) throws Exception;

    public boolean removeProductToOrder(int ProductId, int OrderId) throws Exception;

    public void changeState(Integer id, OrderState newState) throws Exception;
}
