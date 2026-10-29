package minibank.util;

import minibank.model.annotation.MaxLength;
import minibank.model.annotation.Positive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical 7: Reflection-based runtime validator.
 * Inspects declared fields for @Positive and @MaxLength annotations and collects violations.
 */
public final class AnnotationValidator {

    private AnnotationValidator() {
        // Prevent instantiation
    }

    /**
     * Inspects an object's fields via reflection and validates declared annotations.
     * Checks all fields in class hierarchy.
     *
     * @param obj The object instance to validate
     * @return Array of error message strings (empty if valid)
     */
    public static String[] validate(Object obj) {
        if (obj == null) {
            return new String[]{"Object to validate cannot be null"};
        }

        List<String> errors = new ArrayList<>();
        Class<?> currentClass = obj.getClass();

        // Traverse hierarchy up to Object to validate inherited annotated fields
        while (currentClass != null && currentClass != Object.class) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);

                    // Check @Positive annotation
                    if (field.isAnnotationPresent(Positive.class)) {
                        Positive positive = field.getAnnotation(Positive.class);
                        if (value instanceof Number num) {
                            if (num.doubleValue() <= 0) {
                                errors.add(field.getName() + ": " + positive.message() + " (current value: " + value + ")");
                            }
                        }
                    }

                    // Check @MaxLength annotation
                    if (field.isAnnotationPresent(MaxLength.class)) {
                        MaxLength maxLength = field.getAnnotation(MaxLength.class);
                        if (value instanceof String str) {
                            if (str.length() > maxLength.value()) {
                                errors.add(field.getName() + ": length (" + str.length() +
                                        ") exceeds maximum allowed length of " + maxLength.value());
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    errors.add("Unable to access field " + field.getName() + ": " + e.getMessage());
                }
            }
            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
