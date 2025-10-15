import javax.swing.JFrame;                                         
import javax.swing.JOptionPane;

//RECEIPTEDITHANDLER CLASS - Handles editing item quantities on receipts
public class ReceiptEditHandler {
    
    //MAIN EDIT METHOD - This is called when user clicks "EDIT" button
    public static void handleEditAction(CashRegister kassa) {
        
        // Safety check - Make sure there are items to edit
        if (kassa.currentReceipt == null || kassa.currentReceipt.isEmpty()) {
            // If receipt is empty, show a friendly message and exit
            JOptionPane.showMessageDialog(kassa.getFrame(), 
                "Inga varor att redigera!",   
                "Tom varukorg",             
                JOptionPane.INFORMATION_MESSAGE);
            return;  // Exit early - nothing to edit
        }
        
        // This creates an array of formatted strings like "1. Coffee x 2 = 59.00 kr"
        String[] itemList = kassa.currentReceipt.getRowDescriptions();
        
        // Show a dropdown dialog so user can choose which item to edit
        String chosenItem = (String) JOptionPane.showInputDialog(
            kassa.getFrame(),                  // Parent window
            "Välj rad att redigera:",        
            "Redigera kvittorad",            
            JOptionPane.QUESTION_MESSAGE,     
            null,                           
            itemList,                         
            itemList[0]                      
        );

        // Check if user cancelled the dialog by pressing X or Cancel
        if (chosenItem == null) return;  // if user cancelled - exit without doing anything
        
        //  Find the array index of the chosen item to tell the Receipt class which row to edit
        int itemIndex = findItemIndex(itemList, chosenItem);
        
        //  If we found the item, proceed with editing
        if (itemIndex >= 0) {
            editItemQuantity(kassa, itemIndex);
        }
    }
        
    //FIND ITEM INDEX - Convert chosen item back to its position in the list 
    private static int findItemIndex(String[] itemList, String chosenItem) {
        for (int i = 0; i < itemList.length; i++) {  // Loop through each item in the list
            if (itemList[i].equals(chosenItem)) {  
                return i;  
            }
        }
        return -1;  // Not found (this shouldn't happen, but just in case)
    }
       
    // EDIT ITEM QUANTITY - Ask for new quantity and update the receipt 
    private static void editItemQuantity(CashRegister kassa, int itemIndex) {
        
        //  Ask user for the new quantity
        String newQuantityStr = JOptionPane.showInputDialog(
            kassa.getFrame(),
            "Ange nytt antal:",               
            "Redigera antal",            
            JOptionPane.QUESTION_MESSAGE
        );
        
        // Check if user entered something (not cancelled or left blank)
        if (newQuantityStr != null && !newQuantityStr.trim().isEmpty()) {
            
            try {
                // Try to convert the input text to a number
                int newQuantity = Integer.parseInt(newQuantityStr.trim());
            
                //Validate that the quantity is positive
                if (newQuantity > 0) {
                    
                    //Try to update the receipt with the new quantity
                    if (kassa.currentReceipt.editRowQuantity(itemIndex, newQuantity)) {
                        kassa.updateReceiptDisplay();
                    } else {
                        showError(kassa.getFrame(), "Kunde inte redigera raden!");
                    }
                    
                } else {
                    showError(kassa.getFrame(), "Antal måste vara större än 0!");
                }
 
            } catch (NumberFormatException ex) {
                showError(kassa.getFrame(), "Ogiltigt antal! Ange endast siffror.");
            }
        }
    }    
    //SHOW ERROR - Helper method to display error messages. When something goes wrong, we want to show a consistent error dialog.
    private static void showError(JFrame frame, String message) {
        JOptionPane.showMessageDialog(frame, message, "Fel", JOptionPane.ERROR_MESSAGE);
    }
}