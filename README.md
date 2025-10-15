# IndividuellUppgift

This is a simple Java project for managing products and receipts, with a graphical user interface (GUI).

## Features
- Add, edit, and remove products
- Handle different payment methods
- Manage receipts
- Easy-to-use GUI

## How to Run
1. Make sure you have Java installed on your computer.
2. Open a terminal in this project folder.
3. Compile the code with:
	```
	javac *.java
	```
4. Run the program with:
	```
	java Main
	```

## Main Files
- `Main.java`: Starts the program.
- `GUI.java`: Handles the graphical user interface.
- `Produkt.java`: Represents a product.
- `ProduktManager.java`: Manages the list of products.
 - `CashRegister.java`: Handles payments and coordinates with all classes to bring everything together.
- `Receipt.java`: Represents a receipt.
- `ReceiptRow.java`: Represents a row in a receipt.
- `PaymentMethod.java`: Handles different ways to pay.

- `ButtonAction.java`: Handles what happens when you click a button in the GUI.
- `MixedPaymentHandler.java`: Lets you use more than one way to pay at the same time.
- `Product.txt`: A text file that stores product information.
- `ProductButtonAction.java`: Handles actions when you click a product button.
- `ReceiptEditHandler.java`: Lets you change or edit a receipt.
- `ReceiptManagementAction.java`: Helps manage receipts (like viewing or organizing them).
- `ReceiptRemoveHandler.java`: Lets you remove or delete a receipt.

## Author
- Queenie Moradas
- Date: October 2025

---
This project is for learning and practicing Java programming. If you are new to Java, try reading the code and running the program to see how it works!
