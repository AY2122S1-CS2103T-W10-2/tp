package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import seedu.teachbook.commons.core.Messages;
import seedu.teachbook.commons.core.index.Index;
import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.attendance.Attendance;
import seedu.teachbook.model.student.Student;

/**
 * Marks a student identified using it's displayed index from the teachbook.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the student, identified by the index number used in the displayed student list, as present.\n"
            + "Parameters: INDEX1 [INDEX2]... (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1, " + COMMAND_WORD + " 2, 4, 5";

    public static final String MESSAGE_MARK_STUDENT_SUCCESS = "Marked Student(s): %1$s";

    private final ArrayList<Index> targetIndices;
    private final boolean isAll;

    public MarkCommand(ArrayList<Index> targetIndices) {
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
            for (int i = 0; i < lastShownList.size(); i++) {
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

        List<Student> studentsToMark = new ArrayList<>();
        for (Index targetIndex : targetIndices) {
            Student studentToMark = lastShownList.get(targetIndex.getZeroBased());
            studentsToMark.add(studentToMark);
            Student editedStudent = new Student(studentToMark.getName(), studentToMark.getPhone(),
                    studentToMark.getStudentClass(), studentToMark.getEmail(),
                    studentToMark.getAddress(), studentToMark.getRemark(), studentToMark.getTags(),
                    new Attendance(true, LocalDate.now()), studentToMark.getGrade());
            model.setStudent(studentToMark, editedStudent);
        }

        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_MARK_STUDENT_SUCCESS, studentsToMark), false,
                false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkCommand // instanceof handles nulls
                && targetIndices.equals(((MarkCommand) other).targetIndices)); // state check
    }
}
