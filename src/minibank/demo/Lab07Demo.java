package minibank.demo;

import minibank.model.Account;
import minibank.model.SavingsAccount;
import minibank.util.AnnotationValidator;

/**
 * Demonstration of Practical 7 - Part B:
 * - Custom annotations (@Id, @Positive, @MaxLength)
 * - Reflection validator inspecting declared annotations at runtime
 * - Negative balance validation test producing "must be > 0"
 */
public class Lab07Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("  PRACTICAL 7 (PART B) DEMONSTRATION: ANNOTATIONS & REFLECTION ");
        System.out.println("===============================================================");

        // Test with invalid account (negative balance -100)
        System.out.println("1. Validating invalid Account (balance = -100):");
        Account invalidAccount = new SavingsAccount("AC0001", "Riya Sharma", -100, true, 1000);
        String[] errors = AnnotationValidator.validate(invalidAccount);

        System.out.println("   Error count: " + errors.length);
        for (String err : errors) {
            System.out.println("   -> Captured Error: \"" + err + "\"");
        }

        // Test with valid account
        System.out.println("\n2. Validating valid Account (balance = 5000):");
        Account validAccount = new SavingsAccount("AC0002", "Aarav Patel", 5000, true, 1000);
        String[] validErrors = AnnotationValidator.validate(validAccount);
        System.out.println("   Error count: " + validErrors.length + " (Expected: 0)");

        // Supplementary: Test with MaxLength violation
        System.out.println("\n3. Supplementary Test: String length constraint check:");
        Account longNameAccount = new SavingsAccount("AC0003",
                "This name is intentionally made extremely long to exceed the maximum length limit of fifty characters",
                2000, true, 1000);
        String[] nameErrors = AnnotationValidator.validate(longNameAccount);
        for (String err : nameErrors) {
            System.out.println("   -> Captured Error: \"" + err + "\"");
        }

        System.out.println("\n[Practical 7 Part B Completed Successfully]");
    }
}
