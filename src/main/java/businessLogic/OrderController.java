package businessLogic;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import domainModel.CancelResult;
import domainModel.Order;
import domainModel.Product;

import java.util.List;

import static java.util.Collections.unmodifiableList;

public class OrderController {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;

    /**
     * Builds a controller with the given DAOs.
     *
     * @param orderDAO DAO used to manage orders.
     * @param productDAO DAO used to manage products.
     * @param customerDAO DAO used to manage customers.
     */
    public OrderController(OrderDAO orderDAO, ProductDAO productDAO, CustomerDAO customerDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.customerDAO = customerDAO;
    }

    // Order Management
    /**
     * Fetches a single order by id.
     *
     * @param id order id.
     * @return order instance if found.
     * @throws Exception if the DAO operation fails.
     */
    public Order getOrder(int id) throws Exception {
        return orderDAO.get(id);
    }

    /**
     * Returns all orders as an unmodifiable list.
     *
     * @return list of all orders.
     * @throws Exception if the DAO operation fails.
     */
    public List<Order> getOrders() throws Exception {
        return unmodifiableList(orderDAO.getAll());
    }

    /**
     * Returns orders for a specific customer as an unmodifiable list.
     *
     * @param idCustomer customer fiscal code.
     * @return list of orders for the customer.
     * @throws Exception if the DAO operation fails.
     */
    public List<Order> getOrders(String idCustomer) throws Exception {
        return unmodifiableList(orderDAO.getByUser(idCustomer));
    }

    /**
     * Creates a new order for the given customer and returns its id.
     *
     * @param cf_customer customer fiscal code.
     * @return id of the created order.
     * @throws Exception if the DAO operation fails.
     */
    public int createOrder(String cf_customer) throws Exception {
        var customer = customerDAO.get(cf_customer);
        if (customer == null) {
            throw new IllegalArgumentException("The given customer id does not exist.");
        }
        Order order = new Order(orderDAO.GetNextId(), customer);
        orderDAO.insert(order);
        return order.getId();
    }

    /**
     * Adds a product to an order after validating availability and state.
     *
     * @param idProduct product id.
     * @param idOrder order id.
     * @throws Exception if the DAO operation fails.
     */
    public void addProductToOrder(int idProduct, int idOrder) throws Exception {
        Product product = productDAO.get(idProduct);
        Order order = orderDAO.get(idOrder);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (product == null)
            throw new IllegalArgumentException("The given product id does not exist.");
        if (!order.canAddProducts())
            throw new IllegalStateException("You can add products only when the order allows product changes.");
        if (product.getStock() <= 0)
            throw new IllegalArgumentException("The product is not available in the stock.");

        if (!productDAO.DecreaseStock(product, 1)) {
            throw new IllegalArgumentException("The product is not available in the stock.");
        }
        try {
            orderDAO.addProductToOrder(idProduct, idOrder);
        } catch (Exception e) {
            productDAO.IncreaseStock(product, 1);
            throw e;
        }
    }

    /**
     * Removes a product from an order and restores stock if removed.
     *
     * @param idProduct product id.
     * @param idOrder order id.
     * @throws Exception if the DAO operation fails.
     */
    public void removeProductFromOrder(int idProduct, int idOrder) throws Exception {
        Product product = productDAO.get(idProduct);
        Order existingOrder = orderDAO.get(idOrder);

        if (existingOrder == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (product == null)
            throw new IllegalArgumentException("The given product id does not exist.");
        if (!existingOrder.canRemoveProducts())
            throw new IllegalStateException("You can remove products only when the order allows product changes.");
        if (!existingOrder.getProducts().contains(product))
            throw new IllegalArgumentException("The product is not present in the order.");

        if (orderDAO.removeProductToOrder(idProduct, idOrder)) {
            productDAO.IncreaseStock(product, 1);
        }
    }

    /**
     * Deletes an order if it is in a deletable state.
     *
     * @param id order id.
     * @throws Exception if the DAO operation fails.
     */
    public CancelResult deleteOrder(int id) throws Exception {
        Order order = orderDAO.get(id);
        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        if (!order.canDelete())
            throw new IllegalStateException("You can delete an order only if its current state allows cancellation.");

        return new CancelResult(
                orderDAO.delete(id),
                order.isRefundableOnDelete()
        );
    }

    /**
     * Confirms an order and moves it to the pending state.
     *
     * @param id order id.
     * @throws Exception if the DAO operation fails.
     */
    public void confirmOrder(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        order.confirm();
        this.orderDAO.changeState(id, order.getOrderState());
    }

    /**
     * Moves a pending order to the preparation state.
     *
     * @param id order id.
     * @throws Exception if the DAO operation fails.
     */
    public void startPreparation(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        order.startPreparation();
        this.orderDAO.changeState(id, order.getOrderState());
    }

    /**
     * Moves a preparation order to the ready state.
     *
     * @param id order id.
     * @throws Exception if the DAO operation fails.
     */
    public void endPreparation(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        order.endPreparation();
        this.orderDAO.changeState(id, order.getOrderState());
    }

    /**
     * Moves a ready order to the delivered state.
     *
     * @param id order id.
     * @throws Exception if the DAO operation fails.
     */
    public void collectOrder(int id) throws Exception {
        Order order = this.getOrder(id);

        if (order == null)
            throw new IllegalArgumentException("The given order id does not exist.");
        order.collect();
        this.orderDAO.changeState(id, order.getOrderState());
    }
}
