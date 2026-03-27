import businessLogic.AttendantService;
import businessLogic.CategoryService;
import businessLogic.CustomerService;
import businessLogic.OrderService;
import businessLogic.ProductService;
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

    final CustomerService customerService;
    final AttendantService attendantService;
    final CategoryService categoryService;
    final ProductService productService;
    final OrderService orderService;

    public GastroNetContext() throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        customerDAO = new SQLiteCustomerDAO();
        attendantDAO = new SQLiteAttendantDAO();
        categoryDAO = new SQLiteCategoryDAO();
        productDAO = new SQLiteProductDAO(categoryDAO);
        orderDAO = new SQLiteOrderDAO(customerDAO, productDAO);

        customerService = new CustomerService(customerDAO);
        attendantService = new AttendantService(attendantDAO);
        categoryService = new CategoryService(categoryDAO);
        productService = new ProductService(productDAO);
        orderService = new OrderService(orderDAO, productDAO, customerDAO);
    }
}
