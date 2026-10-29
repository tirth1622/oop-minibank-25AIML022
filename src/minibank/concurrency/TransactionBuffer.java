package minibank.concurrency;

import minibank.model.Command;

/**
 * Practical 10: Producer-Consumer circular array buffer for transactions
 * coordinated via wait() and notifyAll() inside synchronized blocks.
 */
public class TransactionBuffer {

    private final Command[] buffer;
    private int head = 0;
    private int tail = 0;
    private int count = 0;
    private final int capacity;

    public TransactionBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Command[capacity];
    }

    /**
     * Producer: Puts a transaction into the buffer, waiting if buffer is full.
     */
    public synchronized void produce(Command command) throws InterruptedException {
        while (count == capacity) {
            wait();
        }
        buffer[tail] = command;
        tail = (tail + 1) % capacity;
        count++;
        notifyAll(); // Notify waiting consumers
    }

    /**
     * Consumer: Takes a transaction from buffer, waiting if buffer is empty.
     */
    public synchronized Command consume() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        Command item = buffer[head];
        head = (head + 1) % capacity;
        count--;
        notifyAll(); // Notify waiting producers
        return item;
    }

    public synchronized int getCount() {
        return count;
    }
}
