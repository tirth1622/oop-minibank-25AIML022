package minibank.util;

import minibank.model.Command;
import minibank.model.TransactionType;

/**
 * Practical 4: Command parser that tokenises input strings into validated Command objects.
 */
public final class CommandParser {

    private CommandParser() {
        // Prevent instantiation
    }

    /**
     * Parses commands formatted as:
     * - "DEPOSIT AC0001 500"
     * - "WITHDRAW AC0001 200"
     * - "TRANSFER AC0001 AC0002 300"
     *
     * @param line Raw input string
     * @return Command record
     * @throws IllegalArgumentException if line is malformed or invalid
     */
    public static Command parse(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Error: Command line cannot be empty.");
        }

        String[] parts = line.trim().split("\\s+");
        if (parts.length < 3 || parts.length > 4) {
            throw new IllegalArgumentException("Error: Invalid command format. Expected 3 or 4 tokens, received " + parts.length +
                    ". (e.g. 'DEPOSIT AC0001 500' or 'TRANSFER AC0001 AC0002 500')");
        }

        TransactionType type;
        try {
            type = TransactionType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Error: Unknown transaction type '" + parts[0] +
                    "'. Allowed types: DEPOSIT, WITHDRAW, TRANSFER.");
        }

        if (type == TransactionType.TRANSFER) {
            if (parts.length == 4) {
                String sourceAcc = parts[1];
                String targetAcc = parts[2];
                long amount = parseAmount(parts[3]);
                return new Command(type, sourceAcc, targetAcc, amount);
            } else {
                // If 3 parts given for transfer, target is not specified
                String sourceAcc = parts[1];
                long amount = parseAmount(parts[2]);
                return new Command(type, sourceAcc, null, amount);
            }
        } else {
            if (parts.length != 3) {
                throw new IllegalArgumentException("Error: " + type + " requires exactly 3 parts: " + type + " <ACCOUNT> <AMOUNT>");
            }
            String acc = parts[1];
            long amount = parseAmount(parts[2]);
            return new Command(type, acc, amount);
        }
    }

    private static long parseAmount(String part) {
        if (!Validator.isValidAmount(part)) {
            throw new IllegalArgumentException("Error: Invalid transaction amount '" + part + "'. Amount must be a positive integer.");
        }
        return Long.parseLong(part);
    }
}
