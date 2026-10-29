package minibank.demo;

import minibank.model.Account;
import minibank.model.Customer;
import minibank.model.SavingsAccount;

/**
 * Demonstration of Practical 2 - Part B:
 * - Customer with auto-generated ID (e.g. CUST101)
 * - Account encapsulation (balance altered only via deposit/withdraw, no public setter)
 * - Account[] array with multiple instances and state modifications
 */
public class Lab02Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   PRACTICAL 2 (PART B) DEMONSTRATION: DOMAIN & ENCAPSULATION  ");
        System.out.println("===============================================================");

        // 1. Customer instances
        Customer c1 = new Customer("Riya Sharma", "riya@example.com", "9876543210");
        Customer c2 = new Customer("Aarav Patel", "aarav@example.com", "9812345678");
        System.out.println("Customer 1 ID: " + c1.getCustomerId() + " (" + c1.getName() + ")");
        System.out.println("Customer 2 ID: " + c2.getCustomerId() + " (" + c2.getName() + ")");

        // 2 & 5. Account[] array of three accounts
        Account[] accounts = new Account[3];
        accounts[0] = new SavingsAccount("Riya", 5000, 1000);
        accounts[1] = new SavingsAccount("Aarav", 8000, 1000);
        accounts[2] = new SavingsAccount("Ananya", 3000, 1000);

        System.out.println("\nInitial State:");
        for (Account acc : accounts) {
            System.out.println("  " + acc.getAccountNumber() + " | " + acc.getOwnerName() + " | Balance: " + acc.getBalance());
        }

        System.out.println("\nTesting Expected Output Sequence on " + accounts[0].getAccountNumber() + " (Opening: 5000):");
        try {
            // deposit(2000)
            accounts[0].deposit(2000);
            System.out.println("  After deposit(2000)  -> Balance: " + accounts[0].getBalance() + " (Expected: 7000)");

            // withdraw(3000)
            boolean w1 = accounts[0].withdraw(3000);
            System.out.println("  After withdraw(3000) -> Status: " + w1 + ", Balance: " + accounts[0].getBalance() + " (Expected: 4000)");

            // withdraw(10000) - exceeds balance
            try {
                accounts[0].withdraw(10000);
            } catch (Exception e) {
                System.out.println("  After withdraw(10000)-> Failed as expected: " + e.getMessage() + ", Balance remains: " + accounts[0].getBalance() + " (Expected: 4000)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n[Practical 2 Part B Completed Successfully]");
    }
}
