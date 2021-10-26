package seedu.teachbook.model.student;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a Student's attendance in the teachbook.
 */
public class Attendance {

    public static final String MESSAGE_CONSTRAINTS =
            "Attendance should contain either the word 'Present' or 'Absent', followed by a whitespace, "
                    + "followed by a datetime in the system clock format e.g., Present 2021-10-25T20:21:03.184652600,"
                    + "Absent 2021-10-25T20:21:03.186648100";
    public final boolean isPresent;
    public final LocalDateTime lastModified;

    /**
     * Constructs an {@code Attendance}.
     */
    public Attendance(boolean isPresent, LocalDateTime lastModified) {
        this.isPresent = isPresent;
        this.lastModified = lastModified;
    }


    public boolean isPresent() {
        return isPresent;
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

    public String getFormattedString() {
        return isPresent
                ? "Present "
                + lastModified.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a", Locale.ENGLISH))
                : "Absent";
    }

    @Override
    public String toString() {
        return (isPresent ? "Present " : "Absent ") + lastModified.toString();
    }
}
