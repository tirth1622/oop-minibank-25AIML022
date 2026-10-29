package minibank.model;

/**
 * Practical 5: Current account with overdraft facility and 0.0% interest rate.
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;

    public static final long DEFAULT_OVERDRAFT_LIMIT = 5000L;
    private final long overdraftLimit;

    public CurrentAccount(String ownerName, long openingBalance, long overdraftLimit) {
        super(ownerName, openingBalance);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, DEFAULT_OVERDRAFT_LIMIT);
    }

    public CurrentAccount(String accountNumber, String ownerName, long openingBalance, boolean active, long overdraftLimit) {
        super(accountNumber, ownerName, openingBalance, active);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        // Allows balance to fall down to minus overdraftLimit
        return (balance - amount) >= -overdraftLimit;
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }

    @Override
    public String toString() {
        return super.toString() + " | OverdraftLimit: " + overdraftLimit + " | Interest: 0.0%";
    }
}
