package businessLogic;

import dao.DAO;
import domainModel.Customer;

public class CustomerController extends PersonController<Customer>{

    public CustomerController(DAO<Customer, String> dao) {
        super(dao);
    }

    public String addCustomer(String firstName, String lastName, String cf,String paymentMethod) throws Exception {
        Customer c = new Customer(firstName, lastName, cf,paymentMethod);
        return  super.addPerson(c);

    }
}
