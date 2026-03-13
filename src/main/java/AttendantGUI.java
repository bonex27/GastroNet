import domainModel.Category;

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

public class AttendantGUI extends AbstractGastroNetFrame {
    private final JComboBox<String> categoryCombo = new JComboBox<>();
    private final JComboBox<String> orderCombo = new JComboBox<>();
    private final JTextArea productsArea = new JTextArea();
    private final JTextArea ordersArea = new JTextArea();

    public AttendantGUI(GastroNetContext context) throws Exception {
        super("GastroNet - Attendant", context);
        setupUI();
        refreshData();
    }

    private void setupUI() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Gestione", buildManagementPanel());
        tabs.addTab("Ordini", buildOrdersPanel());

        setContentPane(wrapWithLog(tabs));
    }

    private JPanel buildManagementPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));

        JPanel attendantPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField attendantName = new JTextField(10);
        JTextField attendantSurname = new JTextField(10);
        JTextField attendantCf = new JTextField(10);
        JTextField attendantIban = new JTextField(14);
        JButton addAttendantButton = new JButton("Aggiungi addetto");

        addAttendantButton.addActionListener(e -> runAction(() -> {
            context.attendantController.addAttendant(
                    attendantName.getText(),
                    attendantSurname.getText(),
                    attendantCf.getText(),
                    attendantIban.getText()
            );
            log("Addetto registrato: " + attendantCf.getText());
        }));

        attendantPanel.setBorder(BorderFactory.createTitledBorder("Anagrafica addetto"));
        attendantPanel.add(new JLabel("Nome"));
        attendantPanel.add(attendantName);
        attendantPanel.add(new JLabel("Cognome"));
        attendantPanel.add(attendantSurname);
        attendantPanel.add(new JLabel("CF"));
        attendantPanel.add(attendantCf);
        attendantPanel.add(new JLabel("IBAN"));
        attendantPanel.add(attendantIban);
        attendantPanel.add(addAttendantButton);

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField categoryDescription = new JTextField(20);
        JButton addCategoryButton = new JButton("Aggiungi categoria");

        addCategoryButton.addActionListener(e -> runAction(() -> {
            context.categoryController.CreateCategory(categoryDescription.getText());
            log("Categoria aggiunta: " + categoryDescription.getText());
            refreshCategories(categoryCombo);
        }));

        categoryPanel.setBorder(BorderFactory.createTitledBorder("Categorie"));
        categoryPanel.add(new JLabel("Descrizione"));
        categoryPanel.add(categoryDescription);
        categoryPanel.add(addCategoryButton);

        JPanel productPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField productName = new JTextField(10);
        JTextField productDescription = new JTextField(16);
        JTextField productPrice = new JTextField(6);
        JTextField productStock = new JTextField(4);
        JButton addProductButton = new JButton("Aggiungi prodotto");

        addProductButton.addActionListener(e -> runAction(() -> {
            String selectedCategory = (String) categoryCombo.getSelectedItem();
            if (selectedCategory == null) {
                throw new IllegalArgumentException("Inserisci prima una categoria.");
            }
            Category category = context.categoryDAO.get(selectedCategory);
            int productId = context.productController.AddProduct(
                    productName.getText(),
                    productDescription.getText(),
                    Double.parseDouble(productPrice.getText()),
                    category,
                    Integer.parseInt(productStock.getText())
            );
            log("Prodotto aggiunto: " + productId + " - " + productName.getText());
            fillProductsArea(productsArea);
        }));

        productPanel.setBorder(BorderFactory.createTitledBorder("Prodotti"));
        productPanel.add(new JLabel("Nome"));
        productPanel.add(productName);
        productPanel.add(new JLabel("Descrizione"));
        productPanel.add(productDescription);
        productPanel.add(new JLabel("Prezzo"));
        productPanel.add(productPrice);
        productPanel.add(new JLabel("Stock"));
        productPanel.add(productStock);
        productPanel.add(new JLabel("Categoria"));
        productPanel.add(categoryCombo);
        productPanel.add(addProductButton);

        productsArea.setEditable(false);
        JScrollPane productScroll = new JScrollPane(productsArea);
        productScroll.setBorder(BorderFactory.createTitledBorder("Catalogo attuale"));

        panel.add(attendantPanel);
        panel.add(categoryPanel);
        panel.add(productPanel);
        panel.add(productScroll);
        return panel;
    }

    private JPanel buildOrdersPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshOrdersButton = new JButton("Aggiorna ordini");
        JButton startPreparationButton = new JButton("Inizia preparazione");
        JButton endPreparationButton = new JButton("Fine preparazione");
        JButton deliverButton = new JButton("Consegna");

        refreshOrdersButton.addActionListener(e -> runAction(this::refreshOrdersView));
        startPreparationButton.addActionListener(e -> moveOrderState("Preparation"));
        endPreparationButton.addActionListener(e -> moveOrderState("Ready"));
        deliverButton.addActionListener(e -> moveOrderState("Delivered"));

        actionsPanel.setBorder(BorderFactory.createTitledBorder("Stato ordine"));
        actionsPanel.add(new JLabel("Ordine"));
        actionsPanel.add(orderCombo);
        actionsPanel.add(refreshOrdersButton);
        actionsPanel.add(startPreparationButton);
        actionsPanel.add(endPreparationButton);
        actionsPanel.add(deliverButton);

        ordersArea.setEditable(false);
        JScrollPane ordersScroll = new JScrollPane(ordersArea);
        ordersScroll.setBorder(BorderFactory.createTitledBorder("Tutti gli ordini"));

        panel.add(actionsPanel, BorderLayout.NORTH);
        panel.add(ordersScroll, BorderLayout.CENTER);
        return panel;
    }

    private void moveOrderState(String targetState) {
        runAction(() -> {
            int orderId = extractId((String) orderCombo.getSelectedItem());
            switch (targetState) {
                case "Preparation" -> context.orderController.startPreparation(orderId);
                case "Ready" -> context.orderController.endPreparation(orderId);
                case "Delivered" -> context.orderController.collectOrder(orderId);
                default -> throw new IllegalArgumentException("Stato non supportato: " + targetState);
            }
            log("Ordine " + orderId + " aggiornato a " + targetState);
            refreshOrdersView();
        });
    }

    private void refreshData() throws Exception {
        refreshCategories(categoryCombo);
        fillProductsArea(productsArea);
        refreshOrdersView();
    }

    private void refreshOrdersView() throws Exception {
        refreshOrders(orderCombo);
        fillOrdersArea(ordersArea);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AttendantGUI frame = new AttendantGUI(new GastroNetContext());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
