package minibank.demo;

import minibank.model.Account;
import minibank.service.BankService;
import minibank.util.AuditLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstration of Practical 14 - Part B:
 * - Full Capstone Assembly & BankService integration
 * - Layered architecture verification (Model -> Repository -> Service -> UI)
 * - Safe concurrent batch transactions over thread pool
 * - Reflection audit logging
 */
public class Lab14Demo {
    public static void main(String[] args) throws Exception {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 14 (PART B) DEMONSTRATION: CAPSTONE INTEGRATION    ");
        System.out.println("===============================================================");

        BankService bankService = new BankService();

        // 1. Open Accounts through BankService with regex validation
        System.out.println("1. Opening Accounts with Full Validation Layer:");
        Account a1 = bankService.openAccount("Riya Sharma", "SAVINGS", 8000,
                "riya.sharma@bank.com", "9876543210", "ABCDE1234F", "MINI0123456");
        Account a2 = bankService.openAccount("Aarav Patel", "CURRENT", 15000,
                "aarav.patel@bank.com", "9812345678", "XYZPQ9876R", "MINI0123456");
        System.out.println("   Opened: " + a1);
        System.out.println("   Opened: " + a2);

        // 2. Perform Transactions
        System.out.println("\n2. Executing Real Business Operations:");
        bankService.deposit(a1.getAccountNumber(), 2000);
        System.out.println("   Deposited Rs. 2000 into " + a1.getAccountNumber() + ". Balance: Rs. " + a1.getBalance());

        bankService.withdraw(a2.getAccountNumber(), 5000);
        System.out.println("   Withdrew Rs. 5000 from " + a2.getAccountNumber() + ". Balance: Rs. " + a2.getBalance());

        bankService.transfer(a1.getAccountNumber(), a2.getAccountNumber(), 1500);
        System.out.println("   Transferred Rs. 1500 from " + a1.getAccountNumber() + " to " + a2.getAccountNumber());

        // 3. Statement formatting
        System.out.println("\n3. Generating Statement via StatementFormatter:");
        System.out.println(bankService.statement(a1.getAccountNumber()));

        // 4. Batch Transaction Engine
        System.out.println("\n4. Running Batch Processing Engine over Thread Pool (4 Threads):");
        List<Runnable> batch = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            final int id = i;
            batch.add(() -> {
                try {
                    bankService.deposit(a1.getAccountNumber(), 100);
                    System.out.println("   Batch task " + id + " executed on " + Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        bankService.processBatch(batch);
        System.out.println("   Updated Balance after Batch Deposits: Rs. " + a1.getBalance());

        // 5. Generate End of Day report
        System.out.println("\n5. Generating EOD Summary Report via NIO:");
        String report = bankService.generateReport();
        System.out.println(report);

        // 6. Reflection audit trail
        System.out.println("\n6. Reflective @Audit Log Summary:");
        AuditLogger.printAuditSummary();

        // 7. Persist state
        bankService.saveState();
        System.out.println("\n[Practical 14 Part B Capstone Completed Successfully]");
    }
}
