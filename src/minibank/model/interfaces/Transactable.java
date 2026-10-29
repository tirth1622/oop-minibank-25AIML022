package minibank.model.interfaces;

import minibank.exception.BankException;

/**
 * Practical 6: Capability interface expressing fundamental transaction operations.
 */
public interface Transactable {
    void deposit(long amount) throws BankException;
    boolean withdraw(long amount) throws BankException;
}
