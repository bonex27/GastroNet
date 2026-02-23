package dao;

import domainModel.Order;
import domainModel.OrderState.*;
import domainModel.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SQLiteOrderDAO implements OrderDAO {

    private final CustomerDAO customerDAO;
    private final ProductDAO productDAO;

    public SQLiteOrderDAO(CustomerDAO customerDAO, ProductDAO productDAO) {
        this.customerDAO = customerDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Order get(Integer id) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from orders where id = ?");
        ps.setString(1, id.toString());
        ResultSet rs = ps.executeQuery();
        Order order = null;

        if (rs.next()) {
            OrderState state = toState(rs.getString("state"));
            order = new Order(
                    rs.getInt("id"),
                    customerDAO.get(rs.getString("id_customer")),
                    state,
                    this.getProducts(rs.getInt("id"))
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return order;
    }

    @Override
    public List<Order> getAll() throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from orders");
        ResultSet rs = ps.executeQuery();
        List<Order> orders = new ArrayList<>();
        OrderState state;

        while (rs.next()) {
            state = toState(rs.getString("state"));

            orders.add(new Order(
                    rs.getInt("id"),
                    customerDAO.get(rs.getString("id_customer")),
                    state,
                    this.getProducts(rs.getInt("id"))
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return orders;
    }

    @Override
    public List<Order> getByUser(String id) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from orders where id_customer = ?");
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        List<Order> orders = new ArrayList<>();
        OrderState state;

        while (rs.next()) {
            state = toState(rs.getString("state"));

            orders.add(new Order(
                    rs.getInt("id"),
                    customerDAO.get(rs.getString("id_customer")),
                    state,
                    this.getProducts(rs.getInt("id"))
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return orders;
    }

    @Override
    public void insert(Order order) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Orders(id, id_customer, state) VALUES (?,?,?)");
        ps.setInt(1, order.getId());
        ps.setString(2, order.getCustomerId());
        ps.setString(3, order.getState());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public void update(Order order) throws SQLException {
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
    public boolean delete(Integer id) throws SQLException {
        Order order = this.get(id);
        if (order == null)
            return false;
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM Orders WHERE id = (?)");
        ps.setInt(1, id);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return (rows > 0);
    }

    @Override
    public int GetNextId() throws SQLException {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM Orders");
        int id = 1;
        if (rs.next()) {
            id = rs.getInt(1) + 1;
        }

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }

    private OrderState toState(String stateValue) {
        if (Objects.equals(stateValue, "Pending"))
            return new PendingState();
        if (Objects.equals(stateValue, "Preparation"))
            return new PreparationState();
        if (Objects.equals(stateValue, "Ready"))
            return new ReadyState();
        if (Objects.equals(stateValue, "Delivered"))
            return new DeliveredState();
        return new CustomerChoosingState();
    }

    @Override
    public List<Product> getProducts(int OrderId) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from ProductOrder where order_id = (?)");
        ps.setInt(1, OrderId);
        ResultSet rs = ps.executeQuery();
        List<Product> products = new ArrayList<>();

        while (rs.next()) {
            products.add(productDAO.get(rs.getInt("product_id")));
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return products;
    }

    @Override
    public void addProductToOrder(int ProductId, int OrderId) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO ProductOrder(product_id, order_id) VALUES (?,?)");
        ps.setInt(1, ProductId);
        ps.setInt(2, OrderId);
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public boolean removeProductToOrder(int ProductId, int OrderId) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM ProductOrder WHERE order_id = ? and product_id = ?");
        ps.setInt(1, OrderId);
        ps.setInt(2, ProductId);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return rows > 0;
    }

    @Override
    public void changeState(Integer id, OrderState newState) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE orders SET state = (?) WHERE id = (?)");
        ps.setString(1, newState.getState());
        ps.setInt(2, id);
        ps.executeUpdate();
        ps.close();
        Database.closeConnection(connection);
    }
}
