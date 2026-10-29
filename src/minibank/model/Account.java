package minibank.model;

import minibank.exception.BankException;
import minibank.exception.InsufficientFundsException;
import minibank.exception.InvalidAmountException;
import minibank.model.annotation.Id;
import minibank.model.annotation.MaxLength;
import minibank.model.annotation.Positive;
import minibank.model.interfaces.InterestBearing;
import minibank.model.interfaces.Transactable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Practicals 2, 3, 5, 6, 7, 8, 9, 10, 11, 12:
 * Abstract base class representing a bank account with encapsulated state,
 * thread-safe synchronization, custom exceptions, validation annotations,
 * and interface implementations.
 */
public abstract class Account implements Transactable, InterestBearing, Comparable<Account>, Serializable {
    private static final long serialVersionUID = 1L;

    private static long accountCounter = 0;

    @Id
    private final String accountNumber;

    @MaxLength(50)
    private String ownerName;

    @Positive(message = "balance must be > 0")
    protected long balance; // in whole rupees

    private boolean active;

    protected static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public static synchronized void setStartingCounter(long counter) {
        if (counter > accountCounter) {
            accountCounter = counter;
        }
    }

    /**
     * Practical 2: Primary constructor
     */
    public Account(String ownerName, long openingBalance) {
        this(generateAccountNumber(), ownerName, openingBalance, true);
    }

    /**
     * Practical 2: Constructor chaining with this(ownerName, 0)
     */
    public Account(String ownerName) {
        this(ownerName, 0L);
    }

    /**
     * Constructor allowing specific account number (used in loaders/subclasses)
     */
    public Account(String accountNumber, String ownerName, long openingBalance, boolean active) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = active;
    }

    // Abstract methods defined in Practical 5
    public abstract double interestRate();
    public abstract boolean canWithdraw(long amount);
    public abstract String getAccountType();

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Practical 2, 8, 9: Thread-safe deposit with checked exception validation.
     */
    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        this.balance += amount;
    }

    /**
     * Practical 2, 5, 8, 9: Thread-safe withdraw enforcing subtype rules and checked exceptions.
     */
    @Override
    public synchronized boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }
        if (!canWithdraw(amount)) {
            long maxAllowed = balance;
            long shortfall = amount - maxAllowed;
            throw new InsufficientFundsException(shortfall > 0 ? shortfall : amount);
        }
        this.balance -= amount;
        return true;
    }

    /**
     * Practical 5 Supplementary: Calculate monthly interest.
     */
    public double monthlyInterest() {
        return (getBalance() * interestRate()) / (100.0 * 12.0);
    }

    /**
     * Practical 8 & 10: Safe atomic transfer between accounts.
     * Practical 10 Deadlock Prevention: Locks accounts in deterministic order (smaller accountNumber first).
     */
    public static void transfer(Account from, Account to, long amount) throws BankException {
        if (from == null || to == null) {
            throw new BankException("Transfer accounts must not be null");
        }
        if (from.equals(to)) {
            throw new BankException("Cannot transfer funds to the same account");
        }
        if (amount <= 0) {
            throw new InvalidAmountException(amount);
        }

        // Practical 10 Lock Ordering to prevent deadlock
        Account firstLock = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0 ? from : to;
        Account secondLock = (firstLock == from) ? to : from;

        synchronized (firstLock) {
            synchronized (secondLock) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    /**
     * Instance transfer helper
     */
    public void transferTo(Account target, long amount) throws BankException {
        transfer(this, target, amount);
    }

    /**
     * Practical 3: Readable string representation including active state.
     */
    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + balance +
               " | Type: " + getAccountType() + " | Active: " + active;
    }

    /**
     * Practical 3: Equality based on unique accountNumber.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account other)) return false;
        return Objects.equals(accountNumber, other.accountNumber);
    }

    /**
     * Practical 3: HashCode contract agreement with equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }

    /**
     * Practical 12: Natural ordering by accountNumber.
     */
    @Override
    public int compareTo(Account other) {
        if (other == null) return 1;
        return this.accountNumber.compareTo(other.accountNumber);
    }
}
