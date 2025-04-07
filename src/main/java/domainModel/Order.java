package domainModel;

import domainModel.OrderState.CustomerChoosingState;
import domainModel.OrderState.OrderState;
import domainModel.OrderState.PendingState;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Order(int id, Customer customer, OrderState state, List<Product> products) {
        this.id = id;
        this.customer = customer;
        this.products = products;
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

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        String strOut = "#" + id
                + " by " + customer.firstName + " " + customer.lastName
                + " (" + state.getState() + "):";
        for (Product p : products) {
            strOut = strOut + "\n\t-" + p.getName();
        }
        return strOut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id && Objects.equals(state, order.state) && Objects.equals(customer, order.customer) && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, customer, products);
    }
}
