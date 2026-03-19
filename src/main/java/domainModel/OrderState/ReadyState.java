package domainModel.OrderState;

public class ReadyState extends OrderState {
    @Override
    public String getState() {
        return "Ready";
    }

    @Override
    public OrderState collect(domainModel.Order order) {
        return new DeliveredState();
    }

    @Override
    public boolean canDelete() {
        return true;
    }

    @Override
    public OrderState copy() {
        return new ReadyState();
    }
}
