package domainModel.OrderState;

public class PendingState extends OrderState {
    @Override
    public String getState() {
        return "Pending";
    }
}
