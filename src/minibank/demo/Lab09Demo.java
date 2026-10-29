package minibank.demo;

import minibank.concurrency.AccountWorker;
import minibank.model.Account;
import minibank.model.SavingsAccount;

/**
 * Demonstration of Practical 9 - Part B:
 * - Multithreading and Runnable AccountWorker
 * - Race condition demonstration vs Thread-safe synchronization
 * - Starting and joining 10 threads
 * - Thread lifecycle observation
 */
public class Lab09Demo {

    // Unsynchronized dummy account for demonstration of race condition
    static class UnsynchronizedAccount {
        private long balance = 0;
        public void deposit(long amount) {
            long temp = balance;
            try { Thread.sleep(0, 100); } catch (InterruptedException ignored) {}
            balance = temp + amount;
        }
        public long getBalance() { return balance; }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 9 (PART B) DEMONSTRATION: MULTITHREADING & RACES   ");
        System.out.println("===============================================================");

        int threadCount = 10;
        int depositsPerThread = 1000;
        long depositAmount = 1;

        // 1. Unsynchronized demonstration (Race Condition)
        System.out.println("1. Running 10 threads WITHOUT synchronization (1,000 deposits each of Rs. 1):");
        UnsynchronizedAccount unsyncAcc = new UnsynchronizedAccount();
        Thread[] unsyncThreads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            unsyncThreads[i] = new Thread(() -> {
                for (int j = 0; j < depositsPerThread; j++) {
                    unsyncAcc.deposit(depositAmount);
                }
            }, "Unsync-Thread-" + (i + 1));
            unsyncThreads[i].start();
        }
        for (Thread t : unsyncThreads) {
            t.join();
        }
        System.out.println("   Final Balance WITHOUT synchronization: " + unsyncAcc.getBalance() +
                " (Expected: 10000, Lost updates occurred!)");

        // 2. Synchronized demonstration (Thread-Safe MiniBank Account)
        System.out.println("\n2. Running 10 threads WITH MiniBank synchronization:");
        Account syncAcc = new SavingsAccount("AC0001", "Riya Sharma", 0, true, 0);
        Thread[] syncThreads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            AccountWorker worker = new AccountWorker(syncAcc, depositsPerThread, depositAmount);
            syncThreads[i] = new Thread(worker, "BankWorker-Thread-" + (i + 1));
            System.out.println("   Starting Thread: " + syncThreads[i].getName() + " [State: " + syncThreads[i].getState() + "]");
            syncThreads[i].start();
        }

        for (Thread t : syncThreads) {
            t.join();
            System.out.println("   Joined Thread: " + t.getName() + " [State: " + t.getState() + "]");
        }

        System.out.println("\n   Final Synchronized Balance: Rs. " + syncAcc.getBalance() + " (Expected: EXACTLY 10000)");

        System.out.println("\n[Practical 9 Part B Completed Successfully]");
    }
}
