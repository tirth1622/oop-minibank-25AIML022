package minibank.persistence;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Practical 11: End-of-day reporting engine using Java NIO DirectoryStream and BufferedReader.
 * Aggregates all transaction logs, computes total deposits/withdrawals and net balances.
 */
public final class ReportGenerator {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ReportGenerator() {
        // Prevent instantiation
    }

    /**
     * Walks the specified logs folder, processes each .log file, and outputs a formatted report to reportFile.
     *
     * @param logsDir Directory containing transaction logs
     * @param reportFile Output destination file
     * @return Formatted summary report string
     * @throws IOException on I/O error
     */
    public static String generateReport(Path logsDir, Path reportFile) throws IOException {
        if (!Files.exists(logsDir)) {
            Files.createDirectories(logsDir);
        }

        long totalDeposits = 0L;
        long totalWithdrawals = 0L;
        int totalTransactions = 0;
        int processedFilesCount = 0;

        StringBuilder sb = new StringBuilder();
        sb.append("==============================================================\n");
        sb.append("              MINIBANK END-OF-DAY RECONCILIATION REPORT       \n");
        sb.append("==============================================================\n");
        sb.append("Report Timestamp : ").append(LocalDateTime.now().format(FORMATTER)).append("\n");
        sb.append("Logs Directory   : ").append(logsDir.toAbsolutePath()).append("\n");
        sb.append("--------------------------------------------------------------\n");
        sb.append("PROCESSED LOG FILES:\n");

        // Practical 11: Walk logs directory with Files.newDirectoryStream
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir)) {
            for (Path file : stream) {
                // Supplementary Practical 11: Skip files that do not end with .log or are empty
                String fileName = file.getFileName().toString();
                if (!fileName.endsWith(".log")) {
                    continue;
                }

                BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);
                if (attrs.size() == 0) {
                    continue; // Skip empty files
                }

                processedFilesCount++;
                sb.append(String.format("  - File: %-20s | Size: %6d bytes | Last Modified: %s\n",
                        fileName, attrs.size(), attrs.lastModifiedTime()));

                // Practical 11: Read each file line by line with BufferedReader
                try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty()) continue;

                        totalTransactions++;
                        // Clean up timestamp if present: e.g. "[2026-10-29 18:00:00] DEPOSIT AC0001 500"
                        String content = line;
                        if (content.startsWith("[") && content.contains("] ")) {
                            content = content.substring(content.indexOf("] ") + 2).trim();
                        }

                        String[] tokens = content.split("\\s+");
                        if (tokens.length >= 3) {
                            String type = tokens[0].toUpperCase();
                            try {
                                long amount = Long.parseLong(tokens[tokens.length - 1]);
                                if (type.contains("DEPOSIT")) {
                                    totalDeposits += amount;
                                } else if (type.contains("WITHDRAW")) {
                                    totalWithdrawals += amount;
                                } else if (type.contains("TRANSFER")) {
                                    // In a transfer within same bank, deposit and withdraw balance out
                                    totalDeposits += amount;
                                    totalWithdrawals += amount;
                                }
                            } catch (NumberFormatException ignored) {
                            }
                        }
                    }
                }
            }
        }

        long netChange = totalDeposits - totalWithdrawals;

        sb.append("--------------------------------------------------------------\n");
        sb.append("SUMMARY TOTALS:\n");
        sb.append(String.format("  Files Processed    : %d\n", processedFilesCount));
        sb.append(String.format("  Total Transactions : %d\n", totalTransactions));
        sb.append(String.format("  Total Deposits     : Rs. %,d\n", totalDeposits));
        sb.append(String.format("  Total Withdrawals  : Rs. %,d\n", totalWithdrawals));
        sb.append(String.format("  Net Fund Change    : Rs. %,d\n", netChange));
        sb.append("==============================================================\n");

        String report = sb.toString();

        // Write report to destination file
        if (reportFile != null) {
            if (reportFile.getParent() != null && !Files.exists(reportFile.getParent())) {
                Files.createDirectories(reportFile.getParent());
            }
            Files.writeString(reportFile, report, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        }

        return report;
    }
}
