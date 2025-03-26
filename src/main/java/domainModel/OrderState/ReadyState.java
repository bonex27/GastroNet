package domainModel.OrderState;

public class ReadyState implements OrderState {
    @Override
    public void nextState() {

    }

    @Override
    public String getState() {
        return "Ready for collection";
    }
}
