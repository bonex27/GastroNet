package dao;

import domainModel.Category;
import domainModel.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLiteProductDAO implements ProductDAO {

    private final CategoryDAO categoryDAO;

    public SQLiteProductDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Product get(Integer id) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from products where id = ?");
        ps.setString(1, id.toString());
        ResultSet rs = ps.executeQuery();
        Product product = null;

        if (rs.next()) {
            product = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    categoryDAO.get(rs.getInt("id_category"))
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return product;
    }

    @Override
    public List<Product> getAll() throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("select * from products");
        ResultSet rs = ps.executeQuery();
        List<Product> products = new ArrayList<>();

        while (rs.next()) {
            products.add(new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price"),
                    rs.getInt("stock"),
                    categoryDAO.get(rs.getInt("id_category"))
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return products;
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
