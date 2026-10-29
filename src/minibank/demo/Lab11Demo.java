package minibank.demo;

import minibank.model.Account;
import minibank.model.CurrentAccount;
import minibank.model.FixedDepositAccount;
import minibank.model.SavingsAccount;
import minibank.persistence.ReportGenerator;
import minibank.persistence.StatePersister;
import minibank.persistence.TransactionLog;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Demonstration of Practical 11 - Part B:
 * - Object serialization / deserialization using StatePersister
 * - Append-only transaction logging with NIO Files.write()
 * - ReportGenerator walking directory with DirectoryStream, BufferedReader, and FileAttributes
 */
public class Lab11Demo {
    public static void main(String[] args) throws Exception {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 11 (PART B) DEMONSTRATION: PERSISTENCE & REPORTING ");
        System.out.println("===============================================================");

        Path dataDir = Paths.get("data");
        Path logsDir = Paths.get("logs");
        if (!Files.exists(dataDir)) Files.createDirectories(dataDir);
        if (!Files.exists(logsDir)) Files.createDirectories(logsDir);

        Path accountsFile = dataDir.resolve("demo_accounts.dat");
        Path txLogFile = logsDir.resolve("demo_transactions.log");
        Path reportFile = logsDir.resolve("demo_summary_report.txt");

        // 1 & 2. State serialization test
        System.out.println("1. Testing Object Serialization with StatePersister:");
        Account[] original = new Account[]{
                new SavingsAccount("AC0001", "Riya Sharma", 5000, true, 1000),
                new CurrentAccount("AC0002", "Aarav Patel", 12000, true, 5000),
                new FixedDepositAccount("AC0003", "Ananya Joshi", 30000, true, null, 12)
        };

        System.out.println("   Saving 3 accounts to " + accountsFile + "...");
        StatePersister.save(original, accountsFile);

        System.out.println("   Reloading accounts from disk...");
        Account[] reloaded = StatePersister.load(accountsFile);
        System.out.println("   Reloaded " + reloaded.length + " accounts:");
        for (Account a : reloaded) {
            System.out.println("     - " + a);
        }

        // 3. TransactionLog append
        System.out.println("\n2. Testing Append-Only TransactionLog:");
        TransactionLog.append(txLogFile, "DEPOSIT AC0001 500");
        TransactionLog.append(txLogFile, "WITHDRAW AC0002 200");
        TransactionLog.append(txLogFile, "DEPOSIT AC0003 1500");
        System.out.println("   Appended 3 sample transaction records to " + txLogFile);

        // 4. ReportGenerator with DirectoryStream and BufferedReader
        System.out.println("\n3. Testing ReportGenerator via NIO DirectoryStream:");
        String report = ReportGenerator.generateReport(logsDir, reportFile);
        System.out.println(report);

        System.out.println("\n[Practical 11 Part B Completed Successfully]");
    }
}
