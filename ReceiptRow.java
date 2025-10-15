//RECEIPTROW CLASS - Represents one line item on a receipt     
public class ReceiptRow {

    // PRIVATE FIELDS - The information about this line item
    private Produkt produkt;
    private int antal;

    // Price calculations - we calculate these once and store them for efficiency
    private double prisPerStyckInklMoms;
    private double prisPerStyckExklMoms;
    private double totalInklMoms;
    private double totalExklMoms;
    private double totalMoms;

    // CONSTRUCTOR - Create a new receipt row
    public ReceiptRow(Produkt produkt, int antal) {
        // Store the basic information
        this.produkt = produkt;
        this.antal = antal;

        // Get the prices per item from the product
        this.prisPerStyckInklMoms = produkt.getPris();
        this.prisPerStyckExklMoms = produkt.getPrisExklMoms();

        // Calculate total amounts (multiply by quantity)
        this.totalInklMoms = prisPerStyckInklMoms * antal;
        this.totalExklMoms = prisPerStyckExklMoms * antal;
        this.totalMoms = totalInklMoms - totalExklMoms;
    }

    // FORMAT ROW - Create a nicely formatted string for the receipt
    public String formateraRad() {
        return String.format("%-30s * %-3d  %9.2f kr",
                produkt.getNamn(), antal, totalInklMoms);
    }

    // GETTER METHODS - Allow other classes to access our private data
    public Produkt getProdukt() {
        return produkt;
    }

    public double getTotalInklMoms() {
        return totalInklMoms;
    }

    public double getTotalMoms() {
        return totalMoms;
    }
}