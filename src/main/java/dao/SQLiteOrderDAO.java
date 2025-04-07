package dao;

import domainModel.Category;
import domainModel.Order;
import domainModel.OrderState.*;
import domainModel.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteOrderDAO implements OrderDAO {

    private final CustomerDAO customerDAO;

    public SQLiteOrderDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    public Order get(Integer id) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from orders where id = ?");
        ps.setString(1, id.toString());
        ResultSet rs = ps.executeQuery();
        Order order = null;

        OrderState state;
        if (Objects.equals(rs.getString("state"), "Pending"))
            state = new PendingState();
        else if (Objects.equals(rs.getString("state"), "Preparation"))
            state = new PreparationState();
        else if (Objects.equals(rs.getString("state"), "Ready"))
            state = new ReadyState();
        else if (Objects.equals(rs.getString("state"), "Delivered"))
            state = new DeliveredState();
        else
            state = new CustomerChoosingState();


        if (rs.next()) {
            order = new Order(
                    rs.getInt("id"),
                    customerDAO.get(rs.getString("id_customer")),
                    state
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return order;
    }

    @Override
    public List<Order> getAll() throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from orders");
        ResultSet rs = ps.executeQuery();
        List<Order> orders = new ArrayList<>();
        OrderState state;

        while (rs.next()) {
            if (Objects.equals(rs.getString("state"), "Pending"))
                state = new PendingState();
            else if (Objects.equals(rs.getString("state"), "Preparation"))
                state = new PreparationState();
            else if (Objects.equals(rs.getString("state"), "Ready"))
                state = new ReadyState();
            else if (Objects.equals(rs.getString("state"), "Delivered"))
                state = new DeliveredState();
            else
                state = new CustomerChoosingState();

            orders.add(new Order(
                    rs.getInt("id"),
                    customerDAO.get(rs.getString("id_customer")),
                    state
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return orders;
    }

    @Override
    public void insert(Order order) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders(id_customer, state) VALUES (?,?)");
        ps.setString(1, order.getCustomerId());
        ps.setString(2, order.getState());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public void update(Order order) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE Orders SET state = (?) WHERE id = (?)");
        ps.setString(1, order.getState());
        ps.setInt(2, order.getId());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    //TODO: delete on cascade SQLiteOrderDAO
    @Override
    public boolean delete(Integer id) throws Exception {
        Order order = this.get(id);
        if (order == null)
            return false;
        Connection connection= Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM Orders WHERE id = (?)");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return (rows > 0);
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

    @Override
    public void addProductToOrder(int ProductId, int OrderId) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO ProductOrder(product_id, order_id) VALUES (?,?)");
        ps.setInt(1, ProductId);
        ps.setInt(2, OrderId);
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public boolean removeProductToOrder(int ProductId, int OrderId) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM ProductOrder WHERE order_id = ? and product_id = ?");
        ps.setInt(1, OrderId);
        ps.setInt(2, ProductId);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return rows > 0;
    }
}
