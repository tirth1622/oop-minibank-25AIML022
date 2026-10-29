package minibank.model;

/**
 * Practical 1: Enum representing all actions offered in MiniBank.
 * Enhanced through practicals 1 to 14.
 */
public enum MenuOption {
    OPEN_ACCOUNT(1, "Open New Account"),
    DEPOSIT(2, "Deposit Funds"),
    WITHDRAW(3, "Withdraw Funds"),
    TRANSFER(4, "Transfer Funds"),
    STATEMENT(5, "Generate Account Statement"),
    LIST_ACCOUNTS(6, "List All Accounts (Sorted)"),
    BATCH_PROCESSING(7, "Run Safe Batch Processor (Thread Pool)"),
    EOD_REPORT(8, "Generate End-of-Day Transaction Report (NIO)"),
    WORKING_HOURS(9, "View Bank Working Hours"),
    RUN_DIAGNOSTICS(10, "Run Reflection & Annotation Validator"),
    EXIT(11, "Exit Application");

    private final int optionNumber;
    private final String description;

    MenuOption(int optionNumber, String description) {
        this.optionNumber = optionNumber;
        this.description = description;
    }

    public int getOptionNumber() {
        return optionNumber;
    }

    public String getDescription() {
        return description;
    }

    public static MenuOption fromNumber(int number) {
        for (MenuOption opt : values()) {
            if (opt.optionNumber == number) {
                return opt;
            }
        }
        return null;
    }
}
