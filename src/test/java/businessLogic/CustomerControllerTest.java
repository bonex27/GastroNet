package businessLogic;

import dao.Database;
import dao.SQLiteCustomerDAO;
import domainModel.Customer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerControllerTest {
    private CustomerController customerController;
    private Customer testCustomer;

    @BeforeAll
    static void initDb() throws SQLException, IOException {
        //Set up database before test
        Database.setDatabase("TestDB.db");
        Database.initDatabase();
    }

    @BeforeEach
    void setUp () throws SQLException {
        Database.getConnection().prepareStatement("DELETE FROM Customers").executeUpdate();
        Database.getConnection().prepareStatement("DELETE FROM sqlite_sequence").executeUpdate();

        SQLiteCustomerDAO customerDAO = new SQLiteCustomerDAO();
        customerController = new CustomerController(customerDAO);
        testCustomer = new Customer("Pietro","Bonechi","AAA","Card");
        customerDAO.insert(testCustomer);

    }

    @Test
    void getCustomerTest() throws Exception {
        Customer customer = customerController.getPerson("AAA");
        Assertions.assertNotNull(customer);
        Assertions.assertEquals("AAA",customer.getCf());
    }

   @Test
    void addCustomerTest() throws Exception {
       Assertions.assertDoesNotThrow(() ->customerController.addCustomer("Mario","Rossi","MMM","Cash"));
       Customer insertedCustomer = customerController.getPerson("MMM");
       Assertions.assertNotNull(insertedCustomer);
       Assertions.assertEquals("MMM",insertedCustomer.getCf());
   }
   @Test
   void addCustomerTest_alreadyExisting()
   {
       Assertions.assertThrows(Exception.class,() ->customerController.addCustomer("Pietro","Bonechi","AAA","Cash"));
   }

    @Test
    void deleteCustomerTest_ResultTrue() throws Exception {
        boolean result = customerController.deletePerson("AAA");
        Customer c = customerController.getPerson("AAA");
        Assertions.assertTrue(result);
        Assertions.assertNull(c);

    }
    @Test
    void deleteCustomerTest_ResultFalse() throws Exception {
        boolean result = customerController.deletePerson("CCC");
        Assertions.assertFalse(result);

    }

}
