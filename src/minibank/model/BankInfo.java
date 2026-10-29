package minibank.model;

import java.io.Serializable;

/**
 * Practical 1: Record holding read-only bank header information.
 * Java auto-generates constructor, accessors, equals, hashCode, and toString.
 */
public record BankInfo(String name, String branch) implements Serializable {
    private static final long serialVersionUID = 1L;

    public String formattedHeader() {
        return """
               ==============================================================
               |                 %s - %s
               |              Console Core Banking System (v2.0)
               ==============================================================""".formatted(name, branch);
    }
}
