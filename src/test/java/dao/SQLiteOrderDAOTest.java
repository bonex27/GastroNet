package dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.sql.SQLException;

public class SQLiteOrderDAOTest {

    private CategoryDAO categoryDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private SQLiteOrderDAO orderDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        //Set up database before test
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp () throws SQLException {
        categoryDAO = new SQLiteCategoryDAO();
        customerDAO = new SQLiteCustomerDAO();
        productDAO = new SQLiteProductDAO(categoryDAO);
        orderDAO = new SQLiteOrderDAO(customerDAO, productDAO);

        Database.getConnection().prepareStatement("DELETE FROM Orders").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM Products").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM Customers").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM Category").executeUpdate();

        Database.getConnection().prepareStatement("INSERT INTO Category (description) values ('Dessert')").executeUpdate();
        Database.getConnection().prepareStatement("INSERT INTO Category (description) values ('First Course')").executeUpdate();

        Database.getConnection()
                .prepareStatement("INSERT INTO Products (id, name, description, price, descCategory, stock) values (1,'Chioccolate cake','Chioccolate cake with some' ,5,'Dessert',10)").executeUpdate();
        Database.getConnection()
                .prepareStatement("INSERT INTO Products (id, name, description, price, descCategory, stock) values (2,'Pasta with pesto','Pasta  with  pesto' ,10,'First Course',5)").executeUpdate();

        Database
                .getConnection().prepareStatement("INSERT INTO Customers(fiscal_code, name, surname, payment_method) VALUES ('AAA','Pietro', 'Bonechi','Card')");
        Database
                .getConnection().prepareStatement("INSERT INTO Customers(fiscal_code, name, surname, payment_method) VALUES ('BBB','Andrea', 'Petrucci','At delivery')");

        //TODO Creare ordine con prodotti per default case
        //TODO Test dei vari metodi
    }
}
