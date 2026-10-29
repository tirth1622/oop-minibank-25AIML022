package minibank.model.interfaces;

import minibank.model.Account;

/**
 * Practical 6 & 7: Functional interface defining a custom withdrawal authorization rule.
 */
@FunctionalInterface
public interface WithdrawRule {
    boolean allow(Account account, long amount);
}
