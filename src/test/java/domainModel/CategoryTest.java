package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void constructorAndGetters() {
        Category category = new Category("Drinks");

        Assertions.assertEquals("Drinks", category.getDescription());
    }

    @Test
    public void copyConstructorCopiesDescription() {
        Category original = new Category("Food");
        Category copy = new Category(original);

        Assertions.assertEquals("Food", copy.getDescription());
    }

    @Test
    public void equalsAndHashCodeDependOnDescription() {
        Category first = new Category("Dessert");
        Category second = new Category("Dessert");
        Category different = new Category("Main");

        Assertions.assertEquals(first, second);
        Assertions.assertEquals(first.hashCode(), second.hashCode());
        Assertions.assertNotEquals(first, different);
    }

    @Test
    public void setDescriptionUpdatesValue() {
        Category category = new Category("Old");
        category.setDescription("New");

        Assertions.assertEquals("New", category.getDescription());
    }
}
