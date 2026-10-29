package minibank.concurrency;

import minibank.model.Account;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Practical 10 & 12: Managed transaction processor with fixed thread pool (4 threads)
 * and thread-safe ConcurrentHashMap account store.
 */
public class TransactionProcessor {

    private final ExecutorService executor;
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> threadExecutionCounts = new ConcurrentHashMap<>();

    public TransactionProcessor() {
        this(4);
    }

    public TransactionProcessor(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize, new ThreadFactory() {
            private int counter = 1;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "TxPool-Worker-" + counter++);
            }
        });
    }

    public void registerAccount(Account account) {
        if (account != null) {
            sharedAccounts.put(account.getAccountNumber(), account);
        }
    }

    public Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return sharedAccounts;
    }

    /**
     * Practical 10: Submits a Runnable transaction task to the thread pool.
     */
    public void submit(Runnable task) {
        executor.execute(() -> {
            String threadName = Thread.currentThread().getName();
            threadExecutionCounts.computeIfAbsent(threadName, k -> new AtomicInteger(0)).incrementAndGet();
            task.run();
        });
    }

    /**
     * Practical 10: Gracefully shuts down pool and awaits termination.
     */
    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Practical 10 Supplementary: Prints how many tasks each pool thread executed.
     */
    public void printThreadExecutionStats() {
        System.out.println("=== Thread Pool Execution Statistics ===");
        threadExecutionCounts.forEach((threadName, count) -> {
            System.out.println("Thread [" + threadName + "] processed: " + count.get() + " tasks");
        });
    }
}
