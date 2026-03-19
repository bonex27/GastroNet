package domainModel.OrderState;

public class DeliveredState extends OrderState {
    @Override
    public String getState() {
        return "Delivered";
    }

    @Override
    public OrderState copy() {
        return new DeliveredState();
    }
}
