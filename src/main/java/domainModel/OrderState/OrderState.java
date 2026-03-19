package domainModel.OrderState;

import domainModel.Order;

import java.util.Objects;

public abstract class OrderState {
    public abstract String getState();

    public OrderState confirm(Order order) {
        throw invalidTransition("confirm");
    }

    public OrderState startPreparation(Order order) {
        throw invalidTransition("start preparation for");
    }

    public OrderState endPreparation(Order order) {
        throw invalidTransition("end preparation for");
    }

    public OrderState collect(Order order) {
        throw invalidTransition("collect");
    }

    public boolean canAddProducts() {
        return false;
    }

    public boolean canRemoveProducts() {
        return false;
    }

    public boolean canDelete() {
        return false;
    }

    public boolean isRefundableOnDelete() {
        return false;
    }

    public abstract OrderState copy();

    protected IllegalStateException invalidTransition(String action) {
        return new IllegalStateException(
                "Cannot " + action + " an order while it is in the '" + getState() + "' state."
        );
    }

    public static OrderState fromValue(String stateValue) {
        if (Objects.equals(stateValue, "Pending")) {
            return new PendingState();
        }
        if (Objects.equals(stateValue, "Preparation")) {
            return new PreparationState();
        }
        if (Objects.equals(stateValue, "Ready")) {
            return new ReadyState();
        }
        if (Objects.equals(stateValue, "Delivered")) {
            return new DeliveredState();
        }
        return new CustomerChoosingState();
    }
}
