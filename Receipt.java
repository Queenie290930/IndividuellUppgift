import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;
import java.util.*;

//RECEIPT CLASS - The Digital Receipt that stores all purchase information *** (143 LINES)
public class Receipt {

    // PRIVATE FIELDS - The data that every receipt needs to store
    private int kvittoNummer; 
    private LocalDateTime timestamp; 
    private List<ReceiptRow> rows; 
    private double totalSumma; 
    private double totalMoms; 
    private Map<Double, Double> momsPerSats; 
    private boolean isStarted = false; 

    // CONSTRUCTOR - Create a new empty receipt
    public Receipt(int kvittoNummer) {
        // Store the receipt number
        this.kvittoNummer = kvittoNummer;

        // Initialize empty collections for items and tax tracking
        this.rows = new ArrayList<>(); 
        this.momsPerSats = new HashMap<>(); 

        // Initialize all money amounts to zero
        this.totalSumma = 0.0; 
        this.totalMoms = 0.0; 
    }

    // This method is called when the first item is added to the receipt.
    public void startReceipt() {
        if (!isStarted) { 
            this.timestamp = LocalDateTime.now(); 
            this.isStarted = true; 
        }
    }

    // ADD PRODUCT - Add an item to the receipt with specified quantity
    public void addProduct(Produkt produkt, int antal) {

        // Start the receipt if this is the first item
        if (!isStarted) {
            startReceipt();
        }

        // Create a new receipt row for this product
        ReceiptRow row = new ReceiptRow(produkt, antal);
        rows.add(row); // Add this row to our list of items

        // Get the calculated amounts from the new row
        double radTotalInklMoms = row.getTotalInklMoms(); 
        double radMoms = row.getTotalMoms(); 

        // Update the receipt's overall totals
        totalSumma += radTotalInklMoms; // Add this row's total to receipt total
        totalMoms += radMoms; // Add this row's tax to receipt tax total

        // This is important because different products might have different tax rates
        double momsProcent = produkt.getMomsProcent(); // Get the tax rate for this product
        double currentTaxForThisRate = momsPerSats.getOrDefault(momsProcent, 0.0); // Get current tax total for this rate
                                                                                   
        momsPerSats.put(momsProcent, currentTaxForThisRate + radMoms); // Update the tax total for this rate
    }

