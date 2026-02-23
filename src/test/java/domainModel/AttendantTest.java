package domainModel;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AttendantTest {

    @Test
    public void constructorAndGetters() {
        Attendant attendant = new Attendant("Mario", "Rossi", "CF123", "IT00IBAN");

        Assertions.assertEquals("Mario", attendant.getFirstName());
        Assertions.assertEquals("Rossi", attendant.getLastName());
        Assertions.assertEquals("CF123", attendant.getCf());
        Assertions.assertEquals("IT00IBAN", attendant.getIban());
    }

    @Test
    public void copyConstructorCopiesFields() {
        Attendant original = new Attendant("Giulia", "Bianchi", "CF999", "IT99IBAN");
        Attendant copy = new Attendant(original);

        Assertions.assertEquals(original.getFirstName(), copy.getFirstName());
        Assertions.assertEquals(original.getLastName(), copy.getLastName());
        Assertions.assertEquals(original.getCf(), copy.getCf());
        Assertions.assertEquals(original.getIban(), copy.getIban());
    }
}
