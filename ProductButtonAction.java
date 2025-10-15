import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

// THIS CLASS HANDLES PRODUCT SELECTION AND ADDING ITEMS TO RECEIPT
public class ProductButtonAction {
    private CashRegister cashRegister;

    public ProductButtonAction(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    public ActionListener createProductButtonAction(String produktNamn) { //WHEN YOU CLICK A PRODUCT BUTTON*****
        return e -> {
            // FIND THE PRODUCT IN OUR PRODUCT LIST
            Produkt produkt = cashRegister.produktManager.getProdukt(produktNamn);

            // IF WE FOUND THE PRODUCT
            if (produkt != null) {
                // SAVE THIS PRODUCT AS THE SELECTED ONE
                cashRegister.valdProdukt = produkt;
                String productName = produkt.getNamn();
                String centeredText = "        " + productName + "        "; 
                cashRegister.getInputProductName().setText(centeredText); // THIS ADDED THE PRODUCT INTO THE TEXTBOX BELOW*****
            } else {
                // IF PRODUCT NOT FOUND, SHOW ERROR
                System.err.println("Produkt hittades inte: " + produktNamn);
                JOptionPane.showMessageDialog(cashRegister.getFrame(),
                        "Fel: Produkten '" + produktNamn + "' hittades inte!",
                        "Produktfel",
                        JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    public ActionListener createAddToReceiptAction() { // WHEN YOU CLICK THE ADD BUTTON
        return e -> {
            // CHECK IF USER HAS SELECTED A PRODUCT FIRST
            if (cashRegister.valdProdukt == null) {
                JOptionPane.showMessageDialog(cashRegister.getFrame(), // SHOW WARNING IF NO PRODUCT SELECTED
                        "Välj en produkt först!",
                        "Ingen produkt vald",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // GET THE QUANTITY TEXT FROM INPUT FIELD******
            String antalText = cashRegister.getInputCount().getText().trim();
            // CHECK IF QUANTITY FIELD IS EMPTY
            if (antalText.isEmpty()) {
                // SHOW WARNING - NO QUANTITY ENTERED ***
                JOptionPane.showMessageDialog(cashRegister.getFrame(),
                        "Ange antal!",
                        "Saknar antal",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // TRY TO CONVERT TEXT TO NUMBER
            try {
                int antal = Integer.parseInt(antalText);
                // CHECK IF NUMBER IS POSITIVE
                if (antal <= 0) {
                    // SHOW ERROR - QUANTITY MUST BE POSITIVE ****
                    JOptionPane.showMessageDialog(cashRegister.getFrame(),
                            "Antal måste vara större än 0!",
                            "Ogiltigt antal",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cashRegister.currentReceipt == null) { // CREATE FIRST RECEIPT IF IT DOESN'T EXIST
                    cashRegister.startNewReceipt();
                }

                cashRegister.currentReceipt.addProduct(cashRegister.valdProdukt, antal); // ADD THE PRODUCT TO THE RECEIPT

                cashRegister.updateReceiptDisplay(); // UPDATE THE RECEIPT DISPLAY ON SCREEN

                cashRegister.getInputProductName().setText(""); // CLEAR THE INPUT FIELDS FOR NEXT PRODUCT
                cashRegister.getInputCount().setText("");
                cashRegister.valdProdukt = null;

            } catch (NumberFormatException ex) { // IF TEXT CANNOT BE CONVERTED TO NUMBER
                JOptionPane.showMessageDialog(cashRegister.getFrame(), // SHOW ERROR - INVALID NUMBER FORMAT
                        "Ogiltigt antal! Ange endast siffror.",
                        "Formatfel",
                        JOptionPane.ERROR_MESSAGE);
            }
        };
    }
}