package minibank.demo;

import java.util.Scanner;

/**
 * Interactive selector allowing examiners and students to run any individual
 * Practical's Part B demonstration (Labs 1 through 14) on demand.
 */
public class AllLabsRunner {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("""
                    =============================================================
                    |     MINIBANK - PRACTICAL PART B DEMONSTRATION SELECTOR    |
                    =============================================================
                      [1]  Lab 01: Menu Shell & BankInfo Record
                      [2]  Lab 02: Customer & Account Encapsulation
                      [3]  Lab 03: Object Methods (toString, equals, hashCode, clone)
                      [4]  Lab 04: Regex Validators & Command Parser
                      [5]  Lab 05: Inheritance & Polymorphism (Savings/Current/FD)
                      [6]  Lab 06: Interfaces (Transactable, InterestBearing, Lambdas)
                      [7]  Lab 07: Custom Annotations (@Id, @Positive) & Reflection
                      [8]  Lab 08: Milestone 1: Custom Exception Hierarchy
                      [9]  Lab 09: Multithreading & Race Condition Fix
                      [10] Lab 10: Thread Pools, Wait/Notify & Deadlock Avoidance
                      [11] Lab 11: Persistence (Serialization) & NIO Reporting
                      [12] Lab 12: Milestone 2: Collections Framework & Sorting
                      [13] Lab 13: Generic Repository<T> with Reflection
                      [14] Lab 14: Milestone 3: Full Capstone BankService Assembly
                      [15] RUN ALL LAB DEMOS SEQUENTIALLY
                      [0]  Exit Selector
                    =============================================================""");
            System.out.print("Select a lab to execute (0-15): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection. Please enter a number between 0 and 15.\n");
                continue;
            }

            try {
                switch (choice) {
                    case 0 -> {
                        running = false;
                        System.out.println("Exiting demo selector.");
                    }
                    case 1 -> Lab01Demo.main(new String[0]);
                    case 2 -> Lab02Demo.main(new String[0]);
                    case 3 -> Lab03Demo.main(new String[0]);
                    case 4 -> Lab04Demo.main(new String[0]);
                    case 5 -> Lab05Demo.main(new String[0]);
                    case 6 -> Lab06Demo.main(new String[0]);
                    case 7 -> Lab07Demo.main(new String[0]);
                    case 8 -> Lab08Demo.main(new String[0]);
                    case 9 -> Lab09Demo.main(new String[0]);
                    case 10 -> Lab10Demo.main(new String[0]);
                    case 11 -> Lab11Demo.main(new String[0]);
                    case 12 -> Lab12Demo.main(new String[0]);
                    case 13 -> Lab13Demo.main(new String[0]);
                    case 14 -> Lab14Demo.main(new String[0]);
                    case 15 -> runAll();
                    default -> System.out.println("Selection out of range (0-15).\n");
                }
            } catch (Exception e) {
                System.err.println("Error running demo: " + e.getMessage());
                e.printStackTrace();
            }

            if (running && choice != 0) {
                System.out.println("\nPress ENTER to return to selector menu...");
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                } else {
                    break;
                }
            }
        }
    }

    private static void runAll() throws Exception {
        System.out.println("\n>>> EXECUTING ALL 14 LAB DEMONSTRATIONS SEQUENTIALLY <<<\n");
        Lab01Demo.main(new String[0]);
        Lab02Demo.main(new String[0]);
        Lab03Demo.main(new String[0]);
        Lab04Demo.main(new String[0]);
        Lab05Demo.main(new String[0]);
        Lab06Demo.main(new String[0]);
        Lab07Demo.main(new String[0]);
        Lab08Demo.main(new String[0]);
        Lab09Demo.main(new String[0]);
        Lab10Demo.main(new String[0]);
        Lab11Demo.main(new String[0]);
        Lab12Demo.main(new String[0]);
        Lab13Demo.main(new String[0]);
        Lab14Demo.main(new String[0]);
        System.out.println("\n>>> ALL 14 LAB DEMONSTRATIONS COMPLETED SUCCESSFULLY! <<<");
    }
}
