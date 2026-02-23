package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProductTest {

    @Test
    public void constructorAndGetters() {
        Category category = new Category("Food");
        Product product = new Product(1, "Pizza", "Margherita", 10.5, 3, category);

        Assertions.assertEquals(1, product.getId());
        Assertions.assertEquals("Pizza", product.getName());
        Assertions.assertEquals("Margherita", product.getDescription());
        Assertions.assertEquals(10.5, product.getPrice());
        Assertions.assertEquals(3, product.getStock());
        Assertions.assertEquals(category, product.getCategory());
    }

    @Test
    public void copyConstructorCopiesFields() {
        Category category = new Category("Food");
        Product original = new Product(2, "Pasta", "Carbonara", 8.0, 5, category);
        Product copy = new Product(original);

        Assertions.assertEquals(original.getId(), copy.getId());
        Assertions.assertEquals(original.getName(), copy.getName());
        Assertions.assertEquals(original.getDescription(), copy.getDescription());
        Assertions.assertEquals(original.getPrice(), copy.getPrice());
        Assertions.assertEquals(original.getStock(), copy.getStock());
        Assertions.assertEquals(original.getCategory(), copy.getCategory());
    }

    @Test
    public void equalsUsesIdOnly() {
        Category category = new Category("Food");
        Product first = new Product(3, "Salad", "Green", 4.0, 1, category);
        Product sameIdDifferentData = new Product(3, "Soup", "Hot", 6.0, 10, category);
        Product differentId = new Product(4, "Salad", "Green", 4.0, 1, category);

        Assertions.assertEquals(first, sameIdDifferentData);
        Assertions.assertNotEquals(first, differentId);
    }

    @Test
    public void toStringIncludesPriceStockAndCategory() {
        Category category = new Category("Food");
        Product product = new Product(5, "Burger", "Beef", 12.0, 7, category);

        Assertions.assertEquals("Burger 12.0€ [7 available] (Food)", product.toString());
    }
}
