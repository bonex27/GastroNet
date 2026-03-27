package businessLogic;

import dao.Database;
import dao.SQLiteCustomerDAO;
import domainModel.Customer;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerServiceTest {
    private CustomerService customerService;
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
        customerService = new CustomerService(customerDAO);
        testCustomer = new Customer("Pietro","Bonechi","AAA","Card");
        customerDAO.insert(testCustomer);

    }

    @Test
    void getCustomerTest() throws Exception {
        Customer customer = customerService.getPerson("AAA");
        Assertions.assertNotNull(customer);
        Assertions.assertEquals("AAA",customer.getCf());
    }

   @Test
    void addCustomerTest() throws Exception {
       Assertions.assertDoesNotThrow(() ->customerService.addCustomer("Mario","Rossi","MMM","Cash"));
       Customer insertedCustomer = customerService.getPerson("MMM");
       Assertions.assertNotNull(insertedCustomer);
       Assertions.assertEquals("MMM",insertedCustomer.getCf());
   }
   @Test
   void addCustomerTest_alreadyExisting()
   {
       Assertions.assertThrows(Exception.class,() ->customerService.addCustomer("Pietro","Bonechi","AAA","Cash"));
   }

    @Test
    void deleteCustomerTest_ResultTrue() throws Exception {
        boolean result = customerService.deletePerson("AAA");
        Customer c = customerService.getPerson("AAA");
        Assertions.assertTrue(result);
        Assertions.assertNull(c);

    }
    @Test
    void deleteCustomerTest_ResultFalse() throws Exception {
        boolean result = customerService.deletePerson("CCC");
        Assertions.assertFalse(result);

    }

}
