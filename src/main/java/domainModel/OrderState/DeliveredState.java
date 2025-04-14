package domainModel.OrderState;

public class DeliveredState extends OrderState {
    @Override
    public String getState() {
        return "Delivered";
    }
}
