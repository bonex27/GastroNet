import businessLogic.*;
import dao.*;
import domainModel.Category;
import domainModel.Product;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        //DAO instance
        CustomerDAO customerDAO = new SQLiteCustomerDAO();
        AttendantDAO attendantDAO = new SQLiteAttendantDAO();
        CategoryDAO categoryDAO = new SQLiteCategoryDAO();
        ProductDAO productDAO = new SQLiteProductDAO(categoryDAO);
        OrderDAO orderDAO = new SQLiteOrderDAO();

        //Controller instance
        AttendantController attendantController = new AttendantController(attendantDAO);
        CustomerController customerController = new CustomerController(customerDAO);
        CategoryController categoryController = new CategoryController(categoryDAO);
        ProductController productController = new ProductController(productDAO);
        OrderController orderController = new OrderController(orderDAO,productDAO,customerDAO);

          attendantController.addAttendant("Giovanni","Rossi","GVBSDB","IT44123123");
        String idCustomer1 =  customerController.addCustomer("Pietro","Bonechi","BNCPTR","CreditCard");

        Category firstCourse = categoryController.CreateCategory("First course");
        Category secondCourse = categoryController.CreateCategory("Second course");
        Category desserts = categoryController.CreateCategory("Desserts");
        Category Drink = categoryController.CreateCategory("Drink");


        int idLasagna = productController.AddProduct("Lasagna","it is made of stacked layers of pasta alternating with fillings such as ragù",6.50,firstCourse,10);
        productController.AddProduct("Penne al sugo","it is made of stacked layers of pasta alternating with fillings such as ragù",5.0,firstCourse,10);
        productController.AddProduct("Crostata","it is made of stacked layers of pasta alternating with fillings such as ragù",4.0,desserts,5);
        productController.AddProduct("Acqua naturale","it is made of stacked layers of pasta alternating with fillings such as ragù",2.0,Drink,100);


        List<Product> products = productDAO.getAll();
        System.out.println("Available products: " + products.size()+" :");
        for (Product product : products) {
            System.out.println(product);
        }

        productController.deleteProduct(idLasagna);

        products = productDAO.getAll();
        System.out.println("Available products: " + products.size()+" :");
        for (Product product : products) {
            System.out.println(product);
        }

        //Order creation
        int idOrder1 = orderController.createOrder(idCustomer1);

        //Test decrease and increase
        /*productController.IncreaseProductQuantity(idLasagna,10);
        boolean decreased = productController.DecreaseProductQuantity(idLasagna,25);
        System.out.println("Product decreased: " + decreased);
        decreased = productController.DecreaseProductQuantity(idLasagna,15);
        System.out.println("Product decreased: " + decreased);*/

    }
}