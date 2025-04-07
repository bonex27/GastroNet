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

    @Override
    public void update(Product product) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE Products SET name = (?), description = (?), price = (?), stock = (?), id_category = (?) WHERE id = (?)");
        ps.setString(1, product.getName());
        ps.setString(1, product.getDescription());
        ps.setDouble(1, product.getPrice());
        ps.setInt(1, product.getStock());
        ps.setInt(1, product.getCategory().getId());
        ps.setInt(1, product.getId());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public boolean delete(Integer id) throws Exception {
        Product product = this.get(id);
        if (product == null)
            return false;
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM Products WHERE id = (?)");
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
        ps.setInt(1, product.getStock()- quantity);
        ps.setInt(2, product.getId());
        ps.setInt(3, quantity);
        int updated = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return updated > 0;
    }

    @Override
    public List<Product> search(String query) throws Exception {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        List<Product> products = new ArrayList<>();

        while (rs.next()){
            products.add(this.get(rs.getInt("id")));
        }

        rs.close();
        ps.close();
        Database.closeConnection(connection);
        return products;
    }
}
