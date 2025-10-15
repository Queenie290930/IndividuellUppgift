import java.awt.event.ActionListener;

//This class acts like a "dispatcher", it receives button click requests and forwards them to the right specialist class.

public class ButtonAction {

    private ProductButtonAction productAction; 
    private ReceiptManagementAction receiptAction; 

    public ButtonAction(CashRegister cashRegister) {
        this.productAction = new ProductButtonAction(cashRegister);
        this.receiptAction = new ReceiptManagementAction(cashRegister);
    }

    // Create action for product selection buttons (Kaffe, Bröd, etc.)
    public ActionListener createProductButtonAction(String produktNamn) {
        return productAction.createProductButtonAction(produktNamn);
    }

    // Create action for the "ADD" button (add selected product to receipt)
    public ActionListener createAddToReceiptAction() {
        return productAction.createAddToReceiptAction();
    }

    // Create action for the "PAY" button (process payment)
    public ActionListener createPayButtonAction() {
        return receiptAction.createPayButtonAction();
    }

    // Create action for the "EDIT" button (edit item quantities)
    public ActionListener createEditAction() {
        return receiptAction.createEditAction();
    }

    // Create action for the "REMOVE" button (remove items from receipt)
    public ActionListener createRemoveAction() {
        return receiptAction.createRemoveAction();
    }
}
