package dao;

import domainModel.Order;
import domainModel.OrderState.PendingState;
import domainModel.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        Database.getConnection()
                .prepareStatement("INSERT INTO Customers(fiscal_code, name, surname, payment_method) VALUES ('AAA','Pietro', 'Bonechi','Card')").executeUpdate();
        Database.getConnection()
                .prepareStatement("INSERT INTO Customers(fiscal_code, name, surname, payment_method) VALUES ('BBB','Andrea', 'Petrucci','At delivery')").executeUpdate();

        Database.getConnection()
                .prepareStatement("INSERT INTO Orders (id, id_customer, state) VALUES (1,'AAA','CustomerChoosing')").executeUpdate();
        Database.getConnection()
                .prepareStatement("INSERT INTO Orders (id, id_customer, state) VALUES (2,'BBB','CustomerChoosing')").executeUpdate();
        Database.getConnection()
                .prepareStatement("insert into  ProductOrder (order_id, product_id) VALUES (1,1)").executeUpdate();;
        Database.getConnection()
                .prepareStatement("insert into  ProductOrder (order_id, product_id) VALUES (1,2)").executeUpdate();;

    }

    @Test
    void testGetOrderById() throws SQLException{
        Order createdOrder = orderDAO.get(1);
        Assertions.assertNotNull(createdOrder);
        Assertions.assertEquals("CustomerChoosing",createdOrder.getState());
        Assertions.assertEquals(2,createdOrder.getProducts().size());
        Assertions.assertEquals("AAA",createdOrder.getCustomerId());
    }

    @Test
    void testGetAllOrders() throws SQLException{
        List<Order> createdOrders = orderDAO.getAll();
        Assertions.assertNotNull(createdOrders);
        Assertions.assertEquals(2,createdOrders.size());
    }

    @Test
    void testInsertOrder() throws SQLException{
        Order createdOrder = new Order(orderDAO.GetNextId(),customerDAO.get("AAA"));
        orderDAO.insert(createdOrder);
        Order insertedOrder = orderDAO.get(createdOrder.getId());
        Assertions.assertNotNull(insertedOrder);
        Assertions.assertEquals("CustomerChoosing",insertedOrder.getState());
        Assertions.assertEquals(0,insertedOrder.getProducts().size());
    }

    @Test
    void testUpdateOrder() throws SQLException{
        Order order = new Order(1,customerDAO.get("AAA"),new PendingState(),null);
        orderDAO.update(order);
        Order updatedOrder = orderDAO.get(1);
        Assertions.assertNotNull(updatedOrder);
        Assertions.assertEquals(2,updatedOrder.getProducts().size());
        Assertions.assertEquals("AAA",updatedOrder.getCustomerId());
        Assertions.assertEquals(new PendingState().getState(),updatedOrder.getState());

    }

    @Test
    void  testDeleteOrder() throws SQLException{
        orderDAO.delete(1);
        Order deletedOrder = orderDAO.get(1);
        Assertions.assertNull(deletedOrder);


    }
}
