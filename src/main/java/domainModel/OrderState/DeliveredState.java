package domainModel.OrderState;

public class DeliveredState implements OrderState {
    @Override
    public void nextState() {

    }

    @Override
    public String getState() {
        return "Delivered";
    }
}
