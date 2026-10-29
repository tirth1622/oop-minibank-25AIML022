package minibank.demo;

import minibank.model.Account;
import minibank.model.CurrentAccount;
import minibank.model.FixedDepositAccount;
import minibank.model.SavingsAccount;

/**
 * Demonstration of Practical 5 - Part B:
 * - Abstract Account class hierarchy
 * - Run-time polymorphism with interestRate() across subtypes
 * - Pattern matching instanceof to handle specific types
 */
public class Lab05Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   PRACTICAL 5 (PART B) DEMONSTRATION: RUN-TIME POLYMORPHISM   ");
        System.out.println("===============================================================");

        // 6. Account[] array containing the three subtypes
        Account[] accounts = new Account[]{
                new SavingsAccount("Riya", 10000, 1000),
                new CurrentAccount("Aarav", 25000, 5000),
                new FixedDepositAccount("Ananya", 50000, 12)
        };

        System.out.println("Looping through Account[] and calling interestRate() polymorphically:\n");
        for (Account a : accounts) {
            System.out.printf("  Account %-6s | Type: %-13s | Owner: %-8s | Interest Rate: %4.1f%%%n",
                    a.getAccountNumber(), a.getAccountType(), a.getOwnerName(), a.interestRate());

            // Pattern matching instanceof check to handle one type specially
            if (a instanceof CurrentAccount ca) {
                System.out.println("    -> Special Check: CurrentAccount detected with Overdraft Limit: Rs. " + ca.getOverdraftLimit());
            } else if (a instanceof FixedDepositAccount fda) {
                System.out.println("    -> Special Check: FixedDepositAccount locked until maturity: " + fda.getMaturityDate());
            }
        }

        System.out.println("\nExpected Output Verification:");
        System.out.println("  Savings Interest Rate       : " + accounts[0].interestRate() + "% (Expected: 4.0%)");
        System.out.println("  Current Interest Rate       : " + accounts[1].interestRate() + "% (Expected: 0.0%)");
        System.out.println("  Fixed Deposit Interest Rate : " + accounts[2].interestRate() + "% (Expected: 7.0%)");

        System.out.println("\n[Practical 5 Part B Completed Successfully]");
    }
}
