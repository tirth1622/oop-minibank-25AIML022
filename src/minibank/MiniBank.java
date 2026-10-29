package minibank;

import minibank.exception.BankException;
import minibank.model.Account;
import minibank.model.BankInfo;
import minibank.model.MenuOption;
import minibank.model.SavingsAccount;
import minibank.service.BankService;
import minibank.util.AnnotationValidator;
import minibank.util.BankingSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Practicals 1 to 14: Main interactive console entry point for the MiniBank application.
 * Bridges the console UI to the BankService business logic, persistence, and concurrency layers.
 */
public class MiniBank {

    private static final BankInfo BANK_INFO = new BankInfo("State Bank of MiniBank", "Main Campus Cyber Branch");
    private static final BankService bankService = new BankService();

    public static void main(String[] args) {
        // Practical 8: Try-with-resources wrapping session lifecycle
        try (BankingSession session = new BankingSession("SESSION-" + System.currentTimeMillis())) {
            System.out.println(BANK_INFO.formattedHeader());
            ensureInitialSeedData();

            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                printMenu();
                System.out.print("Enter your choice (1-11): ");
                String input = scanner.nextLine().trim();

                int choiceNumber;
                try {
                    choiceNumber = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("(!) Invalid input. Please enter a valid number between 1 and 11.\n");
                    continue;
                }

                MenuOption option = MenuOption.fromNumber(choiceNumber);
                if (option == null) {
                    System.out.println("(!) Unknown menu choice. Please select an option from 1 to 11.\n");
                    continue;
                }

                // Practical 1: Switch expression handling menu choices
                switch (option) {
                    case OPEN_ACCOUNT -> handleOpenAccount(scanner);
                    case DEPOSIT -> handleDeposit(scanner);
                    case WITHDRAW -> handleWithdraw(scanner);
                    case TRANSFER -> handleTransfer(scanner);
                    case STATEMENT -> handleStatement(scanner);
                    case LIST_ACCOUNTS -> handleListAccounts();
                    case BATCH_PROCESSING -> handleBatchProcessing();
                    case EOD_REPORT -> handleReport();
                    case WORKING_HOURS -> printWorkingHours();
                    case RUN_DIAGNOSTICS -> runDiagnostics();
                    case EXIT -> {
                        running = false;
                        handleExit();
                    }
                }
                System.out.println();
            }
        }
    }

    private static void printMenu() {
        System.out.println("==================== MAIN MENU ====================");
        for (MenuOption opt : MenuOption.values()) {
            System.out.printf("  [%2d] %s%n", opt.getOptionNumber(), opt.getDescription());
        }
        System.out.println("===================================================");
    }

    private static void handleOpenAccount(Scanner scanner) {
        System.out.println("\n--- [1] OPEN NEW ACCOUNT ---");
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter 10-digit Mobile Number: ");
        String mobile = scanner.nextLine().trim();

        System.out.print("Enter PAN Number (e.g. ABCDE1234F): ");
        String pan = scanner.nextLine().trim();

        System.out.print("Enter IFSC Code (e.g. MINI0123456): ");
        String ifsc = scanner.nextLine().trim();

        System.out.print("Account Type (SAVINGS / CURRENT / FIXED_DEPOSIT): ");
        String type = scanner.nextLine().trim();

        System.out.print("Initial Deposit Amount (Rs.): ");
        String amtStr = scanner.nextLine().trim();

        try {
            long initialBalance = Long.parseLong(amtStr);
            Account account = bankService.openAccount(name, type, initialBalance, email, mobile, pan, ifsc);
            System.out.println("Account opened successfully!");
            System.out.println("Generated Account Number : " + account.getAccountNumber());
            System.out.println("Account Type             : " + account.getAccountType());
            System.out.println("Initial Balance          : Rs. " + account.getBalance());
            System.out.println("Interest Rate            : " + account.interestRate() + "%");
        } catch (NumberFormatException e) {
            System.out.println("(!) Invalid amount. Please enter a whole integer.");
        } catch (BankException e) {
            System.out.println("(!) Account Creation Failed: " + e.getMessage());
        }
    }

    private static void handleDeposit(Scanner scanner) {
        System.out.println("\n--- [2] DEPOSIT FUNDS ---");
        System.out.print("Enter Account Number (e.g. AC0001): ");
        String accNum = scanner.nextLine().trim();

        System.out.print("Enter Deposit Amount (Rs.): ");
        String amtStr = scanner.nextLine().trim();

        try {
            long amount = Long.parseLong(amtStr);
            bankService.deposit(accNum, amount);
            System.out.println("Deposit successful!");
            System.out.println(bankService.statement(accNum));
        } catch (NumberFormatException e) {
            System.out.println("(!) Amount must be a valid whole number.");
        } catch (BankException e) {
            System.out.println("(!) Deposit Failed: " + e.getMessage());
        }
    }

    private static void handleWithdraw(Scanner scanner) {
        System.out.println("\n--- [3] WITHDRAW FUNDS ---");
        System.out.print("Enter Account Number (e.g. AC0001): ");
        String accNum = scanner.nextLine().trim();

        System.out.print("Enter Withdrawal Amount (Rs.): ");
        String amtStr = scanner.nextLine().trim();

        try {
            long amount = Long.parseLong(amtStr);
            bankService.withdraw(accNum, amount);
            System.out.println("Withdrawal successful!");
            System.out.println(bankService.statement(accNum));
        } catch (NumberFormatException e) {
            System.out.println("(!) Amount must be a valid whole number.");
        } catch (BankException e) {
            System.out.println("(!) Withdrawal Failed: " + e.getMessage());
        }
    }

    private static void handleTransfer(Scanner scanner) {
        System.out.println("\n--- [4] TRANSFER FUNDS ---");
        System.out.print("Enter Source Account Number (From): ");
        String fromAcc = scanner.nextLine().trim();

        System.out.print("Enter Target Account Number (To): ");
        String toAcc = scanner.nextLine().trim();

        System.out.print("Enter Transfer Amount (Rs.): ");
        String amtStr = scanner.nextLine().trim();

        try {
            long amount = Long.parseLong(amtStr);
            bankService.transfer(fromAcc, toAcc, amount);
            System.out.println("Transfer completed successfully!");
            System.out.println("Updated Source Account:");
            System.out.println(bankService.statement(fromAcc));
        } catch (NumberFormatException e) {
            System.out.println("(!) Amount must be a valid whole number.");
        } catch (BankException e) {
            System.out.println("(!) Transfer Failed: " + e.getMessage());
        }
    }

    private static void handleStatement(Scanner scanner) {
        System.out.println("\n--- [5] VIEW ACCOUNT STATEMENT ---");
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine().trim();

        try {
            String statement = bankService.statement(accNum);
            System.out.println(statement);
        } catch (BankException e) {
            System.out.println("(!) " + e.getMessage());
        }
    }

    private static void handleListAccounts() {
        System.out.println("\n--- [6] ALL ACTIVE ACCOUNTS (SORTED BY BALANCE) ---");
        List<Account> accounts = bankService.listAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts registered yet.");
            return;
        }

        System.out.printf("%-10s | %-16s | %-12s | %-12s | %-10s%n",
                "Acc No", "Owner Name", "Type", "Balance (Rs)", "Int. Rate");
        System.out.println("------------------------------------------------------------------");
        for (Account a : accounts) {
            System.out.printf("%-10s | %-16s | %-12s | Rs. %-8d | %4.1f%%%n",
                    a.getAccountNumber(), a.getOwnerName(), a.getAccountType(), a.getBalance(), a.interestRate());
        }
    }

    private static void handleBatchProcessing() {
        System.out.println("\n--- [7] CONCURRENT BATCH TRANSACTION ENGINE (4 THREADS) ---");
        List<Account> list = bankService.listAccounts();
        if (list.isEmpty()) {
            System.out.println("(!) Please create at least one account first.");
            return;
        }

        Account target = list.get(0);
        System.out.println("Submitting 10 concurrent tasks of Rs. 100 deposits to account " + target.getAccountNumber() + "...");

        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final int taskId = i + 1;
            tasks.add(() -> {
                try {
                    bankService.deposit(target.getAccountNumber(), 100);
                    System.out.println("  [Thread Pool Task " + taskId + "] Completed Rs. 100 deposit on " + Thread.currentThread().getName());
                } catch (Exception e) {
                    System.err.println("  Task failed: " + e.getMessage());
                }
            });
        }

        bankService.processBatch(tasks);
        System.out.println("Batch processing completed cleanly!");
        try {
            System.out.println("Updated balance for " + target.getAccountNumber() + ": Rs. " + target.getBalance());
        } catch (Exception ignored) {
        }
    }

    private static void handleReport() {
        System.out.println("\n--- [8] GENERATE END-OF-DAY RECONCILIATION REPORT (NIO) ---");
        try {
            String report = bankService.generateReport();
            System.out.println(report);
            System.out.println("Report saved to logs/eod_reconciliation_report.txt");
        } catch (IOException e) {
            System.out.println("(!) Failed to generate report: " + e.getMessage());
        }
    }

    private static void printWorkingHours() {
        System.out.println("""
                
                ================ BANK WORKING HOURS ================
                  Monday - Friday : 09:00 AM - 04:00 PM
                  Saturday        : 09:00 AM - 01:00 PM (1st & 3rd)
                  Sunday & 2nd/4th: CLOSED
                  Online Banking  : 24/7 Available
                =====================================================""");
    }

    private static void runDiagnostics() {
        System.out.println("\n--- [10] RUN REFLECTION & ANNOTATION VALIDATOR ---");
        System.out.println("Testing Reflection Validator against an invalid dummy account (balance = -500)...");
        SavingsAccount invalidAccount = new SavingsAccount("AC9999", "Invalid Test User", -500L, true, 1000L);
        String[] errors = AnnotationValidator.validate(invalidAccount);

        if (errors.length == 0) {
            System.out.println("No annotation errors found.");
        } else {
            System.out.println("Annotation validation detected " + errors.length + " constraint violation(s):");
            for (String err : errors) {
                System.out.println("  (!) " + err);
            }
        }
    }

    private static void handleExit() {
        System.out.println("\nSaving application state to disk...");
        try {
            bankService.saveState();
            System.out.println("Data persisted successfully to data/accounts.dat.");
        } catch (IOException e) {
            System.err.println("Failed to persist data: " + e.getMessage());
        }
        System.out.println("""
                =============================================================
                  Thank you for using MiniBank! Have a wonderful day.
                =============================================================""");
    }

    private static void ensureInitialSeedData() {
        if (bankService.listAccounts().isEmpty()) {
            try {
                bankService.openAccount("Riya Sharma", "SAVINGS", 5000,
                        "riya.sharma@example.com", "9876543210", "ABCDE1234F", "MINI0123456");
                bankService.openAccount("Aarav Patel", "CURRENT", 12000,
                        "aarav.patel@example.com", "9812345678", "XYZPQ9876R", "MINI0123456");
                bankService.openAccount("Ananya Joshi", "FIXED_DEPOSIT", 50000,
                        "ananya.joshi@example.com", "9988776655", "JKLMN5432T", "MINI0123456");
            } catch (Exception ignored) {
            }
        }
    }
}
