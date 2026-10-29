package minibank.demo;

import minibank.model.Account;
import minibank.model.Command;
import minibank.model.SavingsAccount;
import minibank.util.CommandParser;
import minibank.util.StatementFormatter;
import minibank.util.Validator;

/**
 * Demonstration of Practical 4 - Part B:
 * - Regex validation for mobile, email, PAN, IFSC
 * - Command tokenisation and parsing into Command records
 * - StatementFormatter using StringBuilder
 */
public class Lab04Demo {
    public static void main(String[] args) {
        System.out.println("===============================================================");
        System.out.println("     PRACTICAL 4 (PART B) DEMONSTRATION: REGEX & COMMANDS      ");
        System.out.println("===============================================================");

        // 1. Validator tests (one valid, one invalid)
        System.out.println("1. Testing Regex Validators:");
        System.out.println("   isValidMobile(\"9876543210\") : " + Validator.isValidMobile("9876543210") + " (Expected: true)");
        System.out.println("   isValidMobile(\"12345\")      : " + Validator.isValidMobile("12345") + " (Expected: false)");

        System.out.println("   isValidEmail(\"test@xyz.com\") : " + Validator.isValidEmail("test@xyz.com") + " (Expected: true)");
        System.out.println("   isValidEmail(\"invalid-email\") : " + Validator.isValidEmail("invalid-email") + " (Expected: false)");

        System.out.println("   isValidPan(\"ABCDE1234F\")    : " + Validator.isValidPan("ABCDE1234F") + " (Expected: true)");
        System.out.println("   isValidPan(\"12345ABCDE\")    : " + Validator.isValidPan("12345ABCDE") + " (Expected: false)");

        System.out.println("   isValidIfsc(\"SBIN0001234\")   : " + Validator.isValidIfsc("SBIN0001234") + " (Expected: true)");
        System.out.println("   isValidIfsc(\"SBIN1234\")      : " + Validator.isValidIfsc("SBIN1234") + " (Expected: false)");

        // 2 & 3. Command parser
        System.out.println("\n2. Testing CommandParser.parse(\"DEPOSIT AC0001 500\"):");
        String inputLine = "DEPOSIT AC0001 500";
        Command cmd = CommandParser.parse(inputLine);
        System.out.println("   Parsed Command Record: " + cmd);
        System.out.println("   Type           : " + cmd.type());
        System.out.println("   Account Number : " + cmd.accountNumber());
        System.out.println("   Amount         : " + cmd.amount());

        // 4. StatementFormatter test
        System.out.println("\n3. Testing StatementFormatter.buildStatement():");
        Account acc = new SavingsAccount("AC0001", "Riya Sharma", 7500, true, 1000);
        String statement = StatementFormatter.buildStatement(acc);
        System.out.println(statement);

        System.out.println("\n[Practical 4 Part B Completed Successfully]");
    }
}
