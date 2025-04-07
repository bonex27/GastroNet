package domainModel.OrderState;

public abstract class OrderState {
    private String state;

    public String getState()
    {
        return state;
    }

    public void nextState()
    {

    }
}
/**
 * Ordine stati:
 * 1)CustomerChoosing
 * 2)Pending
 * 3)Preparation
 * 4)Ready
 * 5)Delivered
 */