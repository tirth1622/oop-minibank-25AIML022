package minibank.demo;

import minibank.model.Account;
import minibank.model.SavingsAccount;
import minibank.service.AccountStore;

import java.util.List;

/**
 * Demonstration of Practical 12 - Part B:
 * - AccountStore using HashMap<String, Account>
 * - ArrayList transaction history
 * - Sorting accounts with Comparator by balance (descending: 9000, 5000, 2000)
 * - Comparable natural ordering
 */
public class Lab12Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 12 (PART B) DEMONSTRATION: COLLECTIONS & SORTING   ");
        System.out.println("===============================================================");

        AccountStore store = new AccountStore();

        // 1 & 6. Add three accounts with balances 5000, 2000, and 9000
        Account a1 = new SavingsAccount("AC0001", "Riya", 5000, true, 1000);
        Account a2 = new SavingsAccount("AC0002", "Aarav", 2000, true, 1000);
        Account a3 = new SavingsAccount("AC0003", "Ananya", 9000, true, 1000);

        store.add(a1);
        store.add(a2);
        store.add(a3);
        System.out.println("1. Added 3 accounts to AccountStore (HashMap):");
        System.out.println("   AC0001 (Rs. 5000), AC0002 (Rs. 2000), AC0003 (Rs. 9000)");

        // Look up by account number
        System.out.println("\n2. Testing Lookup by Account Number:");
        Account fetched = store.get("AC0002");
        System.out.println("   get(\"AC0002\") -> " + fetched);

        // 3. Sort by balance descending with Comparator
        System.out.println("\n3. Testing sortedByBalance() with Comparator (Highest First):");
        List<Account> sortedList = store.sortedByBalance();
        for (Account a : sortedList) {
            System.out.printf("   Acc: %-6s | Owner: %-8s | Balance: Rs. %-6d%n",
                    a.getAccountNumber(), a.getOwnerName(), a.getBalance());
        }

        System.out.println("\n   Verification of Expected Output Order: 9000, 5000, 2000");
        boolean correctOrder = sortedList.get(0).getBalance() == 9000 &&
                               sortedList.get(1).getBalance() == 5000 &&
                               sortedList.get(2).getBalance() == 2000;
        System.out.println("   Order check matches expected: " + correctOrder);

        // 4. Comparable compareTo check
        System.out.println("\n4. Testing Comparable<Account> natural ordering by account number:");
        System.out.println("   a1.compareTo(a2) -> " + a1.compareTo(a2) + " (AC0001 before AC0002)");

        System.out.println("\n[Practical 12 Part B Completed Successfully]");
    }
}
