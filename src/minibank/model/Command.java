package minibank.model;

import java.io.Serializable;

/**
 * Practical 4: Record representing a parsed banking command.
 * Three primary fields: type, accountNumber, amount.
 * Also supports optional targetAccountNumber for transfers.
 */
public record Command(TransactionType type, String accountNumber, String targetAccountNumber, long amount) implements Serializable {
    private static final long serialVersionUID = 1L;

    public Command(TransactionType type, String accountNumber, long amount) {
        this(type, accountNumber, null, amount);
    }

    @Override
    public String toString() {
        if (type == TransactionType.TRANSFER && targetAccountNumber != null) {
            return "Command[type=" + type + ", from=" + accountNumber + ", to=" + targetAccountNumber + ", amount=" + amount + "]";
        }
        return "Command[type=" + type + ", accountNumber=" + accountNumber + ", amount=" + amount + "]";
    }
}
