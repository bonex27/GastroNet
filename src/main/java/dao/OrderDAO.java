package dao;

import domainModel.Order;
import domainModel.Product;
import domainModel.OrderState.OrderState;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO extends DAO<Order, Integer> {
    public int GetNextId() throws Exception;

    public List<Order> getByUser(String CustomerId) throws SQLException;

    public List<Product> getProducts(int OrderId) throws SQLException;

    public void addProductToOrder(int ProductId, int OrderId) throws SQLException;

    public boolean removeProductToOrder(int ProductId, int OrderId) throws SQLException;

    public void changeState(Integer id, OrderState newState) throws SQLException;
}
