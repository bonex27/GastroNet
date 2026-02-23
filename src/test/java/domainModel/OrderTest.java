package domainModel;

import domainModel.OrderState.PendingState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    @Test
    public void constructorSetsCustomerAndInitialState() {
        Customer customer = new Customer("Luca", "Verdi", "CF001", "cash");
        Order order = new Order(10, customer);

        Assertions.assertEquals(10, order.getId());
        Assertions.assertEquals("CF001", order.getCustomerId());
        Assertions.assertEquals("CustomerChoosing", order.getState());
    }

    @Test
    public void copyConstructorDeepCopiesProductsAndState() {
        Customer customer = new Customer("Anna", "Neri", "CF777", "card");
        Category category = new Category("Food");
        Product product = new Product(1, "Pizza", "Margherita", 10.0, 2, category);
        List<Product> products = new ArrayList<>();
        products.add(product);

        Order original = new Order(20, customer, new PendingState(), products);
        Order copy = new Order(original);

        Assertions.assertEquals(original.getId(), copy.getId());
        Assertions.assertEquals(original.getCustomerId(), copy.getCustomerId());
        Assertions.assertEquals("Pending", copy.getState());
        Assertions.assertNotSame(original.getProducts(), copy.getProducts());
        Assertions.assertEquals(1, copy.getProducts().size());
        Assertions.assertNotSame(original.getProducts().getFirst(), copy.getProducts().getFirst());
        Assertions.assertEquals(original.getProducts().getFirst(), copy.getProducts().getFirst());
    }

    @Test
    public void toStringIncludesOrderInfo() {
        Customer customer = new Customer("Marco", "Blu", "CF555", "cash");
        Category category = new Category("Dessert");
        Product product = new Product(2, "Cake", "Chocolate", 4.5, 1, category);
        List<Product> products = List.of(product);

        Order order = new Order(30, customer, new PendingState(), products);
        String output = order.toString();

        Assertions.assertTrue(output.contains("#30"));
        Assertions.assertTrue(output.contains("Marco Blu"));
        Assertions.assertTrue(output.contains("Pending"));
        Assertions.assertTrue(output.contains("Cake"));
    }
}
