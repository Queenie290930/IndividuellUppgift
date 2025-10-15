import java.awt.*;
import javax.swing.*;

public class GUI {
    private JFrame frame;
    private JTextArea receipt;
    private JTextArea inputProductName;
    private JTextField inputCount;
    private JLabel totalLabel;

    private ProduktManager produktManager;
    private ButtonAction buttonAction;

    public GUI(ProduktManager produktManager, ButtonAction buttonAction) {
        this.produktManager = produktManager;
        this.buttonAction = buttonAction;
        createGUI();
    }

    private void createGUI() {
        frame = new JFrame("IOT25 POS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout(10, 10)); // Resizable layout
        frame.getContentPane().setBackground(Color.PINK);

        // LEFT: product buttons
        frame.add(createProductButtonsPanel(), BorderLayout.CENTER);

        // RIGHT: receipt area
        frame.add(createReceiptPanel(), BorderLayout.EAST);

        // BOTTOM: input + action buttons
        frame.add(createBottomPanel(), BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JPanel createProductButtonsPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        buttonPanel.setBackground(Color.PINK);

        for (String productName : produktManager.getAllaProduktNamn()) { //IT MAKE MY BUTTONS DYNAMIC, I CAN ADD/REMOVE PRODUCT FROM PRODUCT.TXT
            JButton button = new JButton(productName);
            button.setFont(new Font("Arial", Font.BOLD, 15));
            button.addActionListener(buttonAction.createProductButtonAction(productName));
            buttonPanel.add(button);
        }

        return buttonPanel;
    }

    private JPanel createReceiptPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setPreferredSize(new Dimension(390, 0)); // Fixed width, flexible height
        rightPanel.setBackground(Color.PINK);

        receipt = new JTextArea();
        receipt.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receipt.setEditable(false);
        receipt.setLineWrap(true);
        receipt.setWrapStyleWord(true);
        receipt.setBackground(Color.WHITE);
        receipt.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(receipt);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        totalLabel = new JLabel("Total: 0.00 kr", SwingConstants.CENTER);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.RED);
        totalLabel.setOpaque(true);
        totalLabel.setBackground(Color.WHITE);
        rightPanel.add(totalLabel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomPanel.setBackground(Color.PINK);

        inputProductName = new JTextArea(1, 15);
        inputProductName.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(inputProductName);

        bottomPanel.add(new JLabel("Antal:"));
        inputCount = new JTextField(5);
        inputCount.setFont(new Font("Arial", Font.BOLD, 18));
        inputCount.setHorizontalAlignment(JTextField.CENTER);
        bottomPanel.add(inputCount);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(buttonAction.createAddToReceiptAction());
        bottomPanel.add(addButton);

        JButton editButton = new JButton("Edit");
        editButton.addActionListener(buttonAction.createEditAction());
        bottomPanel.add(editButton);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(buttonAction.createRemoveAction());
        bottomPanel.add(removeButton);

        JButton payButton = new JButton("Pay");
        payButton.addActionListener(buttonAction.createPayButtonAction());
        bottomPanel.add(payButton);

        return bottomPanel;
    }

    // === GETTERS ===
    public JFrame getFrame() {
        return frame;
    }

    public JTextArea getReceipt() {
        return receipt;
    }

    public JTextArea getInputProductName() {
        return inputProductName;
    }

    public JTextField getInputCount() {
        return inputCount;
    }

    public JLabel getTotalLabel() {
        return totalLabel;
    }
}
