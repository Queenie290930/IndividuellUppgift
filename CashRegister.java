
import javax.swing.*;

//This is the main coordinator class that brings everything together:
public class CashRegister {

    private GUI gui;

    // PACKAGE-PRIVATE FIELDS - These can be accessed by other classes in same package
    ProduktManager produktManager; 
    Produkt valdProdukt = null; 
    Receipt currentReceipt; 
    int kvittoRaknarenummer = 0; 

    private ButtonAction buttonAction; // Handles all button click events

    // CONSTRUCTOR - Set up the entire cash register system
    public CashRegister() {
        produktManager = new ProduktManager();

      
        buttonAction = new ButtonAction(this);  // Set up button event handling

      
        gui = new GUI(produktManager, buttonAction);   // Create the graphical user interface

        currentReceipt = null; // Start with no receipt

        updateReceiptDisplay();
    }

    // GETTER METHODS - Allow ButtonAction classes to access GUI components
    public JFrame getFrame() {
        return gui.getFrame();
    }

    public JTextArea getReceipt() {
        return gui.getReceipt();
    }

    public JTextArea getInputProductName() {
        return gui.getInputProductName();
    }

    public JTextField getInputCount() {
        return gui.getInputCount();
    }

    public JLabel getTotalLabel() {
        return gui.getTotalLabel();
    }

    // START NEW RECEIPT - Prepare for a new customer
    public void startNewReceipt() {
        valdProdukt = null;  //Clear any selected product

        kvittoRaknarenummer++; //JUST LIKE THE CODE THAT YOU(ZEN) HAS WRITTEN, ONLY TRANSLATED TO SWEDISH*****
        currentReceipt = new Receipt(kvittoRaknarenummer); 

        if (kvittoRaknarenummer > 1) { // Only show dialog for subsequent receipts
            JOptionPane.showMessageDialog(getFrame(), "Klicka för att starta nästa kvitto!");
        }

        // Visa header för nytt kvitto
        updateReceiptDisplay();
    }

    void updateReceiptDisplay() { //THIS SHOWS IN AN EMPTY RECEIPT*******
        if (currentReceipt == null || currentReceipt.isEmpty()) {
            getReceipt().setText("");
            getReceipt().append("                   QUEENIE'S SUPERSHOP\n");
            getReceipt().append("                       Pink street\n                 76544 Wonderland City\n");
            getReceipt().append("====================================================\n");
            getReceipt().append("\n");
            getTotalLabel().setText("Total: 0.00 kr"); //THIS UPDATES THE TOTAL EVERYTIME WE ADD A PRODUCT.
        } else {
            getReceipt().setText(currentReceipt.formateraKvitto());
            getTotalLabel().setText(String.format("Total: %.2f kr", currentReceipt.getTotalSumma()));
        }
    }
}