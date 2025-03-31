package dao;

import domainModel.Category;
import domainModel.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class SQLiteCategoryDAO implements CategoryDAO {
    @Override
    public Category get(Integer id) throws Exception {
        Connection conn = Database.getConnection();
        Category category = null;
        PreparedStatement ps = conn.prepareStatement("select * from category where id = ?");
        ps.setString(1, id.toString());
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            category = new Category(
                    rs.getInt("id"),
                    rs.getString("description")
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return  category;
    }

    @Override
    public List<Category> getAll() throws Exception {
        return List.of();
    }

    @Override
    public void insert(Category category) throws Exception {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO Category(description) VALUES (?)");
        ps.setString(1, category.getDescription());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public void update(Category category) throws Exception {

    }

    @Override
    public boolean delete(Integer integer) throws Exception {
        return false;
    }

    @Override
    public int getNewId() throws Exception {
        Connection connection = Database.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM category");
        int id = rs.getInt(1) + 1;

        rs.close();
        stmt.close();
        Database.closeConnection(connection);
        return id;
    }
}
