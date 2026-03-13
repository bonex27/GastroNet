import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class GastroNetGUI extends JFrame {
    private final GastroNetContext context;

    public GastroNetGUI() throws Exception {
        super("GastroNet Launcher");
        this.context = new GastroNetContext();
        setupUI();
    }

    private void setupUI() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(420, 220);
        setLocationRelativeTo(null);

        JLabel title = new JLabel("Scegli quale GUI aprire", SwingConstants.CENTER);

        JButton customerButton = new JButton("Apri Customer GUI");
        customerButton.addActionListener(e -> openWindow(() -> new CustomerGUI(context)));

        JButton attendantButton = new JButton("Apri Attendant GUI");
        attendantButton.addActionListener(e -> openWindow(() -> new AttendantGUI(context)));

        JPanel buttons = new JPanel(new GridLayout(2, 1, 12, 12));
        buttons.add(customerButton);
        buttons.add(attendantButton);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.add(title, BorderLayout.NORTH);
        root.add(buttons, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void openWindow(WindowFactory factory) {
        try {
            JFrame frame = factory.create();
            frame.setVisible(true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @FunctionalInterface
    private interface WindowFactory {
        JFrame create() throws Exception;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new GastroNetGUI().setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
