package domainModel;

import domainModel.OrderState.*;

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
        this.products = new ArrayList<>();
        this.state = new CustomerChoosingState();
    }

    public Order(int id, Customer customer, OrderState state, List<Product> products) {
        this.id = id;
        this.customer = customer;
        this.products = products == null ? new ArrayList<>() : new ArrayList<>(products);
        this.state = state == null ? new CustomerChoosingState() : state;
    }

    public Order(Order order) {
        this.id = order.getId();
        this.customer = new Customer(order.customer);
        this.products = new ArrayList<>();

        for (Product i : order.products) {
            this.products.add(new Product(i));
        }
        this.state = order.state.copy();
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

    public OrderState getOrderState() {
        return state;
    }

    public void confirm() {
        changeState(state.confirm(this));
    }

    public void startPreparation() {
        changeState(state.startPreparation(this));
    }

    public void endPreparation() {
        changeState(state.endPreparation(this));
    }

    public void collect() {
        changeState(state.collect(this));
    }

    public boolean canAddProducts() {
        return state.canAddProducts();
    }

    public boolean canRemoveProducts() {
        return state.canRemoveProducts();
    }

    public boolean canDelete() {
        return state.canDelete();
    }

    public boolean isRefundableOnDelete() {
        return state.isRefundableOnDelete();
    }

    public List<Product> getProducts() {
        return products;
    }

    private void changeState(OrderState newState) {
        this.state = Objects.requireNonNull(newState);
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
        return id == order.id
                && Objects.equals(getState(), order.getState())
                && Objects.equals(customer, order.customer)
                && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getState(), customer, products);
    }
}
