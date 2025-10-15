import java.awt.event.ActionListener;                    
import javax.swing.JOptionPane;

public class ReceiptManagementAction {

    private CashRegister cashRegister;

    // CONSTRUCTOR - Connect this action handler to the cash register
    public ReceiptManagementAction(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }

    // CREATE PAY BUTTON ACTION - Handle when customer wants to pay
    public ActionListener createPayButtonAction() {

        return e -> {

            // Safety check - make sure there are items to pay for
            if (cashRegister.currentReceipt == null || cashRegister.currentReceipt.isEmpty()) {
                JOptionPane.showMessageDialog(cashRegister.getFrame(),
                        "Inga varor att betala för!", 
                        "Tom varukorg",
                        JOptionPane.INFORMATION_MESSAGE);
                return; //  can't process payment with no items
            }

            // Ask customer to choose payment method
            String[] options = { "Kort", "Kontant", "Avbryt" }; 
            int choice = JOptionPane.showOptionDialog(
                    cashRegister.getFrame(),
                    "Välj betalningsmetod:", 
                    "Betalning", 
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0] // Default to "Card"
            );

            // Process the chosen payment method
            PaymentMethod valdBetalningsmetod = null; // Will store the final payment method object

            if (choice == 0) {
                // CARD PAYMENT - Simple case, exact amount, no rounding needed
                valdBetalningsmetod = PaymentMethod.skapaKortBetalning(cashRegister.currentReceipt.getTotalSumma());

            } else if (choice == 1) {
                // CASH PAYMENT - More complex, needs rounding and change calculation
                double totalBelopp = cashRegister.currentReceipt.getTotalSumma();
                double avrundatBelopp = Math.round(totalBelopp); // Round to nearest krona

                // Ask how much cash the customer is giving us
                String input = JOptionPane.showInputDialog(
                        cashRegister.getFrame(),
                        String.format("Att betala: %.0f kr\nAnge hur mycket kunden betalar kontant:", avrundatBelopp),
                        "Kontantbetalning", 
                        JOptionPane.QUESTION_MESSAGE);

                // Check if user cancelled or entered nothing
                if (input == null || input.trim().isEmpty()) {
                    return; //exit without processing payment
                }

                try {
                    double betaltBelopp = Double.parseDouble(input.trim());

                    // Check if customer paid enough using helper method
                    if (!MixedPaymentHandler.isCashSufficient(betaltBelopp, avrundatBelopp)) {
                        // Handle insufficient cash - offer mixed payment
                        valdBetalningsmetod = MixedPaymentHandler.handleInsufficientCash(
                                cashRegister.getFrame(),
                                totalBelopp,
                                betaltBelopp,
                                avrundatBelopp);

                        if (valdBetalningsmetod == null) {
                            // User declined mixed payment
                            return;
                        }
                    } else {
                        // Enough cash - create pure cash payment
                        valdBetalningsmetod = PaymentMethod.skapaKontantBetalningMedVaxel(totalBelopp, betaltBelopp);

                        // Show change amount in a dialog if there is change
                        double vaxel = betaltBelopp - avrundatBelopp;
                        if (vaxel > 0.001) {
                            JOptionPane.showMessageDialog(cashRegister.getFrame(),
                                    String.format("VÄXEL ATT GE TILLBAKA:\n%.2f kr", vaxel),
                                    "Växel",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(cashRegister.getFrame(),
                            "Ogiltigt belopp! Ange endast siffror.",
                            "Fel format",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                return;
            }

            // CREATE COMPLETE RECEIPT WITH PAYMENT SUMMARY
            String fullReceipt = cashRegister.currentReceipt.formateraKvitto() +
                    cashRegister.currentReceipt.formateraBetalningssammanfattning(valdBetalningsmetod);

            cashRegister.getReceipt().setText(fullReceipt);

            cashRegister.startNewReceipt(); //THIS CODE CLEARS THE RECEIPT SO THE NEXT RECEIPT STARTS EMPTY ***
        };
    }

    // CREATE EDIT ACTION - Handle when user wants to edit item quantities
    public ActionListener createEditAction() {
        return e -> ReceiptEditHandler.handleEditAction(cashRegister);
    }

    // CREATE REMOVE ACTION - Handle when user wants to remove items
    public ActionListener createRemoveAction() {
        return e -> ReceiptRemoveHandler.handleRemoveAction(cashRegister);
    }
}