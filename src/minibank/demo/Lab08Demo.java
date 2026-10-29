package minibank.demo;

import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.Account;
import minibank.model.SavingsAccount;
import minibank.util.BankingSession;

/**
 * Demonstration of Practical 8 - Part B:
 * - Custom checked exception hierarchy (BankException, InsufficientFundsException with shortfall)
 * - Structured exception handling with try-catch-finally
 * - AutoCloseable resource using try-with-resources
 */
public class Lab08Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 8 (PART B) DEMONSTRATION: EXCEPTIONS & ROBUSTNESS ");
        System.out.println("===============================================================");

        Account accountA = new SavingsAccount("AC0001", "Riya Sharma", 1000, true, 0);
        Account accountB = new SavingsAccount("AC0002", "Aarav Patel", 2000, true, 0);

        // 4. Try-with-resources demonstration
        try (BankingSession session = new BankingSession("DEMO-SESSION-008")) {

            // 1 & 2. Insufficient funds with shortfall
            System.out.println("\n1. Testing InsufficientFundsException (Balance: 1000, Withdraw: 5000):");
            try {
                accountA.withdraw(5000);
            } catch (InsufficientFundsException e) {
                System.out.println("   Caught Expected Exception: " + e.getMessage());
                System.out.println("   Stored Shortfall Amount  : Rs. " + e.getShortfall() + " (Expected: 4000)");
                System.out.println("   Balance Remains          : Rs. " + accountA.getBalance() + " (Expected: 1000)");
            } catch (BankException e) {
                System.out.println("   Generic BankException: " + e.getMessage());
            }

            // Test non-positive amount exception
            System.out.println("\n2. Testing InvalidAmountException (Deposit: -200):");
            try {
                accountA.deposit(-200);
            } catch (InvalidAmountException e) {
                System.out.println("   Caught Expected Exception: " + e.getMessage());
            }

            // 3. Transfer with try-catch-finally
            System.out.println("\n3. Testing Atomic Transfer (Transfer Rs. 500 from AC0001 to AC0002):");
            try {
                Account.transfer(accountA, accountB, 500);
                System.out.println("   Transfer Successful!");
                System.out.println("   AC0001 Balance: Rs. " + accountA.getBalance() + " (Expected: 500)");
                System.out.println("   AC0002 Balance: Rs. " + accountB.getBalance() + " (Expected: 2500)");
            } catch (BankException e) {
                System.out.println("   Transfer Failed: " + e.getMessage());
            } finally {
                System.out.println("   [finally] Transfer transaction block completed.");
            }
        } // BankingSession closes automatically here!

        System.out.println("\n[Practical 8 Part B Completed Successfully]");
    }
}
