package domainModel.OrderState;

public class DeliveredState extends OrderState {
    @Override
    public void nextState() {

    }

    @Override
    public String getState() {
        return "Delivered";
    }
}
