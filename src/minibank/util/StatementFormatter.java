package minibank.util;

import minibank.model.Account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Practical 4: Generates a multi-line formatted account statement using StringBuilder.
 */
public final class StatementFormatter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private StatementFormatter() {
        // Prevent instantiation
    }

    public static String buildStatement(Account account) {
        if (account == null) {
            return "No account specified.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------\n");
        sb.append("                  OFFICIAL ACCOUNT STATEMENT                \n");
        sb.append("------------------------------------------------------------\n");
        sb.append("Generated On     : ").append(LocalDateTime.now().format(DATE_TIME_FORMATTER)).append("\n");
        sb.append("Account Number   : ").append(account.getAccountNumber()).append("\n");
        sb.append("Account Holder   : ").append(account.getOwnerName()).append("\n");
        sb.append("Account Type     : ").append(account.getAccountType()).append("\n");
        sb.append("Status           : ").append(account.isActive() ? "ACTIVE" : "INACTIVE").append("\n");
        sb.append("Current Balance  : Rs. ").append(account.getBalance()).append("\n");
        sb.append("Interest Rate    : ").append(account.interestRate()).append("% per annum\n");
        sb.append("Est. Yearly Int. : Rs. ").append(String.format("%.2f", account.yearlyInterest())).append("\n");
        sb.append("Est. Monthly Int.: Rs. ").append(String.format("%.2f", account.monthlyInterest())).append("\n");
        sb.append("------------------------------------------------------------\n");
        sb.append("Notice: Please report any discrepancy within 15 days.\n");
        sb.append("------------------------------------------------------------");

        return sb.toString();
    }
}
