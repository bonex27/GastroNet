import businessLogic.AttendantController;
import businessLogic.CategoryController;
import businessLogic.CustomerController;
import businessLogic.ProductController;
import dao.*;
import domainModel.Attendant;
import domainModel.Category;
import domainModel.Customer;
import domainModel.Person;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        //DAO instance
        CustomerDAO customerDAO = new SQLiteCustomerDAO();
        AttendantDAO attendantDAO = new SQLiteAttendantDAO();
        CategoryDAO categoryDAO = new SQLiteCategoryDAO();
        ProductDAO productDAO = new SQLiteProductDAO(categoryDAO);

        //Controller instance
        AttendantController attendantController = new AttendantController(attendantDAO);
        CustomerController customerController = new CustomerController(customerDAO);
        CategoryController categoryController = new CategoryController(categoryDAO);
        ProductController productController = new ProductController(productDAO);

        attendantController.addAttendant("Giovanni","Rossi","GVBSDB","IT44123123");
        customerController.addCustomer("Pietro","Bonechi","BNCPTR","CreditCard");

        Category firstCourse = categoryController.CreateCategory("First course");
        int idLasagna = productController.AddProduct("Lasagna","it is made of stacked layers of pasta alternating with fillings such as ragù",6.50,firstCourse,10);

        //Test decrease and increase
        productController.IncreaseProductQuantity(idLasagna,10);
        boolean decreased = productController.Decrease(idLasagna,25);
        System.out.println("Product decreased: " + decreased);
        decreased = productController.Decrease(idLasagna,15);
        System.out.println("Product decreased: " + decreased);

    }
}