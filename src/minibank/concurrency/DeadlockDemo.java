package minibank.concurrency;

import minibank.exception.BankException;
import minibank.model.Account;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Practical 10: Demonstrates classic lock-order inversion deadlock between two accounts
 * transferring money in opposite directions, and verifies the consistent lock-ordering solution.
 */
public class DeadlockDemo {

    /**
     * Simulates the deadlock condition (opposite lock order: A locks A then B; B locks B then A).
     * Uses a timeout so test runs do not hang forever.
     *
     * @return true if deadlock occurred / timed out, false if finished
     */
    public static boolean simulateOppositeOrderDeadlock(Account a, Account b, long timeoutMs) {
        CountDownLatch latch = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            synchronized (a) {
                latch.countDown();
                try {
                    // Small sleep to ensure t2 acquires lock on b
                    Thread.sleep(50);
                    synchronized (b) {
                        a.withdraw(10);
                        b.deposit(10);
                    }
                } catch (Exception ignored) {
                }
            }
        }, "Deadlock-Thread-1");

        Thread t2 = new Thread(() -> {
            synchronized (b) {
                latch.countDown();
                try {
                    // Small sleep to ensure t1 acquires lock on a
                    Thread.sleep(50);
                    synchronized (a) {
                        b.withdraw(10);
                        a.deposit(10);
                    }
                } catch (Exception ignored) {
                }
            }
        }, "Deadlock-Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join(timeoutMs);
            t2.join(timeoutMs);
            if (t1.isAlive() || t2.isAlive()) {
                // Deadlock confirmed! The threads are stuck waiting for each other's lock.
                t1.interrupt();
                t2.interrupt();
                return true;
            }
            return false;
        } catch (InterruptedException e) {
            return true;
        }
    }

    /**
     * Executes transfer with consistent lock ordering (locking account with smaller accountNumber first).
     * Prevents deadlock completely.
     */
    public static void executeConsistentLockTransfer(Account a, Account b, long amount) throws BankException {
        Account.transfer(a, b, amount);
    }
}
