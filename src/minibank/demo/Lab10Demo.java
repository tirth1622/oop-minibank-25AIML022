package minibank.demo;

import minibank.concurrency.DeadlockDemo;
import minibank.concurrency.TransactionBuffer;
import minibank.concurrency.TransactionProcessor;
import minibank.model.Account;
import minibank.model.Command;
import minibank.model.SavingsAccount;
import minibank.model.TransactionType;

/**
 * Demonstration of Practical 10 - Part B:
 * - Thread pool transaction processing (ExecutorService)
 * - Producer-Consumer coordination with wait() and notifyAll()
 * - Deadlock simulation and resolution via consistent lock ordering
 */
public class Lab10Demo {
    public static void main(String[] args) throws Exception {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 10 (PART B) DEMONSTRATION: POOLS, PROD/CONS & DEADLOCK");
        System.out.println("===============================================================");

        // 1 & 2. Thread pool processor
        System.out.println("1. Testing TransactionProcessor Thread Pool (4 threads):");
        TransactionProcessor processor = new TransactionProcessor(4);
        Account account = new SavingsAccount("AC0001", "Riya Sharma", 1000, true, 0);

        for (int i = 1; i <= 8; i++) {
            final int id = i;
            processor.submit(() -> {
                try {
                    account.deposit(100);
                    System.out.println("   [Pool Task " + id + "] Deposited Rs. 100 by " + Thread.currentThread().getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        processor.stop();
        System.out.println("   Processor stopped. Account balance: Rs. " + account.getBalance() + " (Expected: 1800)");
        processor.printThreadExecutionStats();

        // 3. Producer-Consumer buffer
        System.out.println("\n2. Testing Producer-Consumer Buffer (Capacity 2) with wait/notify:");
        TransactionBuffer buffer = new TransactionBuffer(2);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 4; i++) {
                    Command cmd = new Command(TransactionType.DEPOSIT, "AC0001", i * 100);
                    buffer.produce(cmd);
                    System.out.println("   [Producer] Produced: " + cmd);
                    Thread.sleep(50);
                }
            } catch (InterruptedException ignored) {}
        }, "Tx-Producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 4; i++) {
                    Command cmd = buffer.consume();
                    System.out.println("   [Consumer] Consumed and processed: " + cmd);
                    Thread.sleep(80);
                }
            } catch (InterruptedException ignored) {}
        }, "Tx-Consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();

        // 4. Deadlock creation and resolution
        System.out.println("\n3. Testing Deadlock Condition vs Solution:");
        Account accA = new SavingsAccount("AC0001", "Account A", 5000, true, 0);
        Account accB = new SavingsAccount("AC0002", "Account B", 5000, true, 0);

        System.out.println("   (a) Simulating Opposite Lock Order (Thread 1: A->B, Thread 2: B->A)...");
        boolean deadlocked = DeadlockDemo.simulateOppositeOrderDeadlock(accA, accB, 500);
        System.out.println("       Deadlock Detected: " + deadlocked + " (Threads stalled indefinitely on reversed locks)");

        System.out.println("   (b) Executing with Consistent Lock Ordering (Lock smaller accountNumber first)...");
        Account fixedA = new SavingsAccount("AC0003", "Fixed Account A", 5000, true, 0);
        Account fixedB = new SavingsAccount("AC0004", "Fixed Account B", 5000, true, 0);

        DeadlockDemo.executeConsistentLockTransfer(fixedA, fixedB, 500);
        DeadlockDemo.executeConsistentLockTransfer(fixedB, fixedA, 500);
        System.out.println("       Both transfers completed normally without deadlock!");
        System.out.println("       Balance A: Rs. " + fixedA.getBalance() + " | Balance B: Rs. " + fixedB.getBalance());

        System.out.println("\n[Practical 10 Part B Completed Successfully]");
    }
}
