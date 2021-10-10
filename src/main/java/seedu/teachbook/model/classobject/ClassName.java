package seedu.teachbook.model.classobject;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.commons.util.AppUtil.checkArgument;

/**
 * Represents a Class's name in the teachbook book.
 * Guarantees: immutable; is valid as declared in {@link #isValidClassName(String)}
 */
public class ClassName {

    public static final String MESSAGE_CONSTRAINTS =
            "Class names should only contain alphanumeric characters and spaces, and it should not be blank";

    /*
     * The first character of the teachbook must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String nameOfClass;

    /**
     * Constructs a {@code ClassName}.
     *
     * @param className A valid class name.
     */
    public ClassName(String className) {
        requireNonNull(className);
        checkArgument(isValidClassName(className), MESSAGE_CONSTRAINTS);
        nameOfClass = className;
    }

    /**
     * Returns true if a given string is a valid class name.
     */
    public static boolean isValidClassName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return nameOfClass;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof ClassName) {
            return nameOfClass.equals(((ClassName) other).nameOfClass);
        } else if (other instanceof ClassNameDescriptor) {
            return nameOfClass.equals(((ClassNameDescriptor) other).nameOfClass);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return nameOfClass.hashCode();
    }

}
