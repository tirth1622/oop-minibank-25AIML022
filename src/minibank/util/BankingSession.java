package minibank.util;

import java.time.LocalDateTime;

/**
 * Practical 8: AutoCloseable resource demonstrating try-with-resources in banking operations.
 * Manages an audit session and guarantees proper closure and cleanup.
 */
public class BankingSession implements AutoCloseable {
    private final String sessionId;
    private final LocalDateTime openedAt;
    private boolean closed = false;

    public BankingSession(String sessionId) {
        this.sessionId = sessionId;
        this.openedAt = LocalDateTime.now();
        System.out.println("[Session] >>> Started banking session: " + sessionId + " at " + openedAt);
    }

    public String getSessionId() {
        return sessionId;
    }

    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            System.out.println("[Session] <<< Safely closed banking session: " + sessionId + " (try-with-resources guaranteed)");
        }
    }
}
