package minibank.model;

import minibank.model.interfaces.Premium;

/**
 * Practical 5 & 6: Savings account with minimum balance requirement and 4.0% interest rate.
 * Implements marker interface Premium if minBalance >= 10000.
 */
public class SavingsAccount extends Account implements Premium {
    private static final long serialVersionUID = 1L;

    public static final long DEFAULT_MIN_BALANCE = 1000L;
    private final long minBalance;

    public SavingsAccount(String ownerName, long openingBalance, long minBalance) {
        super(ownerName, openingBalance);
        this.minBalance = minBalance;
    }

    public SavingsAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, DEFAULT_MIN_BALANCE);
    }

    public SavingsAccount(String accountNumber, String ownerName, long openingBalance, boolean active, long minBalance) {
        super(accountNumber, ownerName, openingBalance, active);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() {
        return 4.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return (balance - amount) >= minBalance;
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    public long getMinBalance() {
        return minBalance;
    }

    @Override
    public String toString() {
        return super.toString() + " | MinBalance: " + minBalance + " | Interest: 4.0%";
    }
}
