package dao;

import domainModel.Attendant;
import domainModel.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SQLiteCustomerDAO implements CustomerDAO {

    @Override
    public Customer get(String fc) throws Exception {
        Connection conn = Database.getConnection();
        Customer customer = null;
        PreparedStatement ps = conn.prepareStatement("select * from customers where fiscal_code = ?");
        ps.setString(1, fc);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            customer = new Customer(
                    rs.getString("fiscal_code"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("paymentMethod")
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return customer;
    }

    @Override
    public List<Customer> getAll() throws Exception {
        Connection conn = Database.getConnection();
        List<Customer> customers = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("select * from customers");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            customers.add(new Customer(
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("fiscal_code"),
                    rs.getString("payment_method")
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return customers;
    }

    @Override
    public void insert(Customer customer) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO customers(fiscal_code, name, surname, payment_method) VALUES (?, ?, ?,?)");
        ps.setString(1, customer.getCf());
        ps.setString(2, customer.getFirstName());
        ps.setString(3, customer.getLastName());
        ps.setString(4, customer.getPaymentMethod());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public void update(Customer customer) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE customers SET name = ?, surname = ?, payment_method=? WHERE fiscal_code = ?");
        ps.setString(1, customer.getFirstName());
        ps.setString(2, customer.getLastName());
        ps.setString(3, customer.getPaymentMethod());
        ps.setString(4, customer.getCf());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public boolean delete(String cf) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM customers WHERE fiscal_code = ?");
        ps.setString(1, cf);
        int rows = ps.executeUpdate();

        //todo attivare delete on cascade for remove order when user remove customer

        ps.close();
        Database.closeConnection(connection);
        return rows > 0;
    }
}
