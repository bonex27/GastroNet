package domainModel;

public class Customer extends Person {
    private String paymentMethod;

    public Customer(String firstName, String lastName, String cf, String paymentMethod) {
        super(firstName, lastName, cf);
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
