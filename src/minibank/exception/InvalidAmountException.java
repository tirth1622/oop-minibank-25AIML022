package minibank.exception;

/**
 * Practical 8: Checked exception thrown when an operation amount is non-positive or malformed.
 */
public class InvalidAmountException extends BankException {
    private static final long serialVersionUID = 1L;

    public InvalidAmountException(String message) {
        super(message);
    }

    public InvalidAmountException(long amount) {
        super("Invalid transaction amount: " + amount + ". Amount must be greater than 0.");
    }
}
