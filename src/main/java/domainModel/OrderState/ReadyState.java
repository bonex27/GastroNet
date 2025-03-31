package domainModel.OrderState;

public class ReadyState extends OrderState {
    @Override
    public void nextState() {

    }

    @Override
    public String getState() {
        return "Ready for collection";
    }
}
