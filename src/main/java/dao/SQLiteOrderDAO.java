package dao;

import domainModel.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SQLiteOrderDAO implements OrderDAO {
    @Override
    public Order get(Integer integer) throws Exception {
        return null;
    }

    @Override
    public List<Order> getAll() throws Exception {
        return List.of();
    }

    @Override
    public void insert(Order order) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Orders(customer_id, state) VALUES (?,?)");
        ps.setString(1, order.getCustomerId());
        ps.setString(2, order.getState());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public void update(Order order) throws Exception {

    }

    @Override
    public boolean delete(Integer integer) throws Exception {
        return false;
    }

    @Override
    public int GetNextId() throws Exception {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM Orders");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }
}
