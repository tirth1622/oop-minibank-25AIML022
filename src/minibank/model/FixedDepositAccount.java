package minibank.model;

import java.time.LocalDate;

/**
 * Practical 5: Fixed deposit account with 7.0% interest rate and locked withdrawal rules.
 * Supplementary: Allows withdrawal if deposit has reached maturity date.
 */
public class FixedDepositAccount extends Account {
    private static final long serialVersionUID = 1L;

    private final LocalDate maturityDate;
    private final int termMonths;

    public FixedDepositAccount(String ownerName, long openingBalance, int termMonths) {
        super(ownerName, openingBalance);
        this.termMonths = termMonths;
        this.maturityDate = LocalDate.now().plusMonths(termMonths);
    }

    public FixedDepositAccount(String ownerName, long openingBalance) {
        this(ownerName, openingBalance, 12);
    }

    public FixedDepositAccount(String accountNumber, String ownerName, long openingBalance, boolean active, LocalDate maturityDate, int termMonths) {
        super(accountNumber, ownerName, openingBalance, active);
        this.maturityDate = maturityDate;
        this.termMonths = termMonths;
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        // Locked deposit: cannot withdraw before maturity
        if (maturityDate != null && LocalDate.now().isBefore(maturityDate)) {
            return false;
        }
        return amount <= balance;
    }

    public boolean isMatured() {
        return maturityDate != null && !LocalDate.now().isBefore(maturityDate);
    }

    @Override
    public String getAccountType() {
        return "FIXED_DEPOSIT";
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }

    public int getTermMonths() {
        return termMonths;
    }

    @Override
    public String toString() {
        return super.toString() + " | Maturity: " + maturityDate + " (" + termMonths + " months) | Interest: 7.0%";
    }
}
