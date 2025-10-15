import javax.swing.JFrame;
import javax.swing.JOptionPane;

// This class handles mixed payment logic - when cash is not enough
public class MixedPaymentHandler {

    // Ask user if they want to pay remaining amount with card
    public static PaymentMethod handleInsufficientCash(
            JFrame parentFrame,
            double totalAmount,
            double cashPaid,
            double roundedAmount) {

        // Calculate the actual remaining balance (not rounded)
        double remainingBalance = totalAmount - cashPaid;
        double CashToPay = roundedAmount - cashPaid;

        // if the entered amount is lesser than the amount that should be paid.
        int userChoice = JOptionPane.showConfirmDialog(
                parentFrame,
                String.format("Kontant: %.0f kr räcker inte.\n" +
                        "Att betala(kontant):    %.2f kr\n" +
                        "Att betala(kort):          %.2f kr\n" +
                        "Vill du betala resterande med kort?",
                        cashPaid, CashToPay, remainingBalance),
                "Otillräckligt kontant",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (userChoice == JOptionPane.YES_OPTION) {
            // User said yes - create mixed payment with actual remaining balance
            return PaymentMethod.skapaBlandadBetalning(totalAmount, remainingBalance, cashPaid);
        } else {
            // User said no - show error and return null
            JOptionPane.showMessageDialog(parentFrame,
                    String.format("Minst %.0f kr kontant krävs.", roundedAmount),
                    "Otillräckligt belopp",
                    JOptionPane.ERROR_MESSAGE);
            return null; // Indicates payment was cancelled
        }
    }

    // Helper method to check if cash amount is sufficient
    public static boolean isCashSufficient(double cashPaid, double roundedAmount) {
        return cashPaid >= roundedAmount;
    }
}