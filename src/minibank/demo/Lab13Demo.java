package minibank.demo;

import minibank.model.Account;
import minibank.model.Customer;
import minibank.model.SavingsAccount;
import minibank.repository.Repository;

import java.util.List;

/**
 * Demonstration of Practical 13 - Part B:
 * - Generic Repository<T> with reflection-based @Id key extraction
 * - saveAll() with @SafeVarargs
 * - Generic bounded method <N extends Number> double average(N[] values)
 * - Repository reuse for both Account and Customer
 */
public class Lab13Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 13 (PART B) DEMONSTRATION: GENERICS & REPOSITORY   ");
        System.out.println("===============================================================");

        // 1 & 2. Repository<Account>
        System.out.println("1. Testing Repository<Account> with reflection on @Id:");
        Repository<Account> accountRepo = new Repository<>();

        Account a1 = new SavingsAccount("AC0001", "Riya Sharma", 5000, true, 1000);
        Account a2 = new SavingsAccount("AC0002", "Aarav Patel", 12000, true, 1000);
        Account a3 = new SavingsAccount("AC0003", "Ananya Joshi", 8000, true, 1000);

        // 3. saveAll varargs test
        accountRepo.saveAll(a1, a2, a3);
        System.out.println("   Saved 3 accounts using saveAll().");

        // findById
        Account fetched = accountRepo.findById("AC0002");
        System.out.println("   findById(\"AC0002\") -> " + fetched);

        // findAll
        List<Account> allAccounts = accountRepo.findAll();
        System.out.println("   findAll() returned " + allAccounts.size() + " accounts:");
        for (Account a : allAccounts) {
            System.out.println("     - " + a);
        }

        // Supplementary: Reuse same generic Repository<Customer> without changing code
        System.out.println("\n2. Reusing Repository<Customer> for Customer domain:");
        Repository<Customer> customerRepo = new Repository<>();
        Customer c1 = new Customer("CUST101", "Riya Sharma", "riya@bank.com", "9876543210", null);
        Customer c2 = new Customer("CUST102", "Aarav Patel", "aarav@bank.com", "9812345678", null);

        customerRepo.save(c1);
        customerRepo.save(c2);
        System.out.println("   Customer repository count: " + customerRepo.count());
        System.out.println("   customerRepo.findById(\"CUST101\") -> " + customerRepo.findById("CUST101"));

        // 4. Bounded generic method <N extends Number> average()
        System.out.println("\n3. Testing Bounded Generic Method <N extends Number> double average():");
        Integer[] intValues = {1000, 2000, 3000, 4000, 5000};
        Double[] doubleValues = {4.5, 7.0, 3.5, 6.0};
        Long[] longValues = {10000L, 25000L, 50000L};

        System.out.println("   Average of Integer[] : " + Repository.average(intValues));
        System.out.println("   Average of Double[]  : " + Repository.average(doubleValues));
        System.out.println("   Average of Long[]    : " + Repository.average(longValues));

        System.out.println("\n[Practical 13 Part B Completed Successfully]");
    }
}
