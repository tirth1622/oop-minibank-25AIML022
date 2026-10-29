package minibank.demo;

import minibank.model.BankInfo;
import minibank.model.MenuOption;

import java.util.Scanner;

/**
 * Demonstration of Practical 1 - Part B:
 * - BankInfo record header
 * - MenuOption enum (OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT)
 * - Numbered menu selection using Scanner
 * - Switch expression placeholder mapping
 */
public class Lab01Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("       PRACTICAL 1 (PART B) DEMONSTRATION: MENU SHELL          ");
        System.out.println("===============================================================");

        // 3. Define and print BankInfo record
        BankInfo bankInfo = new BankInfo("MiniBank India", "Central Branch");
        System.out.println(bankInfo.formattedHeader());

        // 4 & 5. Display menu
        String simulatedInput = "2\n5\n"; // Choose Deposit, then Exit
        Scanner scanner = new Scanner(simulatedInput);

        boolean looping = true;
        while (looping) {
            System.out.println("\n--- Practical 1 Menu ---");
            System.out.println("1. OPEN_ACCOUNT");
            System.out.println("2. DEPOSIT");
            System.out.println("3. WITHDRAW");
            System.out.println("4. TRANSFER");
            System.out.println("5. EXIT");
            System.out.print("Select an option (1-5): ");

            int choice = scanner.nextInt();

            // 6. Switch expression printing placeholder
            String message = switch (choice) {
                case 1 -> "Open Account — to be implemented in a later lab";
                case 2 -> "Deposit — to be implemented in a later lab";
                case 3 -> "Withdraw — to be implemented in a later lab";
                case 4 -> "Transfer — to be implemented in a later lab";
                case 5 -> {
                    looping = false;
                    yield "Exiting MiniBank. Goodbye!";
                }
                default -> "Invalid selection! Please enter a valid number (1-5).";
            };

            System.out.println("Result -> " + message);
        }
        System.out.println("\n[Practical 1 Part B Completed Successfully]");
    }
}
