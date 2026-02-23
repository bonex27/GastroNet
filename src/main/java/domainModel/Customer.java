package domainModel;

import java.util.Objects;

public class Customer extends Person {
    private String paymentMethod;

    public Customer(String firstName, String lastName, String cf, String paymentMethod) {
        super(firstName, lastName, cf);
        this.paymentMethod = paymentMethod;
    }
    public Customer(Customer c) {
        super(c.firstName, c.lastName, c.cf);
        this.paymentMethod = c.paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getCf(), customer.getCf())
                && Objects.equals(getFirstName(), customer.getFirstName())
                && Objects.equals(getLastName(), customer.getLastName())
                && Objects.equals(paymentMethod, customer.paymentMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCf(), getFirstName(), getLastName(), paymentMethod);
    }
}
