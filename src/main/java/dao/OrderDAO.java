package dao;

import domainModel.Order;

public interface OrderDAO  extends DAO<Order,Integer> {
    public int GetNextId() throws Exception;
    public  void addProductToOrder(int ProductId, int OrderId) throws Exception;
    public  boolean removeProductToOrder(int ProductId, int OrderId) throws Exception;

}
