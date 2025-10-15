public class Produkt {    
    private String namn; 
    private double pris; 
    private double moms; 

    // CONSTRUCTOR - Create a product with name, price AND tax rate
    public Produkt(String namn, double pris, double moms) {
        this.namn = namn; 
        this.pris = pris; 
        this.moms = moms; 
    }

    // GETTER METHODS - These allow other classes to read our private data
    public String getNamn() {
        return namn;
    }

    public double getPris() {
        return pris;
    }

    public double getMomsProcent() {
        return moms;
    }

    // CALCULATE PRICE WITHOUT TAX
    public double getPrisExklMoms() {
        return pris / (1 + moms / 100.0); // Mathematical formula to remove tax from total price
    }

    // CONVERT PRODUCT TO STRING (for debugging and display)
    @Override
    public String toString() {
        return String.format("Produkt{namn='%s', pris=%.2f (utan moms: %.2f, moms: %.1f%%)}",
                namn, pris, getPrisExklMoms(), moms);
    }
}