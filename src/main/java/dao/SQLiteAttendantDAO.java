package dao;

import domainModel.Attendant;
import domainModel.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAttendantDAO implements AttendantDAO {

    @Override
    public Attendant get(String fc) throws SQLException {
        Connection conn = Database.getConnection();
        Attendant attendant = null;
        PreparedStatement ps = conn.prepareStatement("select * from attendant where fiscal_code = ?");
        ps.setString(1, fc);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            attendant = new Attendant(
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("fiscal_code"),
                    rs.getString("iban")
            );
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return attendant;
    }

    @Override
    public List<Attendant> getAll() throws SQLException {
        Connection conn = Database.getConnection();
        List<Attendant> attendants = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement("select * from attendant");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            attendants.add(new Attendant(
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("fiscal_code"),
                    rs.getString("iban")
            ));
        }
        rs.close();
        ps.close();
        Database.closeConnection(conn);

        return attendants;
    }

    @Override
    public void insert(Attendant attendant) throws SQLException {
        Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement("INSERT INTO attendant(fiscal_code, name, surname, iban) VALUES (?, ?, ?,?)");
        ps.setString(1, attendant.getCf());
        ps.setString(2, attendant.getFirstName());
        ps.setString(3, attendant.getLastName());
        ps.setString(4, attendant.getIban());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(conn);
    }

    @Override
    public void update(Attendant attendant) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("UPDATE attendant SET name = ?, surname = ?, iban=? WHERE fiscal_code = ?");
        ps.setString(1, attendant.getFirstName());
        ps.setString(2, attendant.getLastName());
        ps.setString(3, attendant.getIban());
        ps.setString(4, attendant.getCf());
        ps.executeUpdate();

        ps.close();
        Database.closeConnection(connection);
    }

    @Override
    public boolean delete(String cf) throws SQLException {
        Connection connection = Database.getConnection();
        PreparedStatement ps = connection.prepareStatement("DELETE FROM attendant WHERE fiscal_code = ?");
        ps.setString(1, cf);
        int rows = ps.executeUpdate();

        //todo attivare delete on cascade for remove order when user remove customer

        ps.close();
        Database.closeConnection(connection);
        return rows > 0;
    }
}
