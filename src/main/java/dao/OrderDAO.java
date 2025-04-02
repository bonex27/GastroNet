package dao;

import domainModel.Order;

public interface OrderDAO  extends DAO<Order,Integer> {
    public int GetNextId() throws Exception;
}
