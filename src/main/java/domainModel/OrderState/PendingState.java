package domainModel.OrderState;

public class PendingState extends OrderState {
    @Override
    public String getState() {
        return "Pending";
    }

    @Override
    public OrderState startPreparation(domainModel.Order order) {
        return new PreparationState();
    }

    @Override
    public boolean canDelete() {
        return true;
    }

    @Override
    public boolean isRefundableOnDelete() {
        return true;
    }

    @Override
    public OrderState copy() {
        return new PendingState();
    }
}
