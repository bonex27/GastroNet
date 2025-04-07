package businessLogic;

import dao.OrderDAO;
import domainModel.Order;
import domainModel.OrderState.DeliveredState;
import domainModel.OrderState.PendingState;
import domainModel.OrderState.PreparationState;
import domainModel.OrderState.ReadyState;

import java.util.Objects;

public class StateController {

    private final OrderController orderController;
    private final OrderDAO orderDAO;

    public StateController(OrderController orderController, OrderDAO orderDAO) {
        this.orderController = orderController;
        this.orderDAO = orderDAO;
    }

    public void addProductToOrder(int id_product, int id_order) throws Exception {
        Order order = orderController.getOrder(id_order);
        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing"))
            throw new RuntimeException("You can confirm an order only if is in the 'CustomerChoosing' state.");
        else
            orderController.addProductToOrder(id_product, id_order);
    }

    public void confirmOrder(int id) throws Exception {
        Order order = orderController.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing"))
            throw new RuntimeException("You can confirm an order only if is in the 'CustomerChoosing' state.");

        PendingState pendingState = new PendingState();
        this.orderDAO.changeState(id, pendingState);
    }

    public void startPreparation(int id) throws Exception {
        Order order = orderController.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "Pending"))
            throw new RuntimeException("You can confirm an order only if is in the 'Pending' state.");

        PreparationState preparationState = new PreparationState();
        this.orderDAO.changeState(id, preparationState);
    }

    public void endPreparation(int id) throws Exception {
        Order order = orderController.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "Preparation"))
            throw new RuntimeException("You can confirm an order only if is in the 'Preparation' state.");

        ReadyState readyState = new ReadyState();
        this.orderDAO.changeState(id, readyState);
    }

    public void collectOrder(int id) throws Exception {
        Order order = orderController.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "Ready"))
            throw new RuntimeException("You can confirm an order only if is in the 'Ready' state.");

        DeliveredState deliveredState = new DeliveredState();
        this.orderDAO.changeState(id, deliveredState);
    }

    public void deleteOrder(int id) throws Exception {
        Order order = orderController.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing") || !Objects.equals(order.getState(), "Pending"))
            throw new RuntimeException("You can confirm an order only if is in the 'CustomerChoosing' state.");
        else
            orderDAO.delete(id);
    }
}

/*
 * Ordine stati:
 * 1)CustomerChoosing
 * 2)Pending -> confirmOrder()
 * 3)Preparation -> startPreparation()
 * 4)Ready -> endPreparation()
 * 5)Delivered -> collectOrder()
 */