package domainModel;

import domainModel.OrderState.CustomerChoosingState;
import domainModel.OrderState.OrderState;
import domainModel.OrderState.PendingState;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private OrderState state;
    private Customer customer;
    private List<Product> products;

    public Order(int id, Customer customer) {
        this.id = id;
        this.customer = customer;
        this.products = new ArrayList<Product>();
        this.state = new CustomerChoosingState();
    }

    public Order(int id, Customer customer, OrderState state) {
        this.id = id;
        this.customer = customer;
        this.products = new ArrayList<Product>();
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getCustomerId() {
       return customer.getCf();
    }
    public String getState() {
        return state.getState();
    }
    public void AddProduct(Product product) {
        this.products.add(product);
    }
    public void RemoveProduct(Product product) {

    }
}
