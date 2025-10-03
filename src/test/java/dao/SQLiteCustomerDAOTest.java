package dao;

import domainModel.Customer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SQLiteCustomerDAOTest {
    private SQLiteCustomerDAO customerDAO;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        //Set up database before test
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp () throws SQLException {
        customerDAO = new SQLiteCustomerDAO();
        Database.getConnection().prepareStatement("DELETE FROM Customers").executeUpdate();

        Database.getConnection().prepareStatement("INSERT INTO customers(fiscal_code, name, surname, payment_method) VALUES ('AAABBB01A12B123A','Pietro', 'Bonechi','Card')").executeUpdate();
        Database.getConnection().prepareStatement("INSERT INTO customers(fiscal_code, name, surname, payment_method) VALUES ('BBBCCC01A12B123A','Andrea', 'Petrucci','DebitCard')").executeUpdate();

    }

    @Test
    void testGetCustomerByCF() throws SQLException {
        Customer addedCustomer = customerDAO.get("AAABBB01A12B123A");
        Assertions.assertNotNull(addedCustomer);
        Assertions.assertEquals("AAABBB01A12B123A",addedCustomer.getCf());
        Assertions.assertEquals("Pietro",addedCustomer.getFirstName());
        Assertions.assertEquals("Bonechi",addedCustomer.getLastName());
        Assertions.assertEquals("Card",addedCustomer.getPaymentMethod());

    }

    @Test
    void testGetAllCustomer() throws SQLException {

        List<Customer> customers;
        customers = customerDAO.getAll();

        Assertions.assertNotNull(customers);
        Assertions.assertEquals(2,customers.size());

    }

    @Test
    void testInsertCustomer() throws SQLException {
        Customer c1 = new Customer("Mario","Rossi","CCC1234DDD","Card");
        customerDAO.insert(c1);

        Customer addedCustomer = customerDAO.get("CCC1234DDD");

        Assertions.assertNotNull(addedCustomer);
        Assertions.assertEquals("CCC1234DDD", addedCustomer.getCf());
        Assertions.assertEquals("Mario", addedCustomer.getFirstName());
        Assertions.assertEquals("Rossi", addedCustomer.getLastName());
        Assertions.assertEquals("Card", addedCustomer.getPaymentMethod());
    }

    @Test
    void testUpdateCustomer() throws SQLException {

        Customer updatedCustomer =  new Customer("NewPietro","NewBonechi","AAABBB01A12B123A","NewCard");
        customerDAO.update(updatedCustomer);

        updatedCustomer = customerDAO.get("AAABBB01A12B123A");
        Assertions.assertNotNull(updatedCustomer);
        Assertions.assertEquals("AAABBB01A12B123A",updatedCustomer.getCf());
        Assertions.assertEquals("NewPietro",updatedCustomer.getFirstName());
        Assertions.assertEquals("NewBonechi",updatedCustomer.getLastName());
        Assertions.assertEquals("NewCard",updatedCustomer.getPaymentMethod());
    }
    @Test
    void testDeleteCustomer() throws SQLException {
        customerDAO.delete("AAABBB01A12B123A");
        Customer deletedCustomer = customerDAO.get("CCC1234DDD");
        Assertions.assertNull(deletedCustomer);

    }
}
