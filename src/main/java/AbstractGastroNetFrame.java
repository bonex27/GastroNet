import domainModel.Order;
import domainModel.Product;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

abstract class AbstractGastroNetFrame extends JFrame {
    protected final GastroNetContext context;
    protected final JTextArea logArea = new JTextArea(8, 80);

    protected AbstractGastroNetFrame(String title, GastroNetContext context) {
        super(title);
        this.context = context;
        configureFrame();
    }

    private void configureFrame() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(960, 640));
        setLocationByPlatform(true);

        logArea.setEditable(false);
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);
    }

    protected JPanel wrapWithLog(JComponent content) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(content, BorderLayout.CENTER);
        root.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        return root;
    }

    protected void refreshCustomers(JComboBox<String> comboBox) throws Exception {
        comboBox.removeAllItems();
        for (var customer : context.customerDAO.getAll()) {
            comboBox.addItem(customer.getCf());
        }
    }

    protected void refreshCategories(JComboBox<String> comboBox) throws Exception {
        comboBox.removeAllItems();
        for (var category : context.categoryDAO.getAll()) {
            comboBox.addItem(category.getDescription());
        }
    }

    protected void refreshProducts(JComboBox<String> comboBox) throws Exception {
        comboBox.removeAllItems();
        for (var product : context.productDAO.getAll()) {
            comboBox.addItem(formatProduct(product));
        }
    }

    protected void refreshOrders(JComboBox<String> comboBox) throws Exception {
        comboBox.removeAllItems();
        for (var order : context.orderDAO.getAll()) {
            comboBox.addItem(formatOrder(order));
        }
    }

    protected void refreshOrdersForCustomer(String customerCf, JComboBox<String> comboBox) throws Exception {
        comboBox.removeAllItems();
        if (customerCf == null || customerCf.isBlank()) {
            return;
        }
        for (var order : context.orderService.getOrders(customerCf)) {
            comboBox.addItem(formatOrder(order));
        }
    }

    protected String formatProduct(Product product) {
        return product.getId() + " - " + product.getName();
    }

    protected String formatOrder(Order order) {
        return order.getId() + " - " + order.getState();
    }

    protected void fillProductsArea(JTextArea area) throws Exception {
        List<Product> products = context.productService.GetProductList();
        StringBuilder builder = new StringBuilder();
        for (Product product : products) {
            builder.append(product.getId())
                    .append(" | ")
                    .append(product)
                    .append("\n");
        }
        area.setText(builder.toString());
    }

    protected void fillOrdersArea(JTextArea area) throws Exception {
        StringBuilder builder = new StringBuilder();
        for (Order order : context.orderService.getOrders()) {
            builder.append(order).append("\n\n");
        }
        area.setText(builder.toString());
    }

    protected void fillOrdersAreaForCustomer(String customerCf, JTextArea area) throws Exception {
        StringBuilder builder = new StringBuilder();
        if (customerCf != null && !customerCf.isBlank()) {
            for (Order order : context.orderService.getOrders(customerCf)) {
                builder.append(order).append("\n\n");
            }
        }
        area.setText(builder.toString());
    }

    protected int extractId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Seleziona un elemento valido.");
        }
        String[] split = value.split("-");
        return Integer.parseInt(split[0].trim());
    }

    protected void runAction(Action action) {
        try {
            action.run();
        } catch (Exception ex) {
            log("Errore: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }

    protected void log(String message) {
        logArea.append(message + "\n");
    }

    @FunctionalInterface
    protected interface Action {
        void run() throws Exception;
    }
}
