package businessLogic;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import domainModel.Order;

public class OrderController {

    private final OrderDAO orderDAO;
    private final ProductDAO productDAO;
    private final CustomerDAO customerDAO;


    public OrderController(OrderDAO orderDAO, ProductDAO productDAO, CustomerDAO customerDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.customerDAO = customerDAO;
    }

    public int createOrder(String cf_customer) throws Exception {
        Order order = new Order(orderDAO.GetNextId(),customerDAO.get(cf_customer));
        orderDAO.insert(order);
        return order.getId();
    }
}
