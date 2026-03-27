import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class CustomerGUI extends AbstractGastroNetFrame {
    private final JComboBox<String> customerCombo = new JComboBox<>();
    private final JComboBox<String> orderCombo = new JComboBox<>();
    private final JComboBox<String> productCombo = new JComboBox<>();
    private final JTextArea ordersArea = new JTextArea();
    private final JTextArea productsArea = new JTextArea();

    public CustomerGUI(GastroNetContext context) throws Exception {
        super("GastroNet - Customer", context);
        setupUI();
        refreshData();
    }

    private void setupUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Profilo", buildCustomerPanel());
        tabs.addTab("Catalogo", buildCatalogPanel());
        tabs.addTab("Ordini", buildOrderPanel());

        setContentPane(wrapWithLog(tabs));
    }

    private JPanel buildCustomerPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));

        JPanel createCustomerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField firstNameField = new JTextField(10);
        JTextField lastNameField = new JTextField(10);
        JTextField cfField = new JTextField(10);
        JTextField paymentField = new JTextField(10);
        JButton addCustomerButton = new JButton("Registra cliente");

        addCustomerButton.addActionListener(e -> runAction(() -> {
            context.customerService.addCustomer(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    cfField.getText(),
                    paymentField.getText()
            );
            log("Cliente registrato: " + cfField.getText());
            refreshCustomers(customerCombo);
            customerCombo.setSelectedItem(cfField.getText());
            refreshCustomerOrdersView();
        }));

        createCustomerPanel.setBorder(BorderFactory.createTitledBorder("Nuovo cliente"));
        createCustomerPanel.add(new JLabel("Nome"));
        createCustomerPanel.add(firstNameField);
        createCustomerPanel.add(new JLabel("Cognome"));
        createCustomerPanel.add(lastNameField);
        createCustomerPanel.add(new JLabel("CF"));
        createCustomerPanel.add(cfField);
        createCustomerPanel.add(new JLabel("Pagamento"));
        createCustomerPanel.add(paymentField);
        createCustomerPanel.add(addCustomerButton);

        JPanel selectCustomerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshOrdersButton = new JButton("Aggiorna i miei ordini");
        customerCombo.addActionListener(e -> runAction(this::refreshCustomerOrdersView));
        refreshOrdersButton.addActionListener(e -> runAction(this::refreshCustomerOrdersView));

        selectCustomerPanel.setBorder(BorderFactory.createTitledBorder("Cliente attivo"));
        selectCustomerPanel.add(new JLabel("Cliente"));
        selectCustomerPanel.add(customerCombo);
        selectCustomerPanel.add(refreshOrdersButton);

        panel.add(createCustomerPanel);
        panel.add(selectCustomerPanel);
        return panel;
    }

    private JPanel buildCatalogPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        productsArea.setEditable(false);

        JButton refreshButton = new JButton("Aggiorna catalogo");
        refreshButton.addActionListener(e -> runAction(() -> fillProductsArea(productsArea)));

        panel.setBorder(BorderFactory.createTitledBorder("Catalogo prodotti"));
        panel.add(new JScrollPane(productsArea), BorderLayout.CENTER);
        panel.add(refreshButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel createOrderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createOrderButton = new JButton("Crea ordine");
        createOrderButton.addActionListener(e -> runAction(() -> {
            String customerCf = (String) customerCombo.getSelectedItem();
            if (customerCf == null) {
                throw new IllegalArgumentException("Seleziona prima un cliente.");
            }
            int orderId = context.orderService.createOrder(customerCf);
            log("Creato ordine #" + orderId + " per " + customerCf);
            refreshCustomerOrdersView();
        }));

        createOrderPanel.setBorder(BorderFactory.createTitledBorder("Nuovo ordine"));
        createOrderPanel.add(new JLabel("Cliente"));
        createOrderPanel.add(customerCombo);
        createOrderPanel.add(createOrderButton);

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addProductButton = new JButton("Aggiungi prodotto");
        JButton removeProductButton = new JButton("Rimuovi prodotto");
        JButton confirmButton = new JButton("Conferma ordine");

        addProductButton.addActionListener(e -> runAction(() -> {
            int orderId = extractId((String) orderCombo.getSelectedItem());
            int productId = extractId((String) productCombo.getSelectedItem());
            context.orderService.addProductToOrder(productId, orderId);
            log("Prodotto " + productId + " aggiunto all'ordine " + orderId);
            refreshCustomerOrdersView();
            fillProductsArea(productsArea);
        }));

        removeProductButton.addActionListener(e -> runAction(() -> {
            int orderId = extractId((String) orderCombo.getSelectedItem());
            int productId = extractId((String) productCombo.getSelectedItem());
            context.orderService.removeProductFromOrder(productId, orderId);
            log("Prodotto " + productId + " rimosso dall'ordine " + orderId);
            refreshCustomerOrdersView();
            fillProductsArea(productsArea);
        }));

        confirmButton.addActionListener(e -> runAction(() -> {
            int orderId = extractId((String) orderCombo.getSelectedItem());
            context.orderService.confirmOrder(orderId);
            log("Ordine " + orderId + " confermato");
            refreshCustomerOrdersView();
        }));

        actionsPanel.setBorder(BorderFactory.createTitledBorder("Gestione ordine"));
        actionsPanel.add(new JLabel("Ordine"));
        actionsPanel.add(orderCombo);
        actionsPanel.add(new JLabel("Prodotto"));
        actionsPanel.add(productCombo);
        actionsPanel.add(addProductButton);
        actionsPanel.add(removeProductButton);
        actionsPanel.add(confirmButton);

        ordersArea.setEditable(false);
        JScrollPane ordersScroll = new JScrollPane(ordersArea);
        ordersScroll.setBorder(BorderFactory.createTitledBorder("I miei ordini"));

        panel.add(createOrderPanel, BorderLayout.NORTH);
        panel.add(actionsPanel, BorderLayout.CENTER);
        panel.add(ordersScroll, BorderLayout.SOUTH);

        return panel;
    }

    private void refreshData() throws Exception {
        refreshCustomers(customerCombo);
        refreshProducts(productCombo);
        fillProductsArea(productsArea);
        refreshCustomerOrdersView();
    }

    private void refreshCustomerOrdersView() throws Exception {
        String customerCf = (String) customerCombo.getSelectedItem();
        refreshOrdersForCustomer(customerCf, orderCombo);
        fillOrdersAreaForCustomer(customerCf, ordersArea);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                CustomerGUI frame = new CustomerGUI(new GastroNetContext());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
