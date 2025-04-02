package dao;

import domainModel.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SQLiteProductDAO implements ProductDAO {

    @Override
    public Product get(Integer integer) throws Exception {
        return null;
    }

    @Override
    public List<Product> getAll() throws Exception {
        return List.of();
    }

    @Override
    public void insert(Product product) throws Exception {
        Connection con = Database.getConnection();
        PreparedStatement ps = con.prepareStatement("INSERT INTO Products (name, description, price, id_category, stock) VALUES (?, ?, ?, ?, ?)");
        // id is not needed because is autoincrement
        ps.setString(1, product.getName());
        ps.setString(2, product.getDescription());
        ps.setDouble(3, product.getPrice());
        ps.setInt(4, (product.getCategory().getId()));
        ps.setInt(5, product.getStock());
        ps.executeUpdate();

        ps.close();

        Database.closeConnection(con);
    }

    @Override
    public void update(Product product) throws Exception {

    }

    @Override
    public boolean delete(Integer integer) throws Exception {
        return false;
    }

    @Override
    public int GetNextId() throws Exception {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM products");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }

    @Override
    public void IncreaseStock(Product product, int quantity) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE Products SET stock = ? WHERE id = ?");
        ps.setInt(1, quantity + product.getStock());
        ps.setInt(2, product.getId());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public boolean DecreaseStock(Product product, int quantity) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE Products SET stock = ? WHERE id = ? and stock > ?");
        ps.setInt(1, quantity - product.getStock());
        ps.setInt(2, product.getId());
        ps.setInt(2, product.getStock());
        int updated = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return updated > 0;
    }
}
