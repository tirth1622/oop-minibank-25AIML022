package minibank.util;

import minibank.model.annotation.Audit;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Practical 14: Reflective audit logger that inspects service classes for @Audit annotations
 * and maintains an in-memory chronological audit trace.
 */
public final class AuditLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final List<String> AUDIT_LOGS = Collections.synchronizedList(new ArrayList<>());

    private AuditLogger() {
    }

    public static void log(Class<?> serviceClass, String methodName, String details) {
        try {
            Method[] methods = serviceClass.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName) && m.isAnnotationPresent(Audit.class)) {
                    Audit audit = m.getAnnotation(Audit.class);
                    String entry = String.format("[%s] AUDIT [%s] %s.%s() -> %s",
                            LocalDateTime.now().format(FORMATTER),
                            audit.action(),
                            serviceClass.getSimpleName(),
                            methodName,
                            details);
                    AUDIT_LOGS.add(entry);
                    return;
                }
            }
        } catch (Exception ignored) {
        }

        // Fallback log entry
        String entry = String.format("[%s] AUDIT [LOG] %s.%s() -> %s",
                LocalDateTime.now().format(FORMATTER),
                serviceClass.getSimpleName(),
                methodName,
                details);
        AUDIT_LOGS.add(entry);
    }

    public static List<String> getAuditLogs() {
        return new ArrayList<>(AUDIT_LOGS);
    }

    public static void printAuditSummary() {
        System.out.println("--- System Audit Trail (" + AUDIT_LOGS.size() + " events) ---");
        synchronized (AUDIT_LOGS) {
            for (String log : AUDIT_LOGS) {
                System.out.println(log);
            }
        }
    }
}
