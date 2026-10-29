package minibank.repository;

import minibank.model.annotation.Id;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Practical 13: Generic Repository<T> holding entity storage with reflection-based primary key (@Id) extraction.
 * Reusable for both Account and Customer or any annotated domain model.
 */
public class Repository<T> {

    // Thread-safe map for high-concurrency access across Practical 10, 12, 13, 14
    private final Map<String, T> storage = new ConcurrentHashMap<>();

    /**
     * Practical 13: Saves an entity by reading its @Id annotated field via reflection.
     *
     * @param entity The object to store
     * @return Saved entity
     * @throws IllegalArgumentException if entity has no readable @Id field
     */
    public T save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Cannot save null entity");
        }

        String id = extractId(entity);
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Entity ID extracted from @Id field cannot be null or empty");
        }

        storage.put(id, entity);
        return entity;
    }

    /**
     * Practical 13: Saves multiple entities at once using varargs.
     */
    @SafeVarargs
    public final void saveAll(T... items) {
        if (items != null) {
            for (T item : items) {
                if (item != null) {
                    save(item);
                }
            }
        }
    }

    /**
     * Finds an entity by primary key.
     */
    public T findById(String id) {
        if (id == null) return null;
        return storage.get(id);
    }

    /**
     * Returns an ArrayList containing all stored entities.
     */
    public ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Deletes an entity by primary key.
     */
    public boolean delete(String id) {
        if (id == null) return false;
        return storage.remove(id) != null;
    }

    /**
     * Practical 13 Supplementary: Returns number of stored items.
     */
    public int count() {
        return storage.size();
    }

    /**
     * Checks if key exists.
     */
    public boolean exists(String id) {
        return id != null && storage.containsKey(id);
    }

    /**
     * Clears all entities from repository.
     */
    public void clear() {
        storage.clear();
    }

    /**
     * Helper to read field annotated with @Id by traversing class hierarchy.
     */
    private String extractId(T entity) {
        Class<?> clazz = entity.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    field.setAccessible(true);
                    try {
                        Object val = field.get(entity);
                        return val != null ? val.toString() : null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to access @Id field: " + field.getName(), e);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new IllegalArgumentException("No @Id annotated field found in class " + entity.getClass().getName());
    }

    /**
     * Practical 13: Generic bounded method calculating the average of any Number array.
     * Demonstrates <N extends Number>.
     */
    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            return 0.0;
        }
        double sum = 0.0;
        for (N val : values) {
            if (val != null) {
                sum += val.doubleValue();
            }
        }
        return sum / values.length;
    }
}
