package minibank.demo;

import minibank.model.Account;
import minibank.model.SavingsAccount;
import minibank.model.interfaces.InterestBearing;
import minibank.model.interfaces.Premium;
import minibank.model.interfaces.Transactable;
import minibank.model.interfaces.WithdrawRule;

import static minibank.util.Validator.isValidMobile;

/**
 * Demonstration of Practical 6 - Part B:
 * - Interface capabilities (Transactable, InterestBearing)
 * - Functional interface WithdrawRule implemented as Anonymous Class AND Lambda
 * - Marker interface Premium check
 * - Package structure and static imports
 */
public class Lab06Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("    PRACTICAL 6 (PART B) DEMONSTRATION: INTERFACES & LAMBDAS   ");
        System.out.println("===============================================================");

        SavingsAccount account = new SavingsAccount("AC0001", "Riya Sharma", 15000, true, 2000);

        // 1 & 2. Transactable and InterestBearing
        System.out.println("1. Interface Capabilities:");
        System.out.println("   account instanceof Transactable    : " + (account instanceof Transactable));
        System.out.println("   account instanceof InterestBearing : " + (account instanceof InterestBearing));
        System.out.println("   Default yearlyInterest()           : Rs. " + account.yearlyInterest());
        System.out.println("   Default monthlyInterestEstimate()  : Rs. " + account.monthlyInterestEstimate());

        // 3. WithdrawRule with Anonymous Class
        WithdrawRule anonymousRule = new WithdrawRule() {
            @Override
            public boolean allow(Account acc, long amount) {
                // Disallow single withdrawals larger than 50% of current balance
                return amount <= (acc.getBalance() / 2);
            }
        };

        // 3. WithdrawRule with Lambda Expression
        WithdrawRule lambdaRule = (acc, amount) -> amount <= (acc.getBalance() / 2);

        System.out.println("\n2. Testing WithdrawRule Functional Interface:");
        long testWithdrawal = 5000;
        boolean allowedAnon = anonymousRule.allow(account, testWithdrawal);
        boolean allowedLambda = lambdaRule.allow(account, testWithdrawal);

        System.out.println("   Testing withdrawal of Rs. " + testWithdrawal + " on balance of Rs. " + account.getBalance());
        System.out.println("   Anonymous Class Rule allow() : " + allowedAnon + " (Expected: true)");
        System.out.println("   Lambda Expression Rule allow(): " + allowedLambda + " (Expected: true)");

        long largeWithdrawal = 12000;
        System.out.println("   Testing large withdrawal of Rs. " + largeWithdrawal);
        System.out.println("   Anonymous Class Rule allow() : " + anonymousRule.allow(account, largeWithdrawal) + " (Expected: false)");
        System.out.println("   Lambda Expression Rule allow(): " + lambdaRule.allow(account, largeWithdrawal) + " (Expected: false)");

        // 4. Marker interface
        System.out.println("\n3. Marker Interface Check:");
        System.out.println("   account instanceof Premium: " + (account instanceof Premium));

        // 5. Static import demonstration
        System.out.println("\n4. Static Import Test (Validator.isValidMobile without class prefix):");
        System.out.println("   isValidMobile(\"9876543210\") : " + isValidMobile("9876543210"));

        System.out.println("\n[Practical 6 Part B Completed Successfully]");
    }
}
