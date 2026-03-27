package businessLogic;

import dao.DAO;
import domainModel.Customer;

public class CustomerService extends PersonService<Customer> {

    /**
     * Builds a service with the given customer DAO.
     *
     * @param dao DAO used to manage customers.
     */
    public CustomerService(DAO<Customer, String> dao) {
        super(dao);
    }

    /**
     * Creates and persists a new customer, returning its fiscal code.
     *
     * @param firstName customer first name.
     * @param lastName customer last name.
     * @param cf customer fiscal code.
     * @param paymentMethod customer's payment method.
     * @return fiscal code of the created customer.
     * @throws Exception if the DAO operation fails.
     */
    public String addCustomer(String firstName, String lastName, String cf,String paymentMethod) throws Exception {
        Customer c = new Customer(firstName, lastName, cf,paymentMethod);
        return  super.addPerson(c);

    }
}
