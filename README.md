
# Individuell Uppgift – Beginner-Friendly Cash Register System

<img width="700" height="498" alt="Skärmavbild 2025-10-15 kl  20 52 18" src="https://github.com/user-attachments/assets/e2b22dc3-4387-4308-8dd4-e4b6c799940a" />


This is a simple Java project for managing products and receipts, with a graphical user interface (GUI). It is perfect for beginners who want to learn Java and see how a real world program works. You can test, click, and explore without breaking anything!

---

## Special Features

- **Robust Error Handling:** No matter what goes wrong, like missing files, wrong input, or unexpected problems, my program catches the error, shows a helpful message, and keeps running smoothly. You never have to worry about crashes!
  
- **Always Ready to Work:** Even if the product file is missing or broken, the program makes some basic products so you can still use the cash register.
  
- **Easy to Add Products:** Just write a new line in `Product.txt` and a new button will show up. No need to change the code! The product buttons are created automatically from the file.
  
- **Clear Messages:** If you do something wrong, the program tells you what happened and how to fix it.
  
- **Edit and Remove Buttons:** You can easily change or delete products and receipt items using simple buttons.
  
- **Beautiful and Realistic Cash Register:** The system looks nice and works almost like a real cash register.
  
- **Mixed Payment:** If you pay with cash and it’s not enough, you can pay the rest with a card.
  
- **Smart Rounding:** When you pay with cash, the amount is rounded off. If you pay the rest with a card, only the cash part is rounded, and the card part is exact.
  
- **Automatic Change Calculation:** If a customer pays more than the total amount, the system will calculate the change and show it on the screen. This makes it easier for the cashier.

## Features
- Add, edit, and remove products
- Handle different payment methods
- Manage receipts
- Easy-to-use GUI

## How it Works
1. Add products to `Product.txt` or use the default ones.
2. Start the program and select products by clicking their buttons.
3. Edit or remove items from the receipt if needed.
4. Choose how to pay (cash, card, or mixed).
5. The system will show the total, handle rounding, and calculate any change.
6. See your receipt on the screen.

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
- `ReceiptRemoveHandler.java`: Lets you remove or delete a receipt item.

## Troubleshooting
- If the program doesn’t start, make sure you have Java installed.
- If you add a product but don’t see a button, check that you saved `Product.txt` and restarted the program.
- If you have any other problems, read the error message on the screen, it will help you fix it!

## Author
- Queenie Moradas
- Date: October 2025

---
This project is for learning and practicing Java programming. If you are new to Java, try reading the code and running the program to see how it works!

**Note:** Some variable names in the code are in Swedish, but they are simple and easy to understand. The code is still clear and easy to understand!

