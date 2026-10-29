package minibank.test;

import minibank.concurrency.AccountWorker;
import minibank.concurrency.DeadlockDemo;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.*;
import minibank.persistence.StatePersister;
import minibank.repository.Repository;
import minibank.service.BankService;
import minibank.util.AnnotationValidator;
import minibank.util.CommandParser;
import minibank.util.Validator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical 14: Comprehensive Automated Test Suite.
 * Validates all core requirements specified across the practicals:
 * - Deposit increases balance
 * - Withdrawal over limit throws InsufficientFundsException
 * - Concurrent multithreaded deposits maintain strict thread safety
 * - Regex validation rules
 * - Serialization roundtrip
 * - Reflection annotation validator
 * - Generic repository CRUD
 * - Deadlock-free atomic transfer
 */
public class MiniBankTestSuite {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=============================================================");
        System.out.println("            MINIBANK AUTOMATED SYSTEM TEST SUITE             ");
        System.out.println("=============================================================");

        runTest("Deposit Increases Balance", MiniBankTestSuite::testDepositIncreasesBalance);
        runTest("Withdraw Over Limit Throws InsufficientFundsException", MiniBankTestSuite::testWithdrawOverLimitThrowsException);
        runTest("Deposit Negative Amount Throws InvalidAmountException", MiniBankTestSuite::testInvalidDepositAmount);
        runTest("Concurrent Multithreaded Deposits Keep Exact Balance", MiniBankTestSuite::testConcurrentDeposits);
        runTest("Regex Validators (Mobile, Email, PAN, IFSC)", MiniBankTestSuite::testRegexValidators);
        runTest("Command Parser Correctly Tokenises String", MiniBankTestSuite::testCommandParser);
        runTest("Polymorphic Interest Rates for Account Types", MiniBankTestSuite::testPolymorphism);
        runTest("Reflection Annotation Validator Catches Violations", MiniBankTestSuite::testAnnotationValidator);
        runTest("Generic Repository CRUD with Reflection @Id", MiniBankTestSuite::testGenericRepository);
        runTest("Serialization State Persister Survives Roundtrip", MiniBankTestSuite::testSerialization);
        runTest("Deadlock-Free Lock-Ordered Transfers", MiniBankTestSuite::testDeadlockFreeTransfer);

        System.out.println("=============================================================");
        System.out.printf("Test Execution Summary: %d PASSED, %d FAILED (Total: %d)%n",
                passed, failed, (passed + failed));
        System.out.println("=============================================================");

        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void runTest(String testName, TestCase test) {
        try {
            test.run();
            System.out.printf("  [PASS] %s%n", testName);
            passed++;
        } catch (Throwable t) {
            System.err.printf("  [FAIL] %s: %s%n", testName, t.getMessage());
            failed++;
        }
    }

    @FunctionalInterface
    interface TestCase {
        void run() throws Throwable;
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (expected == null && actual == null) return;
        if (expected != null && expected.equals(actual)) return;
        throw new AssertionError(message + " (Expected: " + expected + ", Got: " + actual + ")");
    }

    // 1. Practical 14 Requirement: A deposit increases the balance
    private static void testDepositIncreasesBalance() throws Exception {
        Account account = new SavingsAccount("AC0001", "Tester", 1000, true, 500);
        account.deposit(500);
        assertEquals(1500L, account.getBalance(), "Balance must be 1500 after depositing 500 into 1000");
    }

    // 2. Practical 14 Requirement: A withdrawal over the limit throws an exception
    private static void testWithdrawOverLimitThrowsException() throws Exception {
        Account account = new SavingsAccount("AC0001", "Tester", 1000, true, 500);
        boolean exceptionThrown = false;
        try {
            // Balance is 1000, minBalance is 500. Withdrawing 600 leaves 400 (< minBalance) -> throws!
            account.withdraw(600);
        } catch (InsufficientFundsException e) {
            exceptionThrown = true;
            assertTrue(e.getShortfall() > 0, "Shortfall must be greater than zero");
        }
        assertTrue(exceptionThrown, "Must throw InsufficientFundsException when withdrawal exceeds allowed limit");
        assertEquals(1000L, account.getBalance(), "Balance must remain unchanged after failed withdrawal");
    }

    private static void testInvalidDepositAmount() {
        Account account = new SavingsAccount("AC0001", "Tester", 1000, true, 500);
        boolean thrown = false;
        try {
            account.deposit(-50);
        } catch (InvalidAmountException e) {
            thrown = true;
        }
        assertTrue(thrown, "Must throw InvalidAmountException for non-positive deposit");
    }

