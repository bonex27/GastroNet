package dao;

import domainModel.Category;
import domainModel.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteCategoryDAO implements CategoryDAO {

    @Override
    public Category get(String desc) throws SQLException {
        Connection connection= Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from category where description=?");
        ps.setString(1, desc);
        ResultSet rs = ps.executeQuery();
        Category category = null;

        if (rs.next()) {
            category = new Category(
                    rs.getString("description")
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return category;
    }

    @Override
    public List<Category> getAll() throws SQLException {
        Connection connection= Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from category");
        ResultSet rs = ps.executeQuery();
        List<Category> categories = new ArrayList<>();

        while (rs.next()) {
            categories.add(new Category(
                    rs.getString("description")
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(connection);

        return categories;
    }

    @Override
    public void insert(Category category) throws SQLException {
        Connection connection= Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("INSERT INTO Category(description) VALUES (?)");
        ps.setString(1, category.getDescription());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public void update(Category category) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean delete(String desc) throws SQLException {
        Category category = this.get(desc);
        if (category == null)
            return false;
        Connection connection= Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM Category WHERE description = ? ");
        ps.setString(1, desc);
        int rows = ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
        return (rows > 0);
    }


}
