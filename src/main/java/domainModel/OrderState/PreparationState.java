package domainModel.OrderState;

public class PreparationState extends OrderState {
    @Override
    public String getState() {
        return "Preparation";
    }

    @Override
    public OrderState endPreparation(domainModel.Order order) {
        return new ReadyState();
    }

    @Override
    public boolean canDelete() {
        return true;
    }

    @Override
    public OrderState copy() {
        return new PreparationState();
    }
}
