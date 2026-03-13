import businessLogic.AttendantController;
import businessLogic.CategoryController;
import businessLogic.CustomerController;
import businessLogic.OrderController;
import businessLogic.ProductController;
import dao.AttendantDAO;
import dao.CategoryDAO;
import dao.CustomerDAO;
import dao.Database;
import dao.OrderDAO;
import dao.ProductDAO;
import dao.SQLiteAttendantDAO;
import dao.SQLiteCategoryDAO;
import dao.SQLiteCustomerDAO;
import dao.SQLiteOrderDAO;
import dao.SQLiteProductDAO;

public class GastroNetContext {
    final CustomerDAO customerDAO;
    final AttendantDAO attendantDAO;
    final CategoryDAO categoryDAO;
    final ProductDAO productDAO;
    final OrderDAO orderDAO;

    final CustomerController customerController;
    final AttendantController attendantController;
    final CategoryController categoryController;
    final ProductController productController;
    final OrderController orderController;

    public GastroNetContext() throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        customerDAO = new SQLiteCustomerDAO();
        attendantDAO = new SQLiteAttendantDAO();
        categoryDAO = new SQLiteCategoryDAO();
        productDAO = new SQLiteProductDAO(categoryDAO);
        orderDAO = new SQLiteOrderDAO(customerDAO, productDAO);

        customerController = new CustomerController(customerDAO);
        attendantController = new AttendantController(attendantDAO);
        categoryController = new CategoryController(categoryDAO);
        productController = new ProductController(productDAO);
        orderController = new OrderController(orderDAO, productDAO, customerDAO);
    }
}
