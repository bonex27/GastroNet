package businessLogic;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import domainModel.Order;
import domainModel.OrderState.DeliveredState;
import domainModel.OrderState.PendingState;
import domainModel.OrderState.PreparationState;
import domainModel.OrderState.ReadyState;
import domainModel.Product;

import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

public class OrderController {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;

    public OrderController(OrderDAO orderDAO, ProductDAO productDAO, CustomerDAO customerDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.customerDAO = customerDAO;
    }

    // Order Management
    public Order getOrder(int id) throws Exception {
        return orderDAO.get(id);
    }

    public List<Order> getOrders() throws Exception {
        return unmodifiableList(orderDAO.getAll());
    }

    public int createOrder(String cf_customer) throws Exception {
        Order order = new Order(orderDAO.GetNextId(), customerDAO.get(cf_customer));
        orderDAO.insert(order);
        return order.getId();
    }

    public void addProductToOrder(int idProduct, int idOrder) throws Exception {
        Product product = productDAO.get(idProduct);
        Order order = orderDAO.get(idOrder);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (product == null)
            throw new IllegalArgumentException("The given product id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing"))
            throw new RuntimeException("You can confirm an order only if is in the 'CustomerChoosing' state.");
        if (product.getStock() == 0)
            throw new IllegalArgumentException("The product is not available in the stock.");

        productDAO.DecreaseStock(product, 1);
        orderDAO.addProductToOrder(idProduct, idOrder);
    }

    public void removeProductFromOrder(int idProduct, int idOrder) throws Exception {
        Product product = productDAO.get(idProduct);
        Order order = new Order( orderDAO.get(idOrder));

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (product == null)
            throw new IllegalArgumentException("The given product id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing"))
            throw new RuntimeException("You can remove an order only if is in the 'CustomerChoosing' state.");
        if (!order.getProducts().contains(product))
            throw new IllegalArgumentException("The product is not present in the order.");

        if (orderDAO.removeProductToOrder(idProduct, idOrder)) {
            productDAO.IncreaseStock(product, 1);
        }
    }

    public void deleteOrder(int id) throws Exception {
        Order order = orderDAO.get(id);
        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing") && !Objects.equals(order.getState(), "Pending"))
            throw new RuntimeException("You can delete an order only if is in the 'CustomerChoosing' or 'Pending' state.");
        orderDAO.delete(id);
    }
    //--

    // State Moving
    public void confirmOrder(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "CustomerChoosing"))
            throw new RuntimeException("You can confirm an order only if is in the 'CustomerChoosing' state.");

        PendingState pendingState = new PendingState();
        this.orderDAO.changeState(id, pendingState);
    }

    public void startPreparation(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "Pending"))
            throw new RuntimeException("You can confirm an order only if is in the 'Pending' state.");

        PreparationState preparationState = new PreparationState();
        this.orderDAO.changeState(id, preparationState);
    }

    public void endPreparation(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "Preparation"))
            throw new RuntimeException("You can confirm an order only if is in the 'Preparation' state.");

        ReadyState readyState = new ReadyState();
        this.orderDAO.changeState(id, readyState);
    }

    public void collectOrder(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!Objects.equals(order.getState(), "Ready"))
            throw new RuntimeException("You can confirm an order only if is in the 'Ready' state.");

        DeliveredState deliveredState = new DeliveredState();
        this.orderDAO.changeState(id, deliveredState);
    }
    // --
}

/*
 * Ordine stati:
 * 1)CustomerChoosing
 * 2)Pending -> confirmOrder()
 * 3)Preparation -> startPreparation()
 * 4)Ready -> endPreparation()
 * 5)Delivered -> collectOrder()
 */