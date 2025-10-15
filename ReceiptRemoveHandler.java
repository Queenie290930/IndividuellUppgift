import javax.swing.JFrame; // (31 code lines)
import javax.swing.JOptionPane;

public class ReceiptRemoveHandler {

    // MAIN REMOVE METHOD - This is called when user clicks "REMOVE" button
    public static void handleRemoveAction(CashRegister kassa) {

        // Safety check - Make sure there are items to remove
        if (kassa.currentReceipt == null || kassa.currentReceipt.isEmpty()) {
            // If receipt is empty, show a friendly message and exit
            JOptionPane.showMessageDialog(kassa.getFrame(),
                    "Inga varor att ta bort!",
                    "Tom varukorg",
                    JOptionPane.INFORMATION_MESSAGE);
            return; // Exit early - nothing more to do
        }

        // This creates an array of formatted strings like "1. Coffee x 2 = 59.00 kr"
        String[] itemList = kassa.currentReceipt.getRowDescriptions();

        //  Show a dropdown dialog so user can choose which item to remove
        String chosenItem = (String) JOptionPane.showInputDialog(
                kassa.getFrame(), // Parent window
                "Välj rad att ta bort:",
                "Ta bort kvittorad",
                JOptionPane.QUESTION_MESSAGE, // Type of dialog (question icon)
                null, // No custom icon
                itemList, // items in receipt 
                itemList[0] // first item selected will be shown
        );

        // Check if user cancelled the dialog
        if (chosenItem == null)
            return; // if the user pressed cancel or X, exit without doing anything

        // We need the index number to tell the Receipt class which row to remove
        int itemIndex = findItemIndex(itemList, chosenItem);

        // If we found the item, proceed with removal
        if (itemIndex >= 0) {
            removeItem(kassa, itemIndex, chosenItem);
        }
    }

    private static int findItemIndex(String[] itemList, String chosenItem) {
        // Loop through each item in the list
        for (int i = 0; i < itemList.length; i++) {
            if (itemList[i].equals(chosenItem)) { // Found a match!
                return i; // Return the index position
            }
        }
        return -1; // Not found (this shouldn't happen, but just in case)
    }

    // REMOVE ITEM - Actually remove the item with confirmation
    private static void removeItem(CashRegister kassa, int itemIndex, String chosenItem) {

        // Ask for confirmation before removing
        int confirm = JOptionPane.showConfirmDialog(
                kassa.getFrame(),
                "Är du säker på att du vill ta bort:\n" + chosenItem,
                "Bekräfta borttagning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE 
        );

        // Only proceed if user clicked "Yes"
        if (confirm == JOptionPane.YES_OPTION) {

            // Try to remove the item from the receipt
            if (kassa.currentReceipt.removeRow(itemIndex)) {
                kassa.updateReceiptDisplay(); // if the removal is successed, the receipt will be refreshed
            } else {
                // FAILURE: Something went wrong during removal
                showError(kassa.getFrame(), "Kunde inte ta bort raden!");
            }
        }
    }

    // SHOW ERROR - Helper method to display error messages
    private static void showError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Fel", JOptionPane.ERROR_MESSAGE);
    }
}