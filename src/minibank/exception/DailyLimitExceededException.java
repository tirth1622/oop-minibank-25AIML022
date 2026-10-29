package minibank.exception;

/**
 * Practical 8 Supplementary: Checked exception thrown when a withdrawal exceeds a daily cap.
 */
public class DailyLimitExceededException extends BankException {
    private static final long serialVersionUID = 1L;
    private final long dailyLimit;
    private final long requestedAmount;

    public DailyLimitExceededException(long requestedAmount, long dailyLimit) {
        super("Daily withdrawal limit of " + dailyLimit + " exceeded by requested amount: " + requestedAmount);
        this.dailyLimit = dailyLimit;
        this.requestedAmount = requestedAmount;
    }

    public long getDailyLimit() {
        return dailyLimit;
    }

    public long getRequestedAmount() {
        return requestedAmount;
    }
}
