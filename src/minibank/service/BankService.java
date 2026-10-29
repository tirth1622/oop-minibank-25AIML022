package minibank.service;

import minibank.concurrency.TransactionProcessor;
import minibank.exception.AccountNotFoundException;
import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.*;
import minibank.model.annotation.Audit;
import minibank.persistence.ReportGenerator;
import minibank.persistence.StatePersister;
import minibank.persistence.TransactionLog;
import minibank.repository.Repository;
import minibank.util.AuditLogger;
import minibank.util.BankingSession;
import minibank.util.StatementFormatter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static minibank.util.Validator.*;

/**
 * Practical 14 (Capstone): Unified banking service layer.
 * Integrates generic repository, regex validation, exception handling,
 * thread pool processing, transaction logging, NIO reporting, and reflection auditing.
 */
public class BankService {

    private final Repository<Account> accountRepository = new Repository<>();
    private final Repository<Customer> customerRepository = new Repository<>();
    private final Path dataFile;
    private final Path logsDir;
    private final Path currentLogFile;

    public BankService() {
        this(Paths.get("data", "accounts.dat"), Paths.get("logs"));
    }

    public BankService(Path dataFile, Path logsDir) {
        this.dataFile = dataFile;
        this.logsDir = logsDir;
        this.currentLogFile = logsDir.resolve("transactions.log");
        loadStateSilently();
    }

    public Repository<Account> getAccountRepository() {
        return accountRepository;
    }

    public Repository<Customer> getCustomerRepository() {
        return customerRepository;
    }

    /**
     * Practical 14: Opens a new account (Savings, Current, or Fixed Deposit) with validated inputs.
     */
    @Audit(action = "OPEN_ACCOUNT")
    public Account openAccount(String ownerName, String accountType, long initialBalance,
                               String email, String mobile, String pan, String ifsc) throws BankException {
        // Enforce validation layer (Practical 4 & 14)
        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new BankException("Validation Error: Owner name cannot be blank.");
        }
        if (email != null && !email.isEmpty() && !isValidEmail(email)) {
            throw new BankException("Validation Error: Invalid email format '" + email + "'.");
        }
        if (mobile != null && !mobile.isEmpty() && !isValidMobile(mobile)) {
            throw new BankException("Validation Error: Invalid mobile number '" + mobile + "'. Must be 10 digits starting with 6-9.");
        }
        if (pan != null && !pan.isEmpty() && !isValidPan(pan)) {
            throw new BankException("Validation Error: Invalid PAN number '" + pan + "'. Format: ABCDE1234F.");
        }
        if (ifsc != null && !ifsc.isEmpty() && !isValidIfsc(ifsc)) {
            throw new BankException("Validation Error: Invalid IFSC code '" + ifsc + "'. Format: ABCD0123456.");
        }
        if (initialBalance < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative.");
        }

        // Register customer
        Customer customer = new Customer(ownerName, email, mobile);
        customerRepository.save(customer);

        Account account;
        String type = accountType != null ? accountType.trim().toUpperCase() : "SAVINGS";
        switch (type) {
            case "CURRENT" -> account = new CurrentAccount(ownerName, initialBalance);
            case "FD", "FIXED_DEPOSIT" -> account = new FixedDepositAccount(ownerName, initialBalance, 12);
            case "SAVINGS" -> account = new SavingsAccount(ownerName, initialBalance);
            default -> throw new BankException("Unknown account type: " + accountType + ". Allowed: SAVINGS, CURRENT, FIXED_DEPOSIT");
        }

