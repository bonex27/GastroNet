package domainModel.OrderState;

public class PreparationState implements OrderState {
    @Override
    public void nextState() {

    }

    @Override
    public String getState() {
        return "Preparation";
    }
}
