import businessLogic.AttendantController;
import businessLogic.CustomerController;
import dao.*;
import domainModel.Attendant;
import domainModel.Category;
import domainModel.Customer;
import domainModel.Person;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.setDatabase("main.db");
        Database.initDatabase();

        CustomerDAO customerDAO = new SQLiteCustomerDAO();
        AttendantDAO attendantDAO = new SQLiteAttendantDAO();

        AttendantController attendantController = new AttendantController(attendantDAO);
        CustomerController customerController = new CustomerController(customerDAO);


        attendantController.addAttendant("Giovanni","Rossi","GVBSDB","IT44123123");
        customerController.addCustomer("pietro","bonechi","BNCPTR","CreditCard");



    }
}