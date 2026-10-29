package minibank.exception;

/**
 * Practical 8: Checked exception thrown when an account number does not exist in the bank store.
 */
public class AccountNotFoundException extends BankException {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(String accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