    // FORMAT RECEIPT 
    public String formateraKvitto() {
        if (!isStarted || rows.isEmpty()) { //THE RECEIPT STARTS EMPTY ********
            return "Tomt kvitto"; 
        }

        StringBuilder sb = new StringBuilder();

        // RECEIPT HEADER WITH STORE INFO
        sb.append("                   QUEENIE'S SUPERSHOP\n");
        sb.append("                      Pink street\n");
        sb.append("                  76544 Wonderland City\n");
        sb.append("====================================================\n");
        sb.append("Kvittonummer: ").append(kvittoNummer) // ADD RECEIPT NUMBER AND DATE
                .append("         Datum: ").append(timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .append("\n");
        sb.append("====================================================\n\n");

        for (ReceiptRow row : rows) { // ADD ALL PRODUCT ROWS TO RECEIPT
            sb.append(row.formateraRad()).append("\n"); // FORMAT AND ADD EACH ROW
        }

        return sb.toString(); // RETURN FORMATTED RECEIPT
    }

    public String formateraBetalningssammanfattning(PaymentMethod betalningsmetod) { // FORMAT PAYMENT SUMMARY FOR THE RECEIPT
                                                                                     
        StringBuilder sb = new StringBuilder();

        double netto = totalSumma - totalMoms; // CALCULATE NET AMOUNT (WITHOUT TAX)

        sb.append("====================================================\n");

        // SHOW TOTAL AMOUNT AND TAX**********
        sb.append(String.format("ATT BETALA:                           %9.2f kr\n", totalSumma));
        sb.append(String.format("VARAV MOMS:                           %9.2f kr\n", totalMoms));

        sb.append("\n====================================================\n");

        // CREATE TAX BREAKDOWN TABLE
        sb.append("\n");
        sb.append( "Moms%       Moms        Netto      Brutto\n");

        boolean firstRow = true; // SHOWs TAX DETAILS FOR EACH TAX RATE *****
        for (Map.Entry<Double, Double> entry : momsPerSats.entrySet()) {
            double momsProcent = entry.getKey(); 
            double momsBelopp = entry.getValue(); 

            if (firstRow) { // FIRST ROW SHOWS NET AND GROSS AMOUNTS
                sb.append(String.format("%.0f%%%-8s %-10.2f %-10.2f %.2f \n",
                        momsProcent, "", momsBelopp, netto, totalSumma));
                firstRow = false;
            } else {
                sb.append(String.format("%.0f%%%-8s %-10.2f\n", // SUBSEQUENT ROWS SHOW ONLY TAX PERCENTAGE AND AMOUNT
                        momsProcent, "", momsBelopp));
            }
        }
        sb.append("\n");

        // ADD PAYMENT METHOD INFORMATION
        sb.append("\n");
        sb.append(betalningsmetod.formateraForKvitto());

        // ADD THANK YOU MESSAGE******
        sb.append("\n\n             *** TACK FÖR DITT KÖP! ***\n");
        sb.append("\n");

        return sb.toString(); // RETURN COMPLETE PAYMENT SUMMARY
    }
    // GET RECEIPT NUMBER
    public int getKvittoNummer() {
        return kvittoNummer;
    }
    // GET TOTAL AMOUNT 
    public double getTotalSumma() {
        return totalSumma;
    }
    // CHECK IF EMPTY - Test whether this receipt has any items, return true if no items
    public boolean isEmpty() {
        return rows.isEmpty();
    }

    // REMOVE ROW - Delete an item from the receipt and update all totals
    public boolean removeRow(int index) {

        // Validate that the index is within the valid range
        if (index >= 0 && index < rows.size()) {

            // Remove the item and keep a reference to it
            ReceiptRow removedRow = rows.remove(index);

            // Subtract the removed item's amounts from our totals
            totalSumma -= removedRow.getTotalInklMoms(); 
            totalMoms -= removedRow.getTotalMoms(); 

            // Update tax tracking for this specific tax rate
            double momsProcent = removedRow.getProdukt().getMomsProcent(); // Get the tax rate
            double currentMoms = momsPerSats.getOrDefault(momsProcent, 0.0); // Get current tax total for this rate
            double newMoms = currentMoms - removedRow.getTotalMoms(); // Calculate new tax total

            // Handle the tax tracking update
            if (newMoms <= 0.001) {
                // If tax amount is approximately zero (I am using 0.001 to handle floating-point precision issues)
                momsPerSats.remove(momsProcent); // Remove this tax rate completely
            } else {
                momsPerSats.put(momsProcent, newMoms); // Update with new tax amount
            }

            return true; //item was removed and totals updated
        }
        return false; //index was out of range
    }

    // EDIT ROW QUANTITY - Change how many of an item the customer wants
    public boolean editRowQuantity(int index, int newAntal) {
        if (index >= 0 && index < rows.size() && newAntal > 0) { // CHECK IF INDEX AND QUANTITY ARE VALID
            ReceiptRow row = rows.get(index); // GET THE ROW TO EDIT

            // REMOVE OLD VALUES FROM TOTALS
            totalSumma -= row.getTotalInklMoms();
            totalMoms -= row.getTotalMoms();

            double momsProcent = row.getProdukt().getMomsProcent();
            double currentMoms = momsPerSats.getOrDefault(momsProcent, 0.0); // UPDATE TAX TRACKING - REMOVE OLD TAX AMOUNT
                                                                         
            momsPerSats.put(momsProcent, currentMoms - row.getTotalMoms());

            ReceiptRow newRow = new ReceiptRow(row.getProdukt(), newAntal); // CREATE NEW ROW WITH NEW QUANTITY
            rows.set(index, newRow); // REPLACE OLD ROW WITH NEW ROW

            totalSumma += newRow.getTotalInklMoms();
            totalMoms += newRow.getTotalMoms();
            momsPerSats.put(momsProcent, momsPerSats.getOrDefault(momsProcent, 0.0) + newRow.getTotalMoms());

            return true;
        }
        return false; // FAILED - INVALID INPUT
    }

    public String[] getRowDescriptions() { // GET FORMATTED LIST OF ALL RECEIPT ROWS FOR DISPLAY IN DIALOGS
        String[] descriptions = new String[rows.size()]; // CREATE ARRAY FOR DESCRIPTIONS
        for (int i = 0; i < rows.size(); i++) {
            ReceiptRow row = rows.get(i);

            descriptions[i] = String.format("%d. %s", i + 1, row.formateraRad()); // FORMAT EACH ROW WITH NUMBER AND DESCRIPTION
                                                                                  
        }
        return descriptions; // RETURN ARRAY OF FORMATTED DESCRIPTIONS
    }
}