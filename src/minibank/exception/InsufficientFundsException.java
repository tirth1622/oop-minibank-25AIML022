package minibank.exception;

/**
 * Practical 8: Checked exception thrown when an account has insufficient funds for withdrawal or transfer.
 * Carries the exact shortfall amount.
 */
public class InsufficientFundsException extends BankException {
    private static final long serialVersionUID = 1L;
    private final long shortfall;

    public InsufficientFundsException(long shortfall) {
        super("Withdrawal failed: short by " + shortfall);
        this.shortfall = shortfall;
    }

    public InsufficientFundsException(String message, long shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}
