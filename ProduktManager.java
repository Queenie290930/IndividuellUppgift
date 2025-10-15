import java.io.*;        
import java.util.*;      


//PRODUKTMANAGER CLASS - The "Product Database" of our cash register
public class ProduktManager { 
    
    // PRIVATE FIELDS - The data storage for our product manager
    private ArrayList<Produkt> allaProdukter;     
    private HashMap<String, Produkt> produktMap;  
    private static final String PRODUKT_FIL = "Product.txt";  
    
    
    // CONSTRUCTOR - This runs automatically when we create a new ProduktManager
    public ProduktManager() {
        this.allaProdukter = new ArrayList<>();   
        this.produktMap = new HashMap<>();        
    
        laddaProdukter(); // This method reads Product.txt and fills our containers
    }

 
     //This method tries to read products from file
    public void laddaProdukter() { 
        try {          
            // Try to read products from the file
            lasProduktFranFil(); 
            
            // If successful, tell us how many products were loaded
            System.out.println("Laddade " + allaProdukter.size() + " produkter från " + PRODUKT_FIL);
            
        } catch (FileNotFoundException e) { 
            // If the Product.txt file doesn't exist
            System.err.println("Kunde inte hitta " + PRODUKT_FIL + ": " + e.getMessage());
            skapaStandardProdukter();  // Create some default products instead
            
        } catch (Exception e) { 
            // If any other error happens (corrupted file, permission issues, etc.)
            System.err.println("Fel vid läsning av produkter: " + e.getMessage());
            skapaStandardProdukter();  // Create some default products instead
        }
    }
          
     //This method opens Product.txt and reads it line by line
    private void lasProduktFranFil() throws FileNotFoundException {     
        File fil = new File(PRODUKT_FIL);          
        Scanner scanner = new Scanner(fil);        
        
        allaProdukter.clear();  //Clear any existing products from memory       
        produktMap.clear();
        
        int radNummer = 0;        // Keep track of line numbers for better error messages

        while (scanner.hasNextLine()) {     //Read the file line by line     
            radNummer++;
            String rad = scanner.nextLine().trim();   // Get next line and remove extra spaces
            
            if (rad.isEmpty() || rad.startsWith("#")) {   //Skip empty lines and comment lines (starting with #)
                continue;  // Go to next line
            }
            
            try {
                Produkt produkt = parseProduktRad(rad);    // Convert line to product
                if (produkt != null) {
                    // Add the product to both our list and our quick-lookup map
                    allaProdukter.add(produkt);   
                    produktMap.put(produkt.getNamn(), produkt);
                }
            } catch (Exception e) {
                // If this line has an error, show which line has the problem but continue reading
                System.err.println("Fel på rad " + radNummer + " i " + PRODUKT_FIL + ": " + e.getMessage());  
            }
        }
        scanner.close();  // Always close the file when done
        
        if (allaProdukter.isEmpty()) {
            throw new RuntimeException("Inga giltiga produkter hittades i filen");
        }
    }
        
    //PARSE PRODUCT LINE - Convert one line from file into a Produkt object
     //This method takes a line like "Kaffe:29.50:25" and breaks it apart:
    private Produkt parseProduktRad(String rad) {   
        String[] delar = rad.split(":");    

        //Check if line has correct number of parts (2 or 3)
        if (delar.length < 2 || delar.length > 3) {   
            throw new IllegalArgumentException("Felaktigt format. Förväntat format: 'namn:pris' eller 'namn:pris:moms'");
        }
        
        //Get the product name (first part)
        String namn = delar[0].trim();  
        if (namn.isEmpty()) {      
            throw new IllegalArgumentException("Produktnamn kan inte vara tomt");
        }
        
        // Try to convert price (and tax) from text to numbers
        try {
            double pris = Double.parseDouble(delar[1].trim());  // Get price (second part)
            
            // Check if tax is provided (third part)
            if (delar.length == 3) {   
                double moms = Double.parseDouble(delar[2].trim());  // Use provided tax rate
                return new Produkt(namn, pris, moms);
            } else {
                return new Produkt(namn, pris, 0);  // Use default tax (0)
            }
        } catch (NumberFormatException e) {
            // If price or tax is not a valid number
            throw new IllegalArgumentException("Ogiltigt pris eller moms: '" + rad + "'");  
        }
    }
    
    
     //CREATE DEFAULT PRODUCTS - Fallback when file reading fails
    private void skapaStandardProdukter() {  
        System.out.println("Skapar standardprodukter...");
        
        // Clear any existing products
        allaProdukter.clear();  
        produktMap.clear();
        
        //array with some basic default products
        Produkt[] standardProdukter = {   
            new Produkt("Kaffe", 29.50, 12),
            new Produkt("Kaka", 22.00, 12)     
        };
        
        // Add each default product to both our list and map
        for (Produkt produkt : standardProdukter) {   
            allaProdukter.add(produkt);
            produktMap.put(produkt.getNamn(), produkt);
        }
    }
    
    
    // GETTER METHODS - These let other classes access our product information
    public Produkt getProdukt(String namn) {
        return produktMap.get(namn);
    }

    
    //GET ALL PRODUCT NAMES - Return a list of all available product names
    public Set<String> getAllaProduktNamn() {      
        return new HashSet<>(produktMap.keySet());  // Return copy of all product names
    }
}