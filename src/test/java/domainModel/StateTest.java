package domainModel;

import domainModel.OrderState.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class StateTest {
    private OrderState orderState;

    private Order buildOrder() {
        return new Order(1, new Customer("Mario", "Rossi", "RSSMRA80A01H501X", "Cash"));
    }

    @Test
    public void testCustomerChoosingState() {
        orderState = new CustomerChoosingState();
        Assertions.assertEquals("CustomerChoosing",orderState.getState());
        Assertions.assertTrue(orderState.canAddProducts());
        Assertions.assertTrue(orderState.canRemoveProducts());
        Assertions.assertTrue(orderState.canDelete());
        Assertions.assertTrue(orderState.isRefundableOnDelete());
        Assertions.assertEquals("Pending", orderState.confirm(buildOrder()).getState());
        Assertions.assertThrows(IllegalStateException.class, () -> orderState.startPreparation(buildOrder()));
    }

    @Test
    public void testPendingState() {
        orderState = new PendingState();
        Assertions.assertEquals("Pending",orderState.getState());
        Assertions.assertFalse(orderState.canAddProducts());
        Assertions.assertFalse(orderState.canRemoveProducts());
        Assertions.assertTrue(orderState.canDelete());
        Assertions.assertTrue(orderState.isRefundableOnDelete());
        Assertions.assertEquals("Preparation", orderState.startPreparation(buildOrder()).getState());
        Assertions.assertThrows(IllegalStateException.class, () -> orderState.confirm(buildOrder()));
    }

    @Test
    public void testPreparationState() {
        orderState = new PreparationState();
        Assertions.assertEquals("Preparation",orderState.getState());
        Assertions.assertFalse(orderState.canAddProducts());
        Assertions.assertFalse(orderState.canRemoveProducts());
        Assertions.assertTrue(orderState.canDelete());
        Assertions.assertFalse(orderState.isRefundableOnDelete());
        Assertions.assertEquals("Ready", orderState.endPreparation(buildOrder()).getState());
        Assertions.assertThrows(IllegalStateException.class, () -> orderState.collect(buildOrder()));
    }

    @Test
    public void testReadyState() {
        orderState = new ReadyState();
        Assertions.assertEquals("Ready",orderState.getState());
        Assertions.assertFalse(orderState.canAddProducts());
        Assertions.assertFalse(orderState.canRemoveProducts());
        Assertions.assertTrue(orderState.canDelete());
        Assertions.assertFalse(orderState.isRefundableOnDelete());
        Assertions.assertEquals("Delivered", orderState.collect(buildOrder()).getState());
        Assertions.assertThrows(IllegalStateException.class, () -> orderState.endPreparation(buildOrder()));
    }

    @Test
    public void testDeliveredState() {
        orderState = new DeliveredState();
        Assertions.assertEquals("Delivered",orderState.getState());
        Assertions.assertFalse(orderState.canAddProducts());
        Assertions.assertFalse(orderState.canRemoveProducts());
        Assertions.assertFalse(orderState.canDelete());
        Assertions.assertFalse(orderState.isRefundableOnDelete());
        Assertions.assertThrows(IllegalStateException.class, () -> orderState.collect(buildOrder()));
    }
}
