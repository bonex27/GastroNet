package dao;

import domainModel.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SQLiteProductDAO implements ProductDAO {

    //TODO: get() pruduct
    @Override
    public Product get(Integer integer) throws Exception {
        return null;
    }

    //TODO: getAll() pruduct
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


    //TODO: update() pruduct
    @Override
    public void update(Product product) throws Exception {

    }

    //TODO: delete() pruduct
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
}
