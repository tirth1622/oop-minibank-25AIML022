package minibank.persistence;

import minibank.model.Account;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

/**
 * Practical 11: Persistence layer using Java Object Serialization and try-with-resources.
 * Saves and reloads Account arrays and collections to disk.
 */
public final class StatePersister {

    private StatePersister() {
        // Prevent instantiation
    }

    /**
     * Practical 11: Saves an array of Account objects to a binary file using ObjectOutputStream.
     */
    public static void save(Account[] accounts, Path file) throws IOException {
        if (file.getParent() != null && !Files.exists(file.getParent())) {
            Files.createDirectories(file.getParent());
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(file)))) {
            oos.writeObject(accounts);
        }
    }

    /**
     * Practical 11: Loads an array of Account objects from a binary file using ObjectInputStream.
     */
    public static Account[] load(Path file) throws IOException, ClassNotFoundException {
        if (!Files.exists(file)) {
            return new Account[0];
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(file)))) {
            return (Account[]) ois.readObject();
        }
    }

    /**
     * Convenience method to save collections of accounts.
     */
    public static void saveCollection(Collection<Account> accounts, Path file) throws IOException {
        save(accounts.toArray(new Account[0]), file);
    }
}
