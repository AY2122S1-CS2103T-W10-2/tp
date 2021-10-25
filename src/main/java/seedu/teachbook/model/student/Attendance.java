package seedu.teachbook.model.student;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Student's attendance in the teachbook.
 */
public class Attendance {

    public final boolean isPresent;
    public final LocalDateTime lastModified;

    /**
     * Constructs an {@code Attendance}.
     */
    public Attendance(boolean isPresent, LocalDateTime lastModified) {
        this.isPresent = isPresent;
        this.lastModified = lastModified;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Attendance // instanceof handles nulls
                && isPresent == ((Attendance) other).isPresent // state check
                && lastModified.equals(((Attendance) other).lastModified));
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPresent, lastModified);
    }

    @Override
    public String toString() {
        return (isPresent ? "Present " : "Absent ") + lastModified.toString();
    }
}
