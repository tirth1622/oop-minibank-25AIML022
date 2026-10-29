package minibank.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

/**
 * Practical 11: Append-only transaction audit log using Java NIO Files.write().
 */
public final class TransactionLog {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TransactionLog() {
        // Prevent instantiation
    }

    /**
     * Practical 11: Appends a single log line using Files.write with CREATE and APPEND options.
     */
    public static void append(Path file, String line) throws IOException {
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }

        String timestampedLine = "[" + LocalDateTime.now().format(FORMATTER) + "] " + line;
        Files.write(
                file,
                Collections.singletonList(timestampedLine),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}