        accountRepository.save(account);
        logTransaction("OPEN_ACCOUNT " + account.getAccountNumber() + " TYPE=" + account.getAccountType() + " BAL=" + initialBalance);
        AuditLogger.log(BankService.class, "openAccount", "Created " + account.getAccountNumber() + " for " + ownerName);
        return account;
    }

    /**
     * Practical 14: Deposits funds into an existing account.
     */
    @Audit(action = "DEPOSIT")
    public void deposit(String accountNumber, long amount) throws BankException {
        Account account = findAccountOrThrow(accountNumber);
        account.deposit(amount);
        logTransaction("DEPOSIT " + accountNumber + " " + amount);
        AuditLogger.log(BankService.class, "deposit", "Deposited " + amount + " to " + accountNumber);
    }

    /**
     * Practical 14: Withdraws funds from an existing account.
     */
    @Audit(action = "WITHDRAW")
    public void withdraw(String accountNumber, long amount) throws BankException {
        Account account = findAccountOrThrow(accountNumber);
        account.withdraw(amount);
        logTransaction("WITHDRAW " + accountNumber + " " + amount);
        AuditLogger.log(BankService.class, "withdraw", "Withdrew " + amount + " from " + accountNumber);
    }

    /**
     * Practical 14: Transfers funds safely between two accounts with deadlock-free lock ordering.
     */
    @Audit(action = "TRANSFER")
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws BankException {
        Account from = findAccountOrThrow(fromAccountNum);
        Account to = findAccountOrThrow(toAccountNum);

        Account.transfer(from, to, amount);
        logTransaction("TRANSFER " + fromAccountNum + " " + toAccountNum + " " + amount);
        AuditLogger.log(BankService.class, "transfer", "Transferred " + amount + " from " + fromAccountNum + " to " + toAccountNum);
    }

    /**
     * Practical 14: Returns formatted statement using StatementFormatter.
     */
    public String statement(String accountNumber) throws BankException {
        Account account = findAccountOrThrow(accountNumber);
        return StatementFormatter.buildStatement(account);
    }

    /**
     * Practical 14: Returns all accounts, sorted by balance descending.
     */
    public List<Account> listAccounts() {
        List<Account> list = accountRepository.findAll();
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    /**
     * Practical 14: Runs safe batch transaction processing using TransactionProcessor.
     */
    public void processBatch(List<Runnable> tasks) {
        TransactionProcessor processor = new TransactionProcessor(4);
        for (Account a : accountRepository.findAll()) {
            processor.registerAccount(a);
        }
        for (Runnable task : tasks) {
            processor.submit(task);
        }
        processor.stop();
    }

    /**
     * Practical 14: Generates end-of-day reconciliation report via ReportGenerator.
     */
    public String generateReport() throws IOException {
        Path reportFile = logsDir.resolve("eod_reconciliation_report.txt");
        return ReportGenerator.generateReport(logsDir, reportFile);
    }

    /**
     * Practical 11 & 14: Saves accounts to binary file via StatePersister.
     */
    public void saveState() throws IOException {
        List<Account> accounts = accountRepository.findAll();
        StatePersister.saveCollection(accounts, dataFile);
    }

    /**
     * Practical 11 & 14: Loads accounts from binary file via StatePersister.
     */
    public void loadState() throws IOException, ClassNotFoundException {
        Account[] accounts = StatePersister.load(dataFile);
        accountRepository.clear();
        long maxAccNum = 0;
        for (Account a : accounts) {
            accountRepository.save(a);
            try {
                String numStr = a.getAccountNumber().replace("AC", "");
                long num = Long.parseLong(numStr);
                if (num > maxAccNum) maxAccNum = num;
            } catch (Exception ignored) {
            }
        }
        Account.setStartingCounter(maxAccNum);
    }

    private void loadStateSilently() {
        try {
            loadState();
        } catch (Exception ignored) {
            // New database / empty on first run
        }
    }

    private Account findAccountOrThrow(String accountNumber) throws AccountNotFoundException {
        Account account = accountRepository.findById(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    private void logTransaction(String entry) {
        try {
            TransactionLog.append(currentLogFile, entry);
        } catch (IOException e) {
            System.err.println("Audit log append failed: " + e.getMessage());
        }
    }
}
