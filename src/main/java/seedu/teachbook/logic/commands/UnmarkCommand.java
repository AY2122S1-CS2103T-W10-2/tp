package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import seedu.teachbook.commons.core.Messages;
import seedu.teachbook.commons.core.index.Index;
import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.student.Attendance;
import seedu.teachbook.model.student.Student;

/**
 * Marks students, identified using the displayed indices from the teachbook, as absent.
 */
public class UnmarkCommand extends Command {

    public static final String COMMAND_WORD = "unmark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks one or students, identified by the index number "
            + "used in the displayed student list, as absent.\n"
            + "Parameters: INDEX1 [INDEX2]... [all] (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1, " + COMMAND_WORD + " 2 4 5, " + COMMAND_WORD + " all";

    public static final String MESSAGE_UNMARK_STUDENT_SUCCESS = "Marked %1$s absent at %2$s";
    public static final String MESSAGE_NOTHING_TO_UNMARK = "There is nothing to unmark as there are no students.";

    private final List<Index> targetIndices;
    private final boolean isAll;

    public UnmarkCommand(List<Index> targetIndices) {
        this.targetIndices = targetIndices;
        isAll = false;
    }

    public UnmarkCommand() {
        targetIndices = new ArrayList<>();
        isAll = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (isAll) {
            if (lastShownList.size() == 0) {
                throw new CommandException(MESSAGE_NOTHING_TO_UNMARK);
            }
            for (int i = lastShownList.size() - 1; i >= 0; i--) {
                targetIndices.add(Index.fromZeroBased(i));
            }
        } else {
            // Ensures all indices are valid before executing the command
            for (Index targetIndex : targetIndices) {
                if (targetIndex.getZeroBased() >= lastShownList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
                }
            }
        }

        LocalDateTime now = LocalDateTime.now();
        List<String> studentToUnmarkNames = new ArrayList<>();
        for (Index targetIndex : targetIndices) {
            Student studentToUnmark = lastShownList.get(targetIndex.getZeroBased());
            studentToUnmarkNames.add(studentToUnmark.getName().toString());
            Student editedStudent = new Student(studentToUnmark.getName(), studentToUnmark.getPhone(),
                    studentToUnmark.getStudentClass(), studentToUnmark.getEmail(),
                    studentToUnmark.getAddress(), studentToUnmark.getRemark(), studentToUnmark.getTags(),
                    new Attendance(false, now), studentToUnmark.getGrade());
            model.setStudent(studentToUnmark, editedStudent);
        }
        Collections.reverse(studentToUnmarkNames);

        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);

        model.commitTeachBook();
        return new CommandResult(String.format(MESSAGE_UNMARK_STUDENT_SUCCESS,
                String.join(", ", studentToUnmarkNames),
                now.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a", Locale.ENGLISH))),
                false, false, true, false, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UnmarkCommand // instanceof handles nulls
                && targetIndices.equals(((UnmarkCommand) other).targetIndices)); // state check
    }
}
