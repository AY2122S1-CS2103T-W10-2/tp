package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;

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
 * Marks students, identified using the displayed indices from the teachbook, as present.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks one or more students, identified by the index number "
            + "used in the displayed student list, as present.\n"
            + "Parameters: INDEX1 [INDEX2]... [all] (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1, " + COMMAND_WORD + " 2 4 5, " + COMMAND_WORD + " all";

    public static final String MESSAGE_MARK_STUDENT_SUCCESS = "Marked %1$s present at %2$s";
    public static final String MESSAGE_NOTHING_TO_MARK = "There is nothing to mark as there are no students.";


    private final List<Index> targetIndices;
    private final boolean isAll;

    public MarkCommand(List<Index> targetIndices) {
        this.targetIndices = targetIndices;
        isAll = false;
    }

    public MarkCommand() {
        targetIndices = new ArrayList<>();
        this.isAll = true;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (isAll) {
            if (lastShownList.size() == 0) {
                throw new CommandException(MESSAGE_NOTHING_TO_MARK);
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
        List<String> studentToMarkNames = new ArrayList<>();
        for (Index targetIndex : targetIndices) {
            Student studentToMark = lastShownList.get(targetIndex.getZeroBased());
            studentToMarkNames.add(studentToMark.getName().toString());
            Student editedStudent = new Student(studentToMark.getName(), studentToMark.getPhone(),
                    studentToMark.getStudentClass(), studentToMark.getEmail(),
                    studentToMark.getAddress(), studentToMark.getRemark(), studentToMark.getTags(),
                    new Attendance(true, now), studentToMark.getGrade());
            model.setStudent(studentToMark, editedStudent);
        }
        Collections.reverse(studentToMarkNames);

        model.commitTeachBook();
        return new CommandResult(String.format(MESSAGE_MARK_STUDENT_SUCCESS,
                String.join(", ", studentToMarkNames),
                now.format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a", Locale.ENGLISH))),
                false, false, true, false, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkCommand // instanceof handles nulls
                && targetIndices.equals(((MarkCommand) other).targetIndices)); // state check
    }
}
