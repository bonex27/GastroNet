package businessLogic;

import dao.CustomerDAO;
import dao.OrderDAO;
import dao.ProductDAO;
import domainModel.Customer;
import domainModel.Order;
import domainModel.Product;

import java.util.List;

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

    public int createOrder(String cf_customer) throws Exception {
        Order order = new Order(orderDAO.GetNextId(), customerDAO.get(cf_customer));
        orderDAO.insert(order);
        return order.getId();
    }

    public boolean addProductToOrder(int idProduct, int idOrder) throws Exception {
        Product product = productDAO.get(idProduct);
        //TODO metterci get order appena pronta!
        Order order = orderDAO.get(idOrder);

        if (product.getStock() > 0 && product != null && order != null) {
            orderDAO.addProductToOrder(idProduct, idOrder);
            productDAO.DecreaseStock(product, 1);
            return true;
        } else {
            return false;
        }

    }

    public boolean removeProductFromOrder(int idProduct, int idOrder) throws Exception {
        Product product = productDAO.get(idProduct);
        Order order = new Order(idOrder, null);
        if (product.getStock() > 0 && product != null && order != null) {
            if (orderDAO.removeProductToOrder(idProduct, idOrder)) {
                productDAO.IncreaseStock(product, 1);
                return true;
            }
        }
        return false;
    }

    public List<Order> getOrders() throws Exception {
        return unmodifiableList(orderDAO.getAll());
    }
}