    // 3. Practical 14 Requirement: Many concurrent deposits keep the balance correct
    private static void testConcurrentDeposits() throws Exception {
        Account account = new SavingsAccount("AC0001", "ThreadTest", 0, true, 0);
        int threadCount = 10;
        int depositsPerThread = 1000;
        long amountPerDeposit = 1;

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            Thread t = new Thread(new AccountWorker(account, depositsPerThread, amountPerDeposit));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        long expectedBalance = (long) threadCount * depositsPerThread * amountPerDeposit;
        assertEquals(expectedBalance, account.getBalance(),
                "Synchronized account must have exactly " + expectedBalance + " with zero lost updates");
    }

    // 4. Practical 4 Regex Validators
    private static void testRegexValidators() {
        assertTrue(Validator.isValidMobile("9876543210"), "Valid 10-digit mobile must pass");
        assertTrue(!Validator.isValidMobile("123456"), "Short mobile number must fail");
        assertTrue(!Validator.isValidMobile("5876543210"), "Mobile starting with 5 must fail");

        assertTrue(Validator.isValidEmail("john.doe@bank.com"), "Valid email must pass");
        assertTrue(!Validator.isValidEmail("invalid-email"), "Malformed email must fail");

        assertTrue(Validator.isValidPan("ABCDE1234F"), "Valid PAN format must pass");
        assertTrue(!Validator.isValidPan("12345ABCDE"), "Invalid PAN format must fail");

        assertTrue(Validator.isValidIfsc("SBIN0001234"), "Valid IFSC format must pass");
        assertTrue(!Validator.isValidIfsc("SBIN1234"), "Invalid IFSC format must fail");
    }

    // 5. Practical 4 Command Parser
    private static void testCommandParser() {
        Command cmd = CommandParser.parse("DEPOSIT AC0001 500");
        assertEquals(TransactionType.DEPOSIT, cmd.type(), "Parsed command type must match");
        assertEquals("AC0001", cmd.accountNumber(), "Account number must match");
        assertEquals(500L, cmd.amount(), "Parsed amount must match 500");
    }

    // 6. Practical 5 Polymorphism
    private static void testPolymorphism() {
        Account savings = new SavingsAccount("User1", 1000);
        Account current = new CurrentAccount("User2", 1000);
        Account fd = new FixedDepositAccount("User3", 1000);

        assertEquals(4.0, savings.interestRate(), "Savings rate must be 4.0%");
        assertEquals(0.0, current.interestRate(), "Current rate must be 0.0%");
        assertEquals(7.0, fd.interestRate(), "FD rate must be 7.0%");
    }

    // 7. Practical 7 Annotation Validator
    private static void testAnnotationValidator() {
        SavingsAccount invalidAcc = new SavingsAccount("AC9999", "Tester", -100, true, 500);
        String[] errors = AnnotationValidator.validate(invalidAcc);
        assertTrue(errors.length > 0, "Must report constraint violations for negative balance");
        assertTrue(errors[0].contains("must be > 0"), "Violation message must contain 'must be > 0'");
    }

    // 8. Practical 13 Generic Repository with @Id
    private static void testGenericRepository() {
        Repository<Account> repo = new Repository<>();
        Account a1 = new SavingsAccount("AC0001", "Riya", 5000, true, 1000);
        Account a2 = new SavingsAccount("AC0002", "Aarav", 8000, true, 1000);

        repo.saveAll(a1, a2);
        assertEquals(2, repo.count(), "Repository must have 2 accounts");
        assertEquals("Riya", repo.findById("AC0001").getOwnerName(), "Must find entity by @Id key");

        repo.delete("AC0001");
        assertEquals(1, repo.count(), "Count must be 1 after deleting one account");
    }

    // 9. Practical 11 Serialization
    private static void testSerialization() throws Exception {
        Path tempFile = Paths.get("data", "test_accounts.dat");
        Account[] accounts = new Account[]{
                new SavingsAccount("AC0001", "SerializeTest", 12345, true, 1000)
        };
        StatePersister.save(accounts, tempFile);
        Account[] loaded = StatePersister.load(tempFile);
        assertEquals(1, loaded.length, "Loaded array must contain 1 account");
        assertEquals(12345L, loaded[0].getBalance(), "Loaded balance must match serialized balance");
        Files.deleteIfExists(tempFile);
    }

    // 10. Practical 10 Deadlock-free Transfer
    private static void testDeadlockFreeTransfer() throws Exception {
        Account a = new SavingsAccount("AC0001", "User A", 5000, true, 0);
        Account b = new SavingsAccount("AC0002", "User B", 5000, true, 0);

        DeadlockDemo.executeConsistentLockTransfer(a, b, 1000);
        assertEquals(4000L, a.getBalance(), "Account A balance after transfer");
        assertEquals(6000L, b.getBalance(), "Account B balance after transfer");
    }
}
