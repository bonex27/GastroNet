package domainModel;

import domainModel.OrderState.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

public class StateTest {
    private OrderState orderState;

    @Test
    public void testCustomerChoosingState() {

        orderState = new CustomerChoosingState();
        Assertions.assertEquals("CustomerChoosing",orderState.getState());
    }
    @Test
    public void testPendingState() {
        orderState = new PendingState();
        Assertions.assertEquals("Pending",orderState.getState());
    }
    @Test
    public void testPreparationState() {
        orderState = new PreparationState();
        Assertions.assertEquals("Preparation",orderState.getState());
    }
    @Test
    public void testReadyState() {
        orderState = new ReadyState();
        Assertions.assertEquals("Ready",orderState.getState());
    }
    @Test
    public void testDeliveredState() {
        orderState = new DeliveredState();
        Assertions.assertEquals("Delivered",orderState.getState());
    }


}
