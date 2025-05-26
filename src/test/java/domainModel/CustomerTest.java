package domainModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDateTime;

public class CustomerTest {
    public void CreatingCustomerWithEmptyName() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Customer("", "Bonechi", "",""),
                "Expected constructor to throw, but it didn't"
        );

    }
}
