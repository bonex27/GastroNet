package domainModel.OrderState;

public class ReadyState extends OrderState {
    @Override
    public String getState() {
        return "Ready for collection";
    }
}
