package domainModel.OrderState;

public class CustomerChoosingState extends OrderState {
    @Override
    public String getState() {
        return "CustomerChoosing";
    }

    @Override
    public OrderState confirm(domainModel.Order order) {
        return new PendingState();
    }

    @Override
    public boolean canAddProducts() {
        return true;
    }

    @Override
    public boolean canRemoveProducts() {
        return true;
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
        return new CustomerChoosingState();
    }
}
