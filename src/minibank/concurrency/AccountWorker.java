package minibank.concurrency;

import minibank.exception.InvalidAmountException;
import minibank.model.Account;

/**
 * Practical 9: Worker task executing repeated concurrent deposits against an Account.
 * Demonstrates thread race conditions when Account methods are not synchronized,
 * and correct balance when synchronized.
 */
public class AccountWorker implements Runnable {
    private final Account account;
    private final int times;
    private final long amount;

    public AccountWorker(Account account, int times, long amount) {
        this.account = account;
        this.times = times;
        this.amount = amount;
    }

    @Override
    public void run() {
        for (int i = 0; i < times; i++) {
            try {
                account.deposit(amount);
            } catch (InvalidAmountException e) {
                System.err.println("Worker deposit failed: " + e.getMessage());
            }
        }
    }
}
