import businessLogic.*;
import dao.*;
import domainModel.Category;
import domainModel.Order;
import domainModel.Product;
import dao.query.DecoratorSearchPrice;
import dao.query.DecoratorSearchStock;
import dao.query.SearchConcrete;

import java.util.List;

public class Main {
    public static final String RED_MESSAGE = "\033[0;31m";     // Red ansi for manage errore printout
    public static void main(String[] args) throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        //DAO instance
        CustomerDAO customerDAO = new SQLiteCustomerDAO();
        AttendantDAO attendantDAO = new SQLiteAttendantDAO();
        CategoryDAO categoryDAO = new SQLiteCategoryDAO();
        ProductDAO productDAO = new SQLiteProductDAO(categoryDAO);
        OrderDAO orderDAO = new SQLiteOrderDAO(customerDAO, productDAO);

        // Service instances
        AttendantService attendantService = new AttendantService(attendantDAO);
        CustomerService customerService = new CustomerService(customerDAO);
        CategoryService categoryService = new CategoryService(categoryDAO);
        ProductService productService = new ProductService(productDAO);
        OrderService orderService = new OrderService(orderDAO, productDAO, customerDAO);

        attendantService.addAttendant("Giovanni", "Rossi", "GVBSDB", "IT44123123");
        String idCustomer1 = customerService.addCustomer("Pietro", "Bonechi", "BNCPTR", "CreditCard");
        String idCustomer2 = customerService.addCustomer("Andrea", "Petrucci", "PTRNDR", "Cash");

        Category firstCourse = categoryService.CreateCategory("First course");
        Category secondCourse = categoryService.CreateCategory("Second course");
        Category desserts = categoryService.CreateCategory("Desserts");
        Category drink = categoryService.CreateCategory("Drink");
        Category fruit = categoryService.CreateCategory("Fruit");


        int idProduct1 = productService.AddProduct("Lasagna", "it is made of stacked layers of pasta alternating with fillings such as ragù", 6.50, firstCourse, 10);
        int idProduct2 = productService.AddProduct("Penne al sugo", "it is made of stacked layers of pasta alternating with fillings such as ragù", 5.0, firstCourse, 10);
        int idProduct3 = productService.AddProduct("Crostata", "it is made of stacked layers of pasta alternating with fillings such as ragù", 4.0, desserts, 5);
        int idProduct4 = productService.AddProduct("Acqua naturale", "it is made of stacked layers of pasta alternating with fillings such as ragù", 2.0, drink, 100);
        int idProduct5 = productService.AddProduct("Acqua frizzante", "it is made of stacked layers of pasta alternating with fillings such as ragù", 2.0, drink, 10);
        int idProduct6 = productService.AddProduct("Mele", "it is made of stacked layers of pasta alternating with fillings such as ragù", 3.0, fruit, 0);
        int idProduct7 = productService.AddProduct("Pere", "it is made of stacked layers of pasta alternating with fillings such as ragù", 3.0, fruit, 30);
        int idProduct8 = productService.AddProduct("Banane", "it is made of stacked layers of pasta alternating with fillings such as ragù", 3.0, fruit, 200);

        /*
        List<Product> products = productDAO.getAll();
        System.out.println("Available products: " + products.size() + " :");
        for (Product product : products) {
            System.out.println(product);
        }

        //productService.deleteProduct(idProduct1);

        products = productDAO.getAll();
        System.out.println("Available products: " + products.size() + " :");
        for (Product product : products) {
            System.out.println(product);
        }
        */

        //Order1 creation
        int idOrder1 = orderService.createOrder(idCustomer1);
        orderService.addProductToOrder(idProduct1, idOrder1);
        orderService.addProductToOrder(idProduct3, idOrder1);
        orderService.addProductToOrder(idProduct5, idOrder1);

        //Order2 creation
        int idOrder2 = orderService.createOrder(idCustomer2);
        orderService.addProductToOrder(idProduct2, idOrder2);
        orderService.addProductToOrder(idProduct3, idOrder2);
        orderService.addProductToOrder(idProduct4, idOrder2);

//        if(orderService.removeProductFromOrder(idOrder1,idOrder1))
//        {
//            System.out.println("Product removed successfully");
//        }
//        else
//        {
//            System.out.println("Product not removed");
//        }

        //Test decrease and increase
        productService.IncreaseProductQuantity(idProduct1,10);
        boolean decreased = productService.DecreaseProductQuantity(idProduct1,25);
        System.out.println("Product decreased: " + decreased);
        decreased = productService.DecreaseProductQuantity(idProduct1,15);
        System.out.println("Product decreased: " + decreased);


        // Search with Decorator
        System.out.println("\nDECORATOR:");
        System.out.println("\nSearching for products in stock with a price range of [2.00 , 5.00] €.\nQuery generated:");
        List<Product> productsFound = productService.Search(
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
        List<Product> productsFound2 = productService.Search(
                new DecoratorSearchStock(
                        new SearchConcrete(),
                        false
                )
        );
        System.out.println("\nResults:");
        for (Product p : productsFound2) {
            System.out.println("-" + p.toString());
        }


        // Test get orders from db
        System.out.println("\nAll Orders:");
        List<Order> allOrders = orderService.getOrders();
        for (Order o : allOrders) {
            System.out.println(o.toString());
        }

        try {
            orderService.addProductToOrder(idProduct6, idOrder1);
        } catch (Exception e) {
            System.out.println(RED_MESSAGE + "Error: " + e.getMessage());
        }

        orderService.confirmOrder(idOrder1);
        System.out.println("\nOrder confirmed:");

        // rimozione prodotto da ordine
        try {
            orderService.removeProductFromOrder(idProduct6, idOrder1);
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        orderService.confirmOrder(idOrder2);

        System.out.println("\nAll Orders:");
        allOrders = orderService.getOrders();
        for (Order o : allOrders) {
            System.out.println(o.toString());
        }


        System.out.println("\nOrder User 1:");
        allOrders = orderService.getOrders(idCustomer1);
        for (Order o : allOrders) {
            System.out.println(o.toString());
        }
    }
}
