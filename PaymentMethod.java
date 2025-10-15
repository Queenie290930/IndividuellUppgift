
//PAYMENTMETHOD CLASS - Handles different ways customers can pay
public class PaymentMethod {

    //
    public enum BetalningsTyp {
        KORT("Kort"),
        KONTANT("Kontant"),
        MIXED("Blandad betalning"); // Mixed payment (card + cash)

        private final String namn; // The display name for each type

        // Constructor
        BetalningsTyp(String namn) {
            this.namn = namn;
        }

        public String getNamn() {
            return namn;
        }
    }

    // PRIVATE FIELDS - Store all the payment information
    private BetalningsTyp betalningsTyp; // Which payment type (KORT eller KONTANT/MIXED)
    private double originalBelopp; // Original amount before any rounding
    private double slutligtBelopp; // Final amount customer actually pays
    private double avrundning; // Rounding amount
    private double betaltBelopp; // Amount customer actually gave us
    private double vaxel;
    private double kortBelopp; // Amount paid by card (for mixed payments)

    // CONSTRUCTOR 1 - Simple payment (card or exact cash)
    public PaymentMethod(BetalningsTyp typ, double belopp) {
        // Store basic information
        this.betalningsTyp = typ;
        this.originalBelopp = belopp;

        // Initialize all amounts to zero
        this.betaltBelopp = 0.0;
        this.vaxel = 0.0;
        this.kortBelopp = 0.0;

        // Handle different payment types
        if (typ == BetalningsTyp.KONTANT) {
            // Cash payments: Round to nearest krona
            this.slutligtBelopp = Math.round(belopp);
            this.avrundning = this.slutligtBelopp - belopp; // Calculate rounding difference
        } else if (typ == BetalningsTyp.KORT) { // Card payments: Exact amount, no rounding

            this.slutligtBelopp = belopp;
            this.avrundning = 0.0;
            this.kortBelopp = belopp;
        }
    }

    // CONSTRUCTOR 2 - Cash payment with change
    public PaymentMethod(BetalningsTyp typ, double belopp, double betaltBelopp) {
        // Store basic information
        this.betalningsTyp = typ;
        this.originalBelopp = belopp;
        this.betaltBelopp = betaltBelopp;
        this.kortBelopp = 0.0;

        if (typ == BetalningsTyp.KONTANT) {
            this.slutligtBelopp = Math.round(belopp);
            this.avrundning = this.slutligtBelopp - belopp;
            this.vaxel = betaltBelopp - this.slutligtBelopp;
        } else {
            this.slutligtBelopp = belopp;
            this.avrundning = 0.0;
            this.vaxel = 0.0;
            this.kortBelopp = belopp;
        }
    }

    // CONSTRUCTOR 3 - blandad payment
    public PaymentMethod(double totalBelopp, double kortBelopp, double kontantBetalt) {
        this.betalningsTyp = BetalningsTyp.MIXED;
        this.originalBelopp = totalBelopp;
        this.kortBelopp = kortBelopp;
        this.betaltBelopp = kortBelopp + kontantBetalt; // visar sen på kvittot
    }

    // Formatera för kvitto
    public String formateraForKvitto() {
        StringBuilder sb = new StringBuilder();

        sb.append("BETALNINGSMETOD: ").append(betalningsTyp.getNamn()).append("\n");

        if (betalningsTyp == BetalningsTyp.KONTANT) {
            if (vaxel > 0.001) { // Check if change exists
                sb.append(String.format("Originalbelopp:                      %8.2f kr\n", originalBelopp));
                if (Math.abs(avrundning) > 0.001) { // Check if rounding exists -- Kontantbetalning med växel
                    if (avrundning > 0) {
                        sb.append(String.format("Avrundning uppåt:                   +%8.2f kr\n", avrundning));
                    } else {
                        sb.append(String.format("Avrundning nedåt:                    %8.2f kr\n", avrundning));
                    }
                }
                sb.append(String.format("ATT BETALA:                          %8.2f kr\n", slutligtBelopp));
                sb.append(String.format("BETALT:                              %8.2f kr\n", betaltBelopp));
                sb.append(String.format("VÄXEL:                               %8.2f kr\n", vaxel));
                sb.append("BETALNING GODKÄND\n");
            } else if (Math.abs(avrundning) > 0.001) { // Kontantbetalning utan växel men med avrundning
                sb.append(String.format("Originalbelopp:                      %8.2f kr\n", originalBelopp));
                if (avrundning > 0) {
                    sb.append(String.format("Avrundning uppåt:                   +%8.2f kr\n", avrundning));
                } else {
                    sb.append(String.format("Avrundning nedåt:                    %8.2f kr\n", avrundning));
                }
                sb.append(String.format("BETALNING GODKÄND:                   %8.2f kr\n", slutligtBelopp));
            } else {
                sb.append(String.format("BETALNING GODKÄND:                   %8.2f kr\n", slutligtBelopp));
            }
        } else if (betalningsTyp == BetalningsTyp.MIXED) {
            sb.append(String.format("Original belopp:                     %8.2f kr\n", originalBelopp));
            sb.append(String.format("Kontant betalt:                      %8.2f kr\n", betaltBelopp - kortBelopp));

            sb.append(String.format("Betalt med kort:                     %8.2f kr\n", kortBelopp));

            if (Math.abs(avrundning) > 0.001) {
                if (avrundning > 0) {
                    sb.append(String.format("Avrundning uppåt:                   +%8.2f kr\n", avrundning));
                } else {
                    sb.append(String.format("Avrundning nedåt:                    %8.2f kr\n", avrundning));
                }
            }

            if (vaxel > 0.001) {
                sb.append(String.format("VÄXEL:                               %8.2f kr\n", vaxel));
            }

            sb.append("BETALNING GODKÄND\n");
        } else {
            sb.append(String.format("BETALNING GODKÄND (%s):             %9.2f kr\n",
                    betalningsTyp.getNamn().toLowerCase(), slutligtBelopp));
        }

        return sb.toString();
    }

    public static PaymentMethod skapaKortBetalning(double belopp) {
        return new PaymentMethod(BetalningsTyp.KORT, belopp);
    }

    public static PaymentMethod skapaKontantBetalningMedVaxel(double belopp, double betaltBelopp) {
        return new PaymentMethod(BetalningsTyp.KONTANT, belopp, betaltBelopp);
    }

    public static PaymentMethod skapaBlandadBetalning(double totalBelopp, double kortBelopp, double kontantBetalt) {
        return new PaymentMethod(totalBelopp, kortBelopp, kontantBetalt);
    }
}
