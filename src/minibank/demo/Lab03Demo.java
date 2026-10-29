package minibank.demo;

import minibank.model.Account;
import minibank.model.Customer;
import minibank.model.SavingsAccount;

/**
 * Demonstration of Practical 3 - Part B:
 * - toString() override
 * - equals() and hashCode() contract based on accountNumber
 * - Static nested Address class
 * - Cloneable clone() method on Customer
 * - instanceof type check
 */
public class Lab03Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("   PRACTICAL 3 (PART B) DEMONSTRATION: OBJECT METHODS & CLONE  ");
        System.out.println("===============================================================");

        // 1 & 2. Two Account objects with the same accountNumber
        Account a1 = new SavingsAccount("AC0001", "Riya", 4000, true, 1000);
        Account a2 = new SavingsAccount("AC0001", "Riya Sharma", 9999, true, 1000);
        Account a3 = new SavingsAccount("AC0002", "Aarav", 4000, true, 1000);

        System.out.println("1. Testing Account.toString():");
        System.out.println("   a1: " + a1);

        System.out.println("\n2. Testing equals() and hashCode():");
        System.out.println("   a1.equals(a2) [same acc number 'AC0001']: " + a1.equals(a2) + " (Expected: true)");
        System.out.println("   a1.hashCode() == a2.hashCode(): " + (a1.hashCode() == a2.hashCode()) + " (Expected: true)");
        System.out.println("   a1.equals(a3) [different acc number]: " + a1.equals(a3) + " (Expected: false)");

        // 3. Static nested class Address
        Customer.Address addr = new Customer.Address("123 MG Road", "Mumbai", "400001");
        Customer customer = new Customer("Riya", "riya@bank.com", "9876543210", addr);
        System.out.println("\n3. Testing Nested Address & Customer:");
        System.out.println("   " + customer);

        // 4. clone() implementation
        Customer clonedCustomer = customer.clone();
        System.out.println("\n4. Testing clone():");
        System.out.println("   Original: " + customer);
        System.out.println("   Cloned  : " + clonedCustomer);
        System.out.println("   customer != clonedCustomer: " + (customer != clonedCustomer));
        System.out.println("   customer.getAddress() != clonedCustomer.getAddress(): " +
                (customer.getAddress() != clonedCustomer.getAddress()) + " (Deep clone confirmed)");

        // 5. instanceof check
        System.out.println("\n5. Testing instanceof:");
        System.out.println("   a1 instanceof Account: " + (a1 instanceof Account));
        System.out.println("   a1 instanceof SavingsAccount: " + (a1 instanceof SavingsAccount));

        System.out.println("\n[Practical 3 Part B Completed Successfully]");
    }
}
