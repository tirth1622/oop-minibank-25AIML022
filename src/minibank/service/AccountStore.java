package minibank.service;

import minibank.model.Account;

import java.util.*;

/**
 * Practical 12: Account storage using the Java Collections Framework.
 * Uses HashMap for O(1) account lookup, ArrayList for transaction history,
 * and Comparator for sorting accounts by balance.
 */
public class AccountStore {

    private final Map<String, Account> accounts = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        if (account != null) {
            accounts.put(account.getAccountNumber(), account);
        }
    }

    public Account get(String accountNumber) {
        if (accountNumber == null) return null;
        return accounts.get(accountNumber);
    }

    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void logTransaction(String entry) {
        if (entry != null) {
            transactionHistory.add(entry);
        }
    }

    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    /**
     * Practical 12: Returns all accounts sorted by balance descending using Comparator.
     */
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    /**
     * Practical 12 Supplementary: Returns accounts with balance strictly above threshold.
     */
    public List<Account> findAccountsWithBalanceAbove(long threshold) {
        List<Account> result = new ArrayList<>();
        for (Account a : accounts.values()) {
            if (a.getBalance() > threshold) {
                result.add(a);
            }
        }
        return result;
    }

    /**
     * Practical 12 Supplementary: Returns accounts stored in sorted key order using TreeMap.
     */
    public SortedMap<String, Account> asTreeMap() {
        return new TreeMap<>(accounts);
    }

    public int count() {
        return accounts.size();
    }
}
