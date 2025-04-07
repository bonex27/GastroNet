import businessLogic.*;
import dao.*;
import domainModel.Category;
import domainModel.Order;
import domainModel.Product;
import domainModel.Search.DecoratorSearchPrice;
import domainModel.Search.DecoratorSearchStock;
import domainModel.Search.SearchConcrete;

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
        OrderDAO orderDAO = new SQLiteOrderDAO(customerDAO);

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
        Category drink = categoryController.CreateCategory("Drink");
        Category fruit = categoryController.CreateCategory("Fruit");


        int idLasagna = productController.AddProduct("Lasagna", "it is made of stacked layers of pasta alternating with fillings such as ragù", 6.50, firstCourse, 10);
        productController.AddProduct("Penne al sugo", "it is made of stacked layers of pasta alternating with fillings such as ragù", 5.0, firstCourse, 10);
        productController.AddProduct("Crostata", "it is made of stacked layers of pasta alternating with fillings such as ragù", 4.0, desserts, 5);
        productController.AddProduct("Acqua naturale", "it is made of stacked layers of pasta alternating with fillings such as ragù", 2.0, drink, 100);
        productController.AddProduct("Acqua frizzante", "it is made of stacked layers of pasta alternating with fillings such as ragù", 2.0, drink, 0);
        productController.AddProduct("Mele", "it is made of stacked layers of pasta alternating with fillings such as ragù", 3.0, fruit, 0);
        productController.AddProduct("Pere", "it is made of stacked layers of pasta alternating with fillings such as ragù", 3.0, fruit, 30);
        productController.AddProduct("Banane", "it is made of stacked layers of pasta alternating with fillings such as ragù", 3.0, fruit, 200);


        List<Product> products = productDAO.getAll();
        System.out.println("Available products: " + products.size()+" :");
        for (Product product : products) {
            System.out.println(product);
        }

        //productController.deleteProduct(idLasagna);

        products = productDAO.getAll();
        System.out.println("Available products: " + products.size() + " :");
        for (Product product : products) {
            System.out.println(product);
        }



        //Order creation
        int idOrder1 = orderController.createOrder(idCustomer1);
        if(orderController.addProductToOrder(idOrder1,idOrder1))
        {
            System.out.println("Product added successfully");
        }
        else
        {
            System.out.println("Product not added");
        }

//        if(orderController.removeProductFromOrder(idOrder1,idOrder1))
//        {
//            System.out.println("Product removed successfully");
//        }
//        else
//        {
//            System.out.println("Product not removed");
//        }
        Order order = orderDAO.get(idOrder1);
        System.out.println("\nOrder List:");
        for (Product p : order.getProducts()) {
            System.out.println("-" + p.toString());
        }


        //Test decrease and increase
        /*productController.IncreaseProductQuantity(idLasagna,10);
        boolean decreased = productController.DecreaseProductQuantity(idLasagna,25);
        System.out.println("Product decreased: " + decreased);
        decreased = productController.DecreaseProductQuantity(idLasagna,15);
        System.out.println("Product decreased: " + decreased);*/


        // Search with Decorator
        System.out.println("\nDECORATOR:");
        System.out.println("\nSearching for products in stock with a price range of [2.00 , 5.00] €.\nQuery generated:");
        List<Product> productsFound = productController.Search(
                new DecoratorSearchPrice(
                        new DecoratorSearchStock(
                                new SearchConcrete(),
                                true
                        ),
                        2,
                        5
                )
        );

        System.out.println("\nResults:");
        for (Product p : productsFound) {
            System.out.println("-" + p.toString());
        }

        System.out.println("\nSearching for all not available products.\nQuery generated:");
        List<Product> productsFound2 = productController.Search(
                new DecoratorSearchStock(
                        new SearchConcrete(),
                        false
                )
        );
        System.out.println("\nResults:");
        for (Product p : productsFound2) {
            System.out.println("-" + p.toString());
        }
    }
}