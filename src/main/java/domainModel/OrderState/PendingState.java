package domainModel.OrderState;

public class PendingState implements OrderState {
    @Override
    public void nextState() {

    }

    @Override
    public String getState() {
        return "Pending";
    }
}
