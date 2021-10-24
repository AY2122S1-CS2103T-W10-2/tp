package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.commons.core.Messages.MESSAGE_INVALID_GRADE;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_GRADE;
import static seedu.teachbook.model.Model.PREDICATE_SHOW_ALL_STUDENTS;
import static seedu.teachbook.model.gradeobject.GradingSystem.NOT_GRADED;

import java.util.List;

import seedu.teachbook.commons.core.Messages;
import seedu.teachbook.commons.core.index.Index;
import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.gradeobject.Grade;
import seedu.teachbook.model.student.Student;

public class GradeCommand extends Command {

    public static final String COMMAND_WORD = "grade";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the grade of the student identified "
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_GRADE + "Grade] "
            + "Example: " + COMMAND_WORD + " 1 " + "g/A";
    public static final String MESSAGE_GRADING_SYSTEM_NOT_SET =
            "Set a grading system before editing any grade";
    public static final String MESSAGE_GRADE_PERSON_SUCCESS = "Graded Student: %1$s";

    private final Grade grade;
    private final Index index;

    public GradeCommand(Index index, Grade grade) {
        this.grade = grade;
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToEdit = lastShownList.get(index.getZeroBased());
        if (!model.hasExistingGradingSystem() && !grade.value.equals(NOT_GRADED)) {
            throw new CommandException(MESSAGE_GRADING_SYSTEM_NOT_SET);
        }

        if (!model.isValidGrade(grade)) {
            throw new CommandException(String.format(MESSAGE_INVALID_GRADE, model.getGradingSystem()));
        }
        Student editedStudent = new Student(studentToEdit.getName(), studentToEdit.getPhone(), studentToEdit.getEmail(),
                                            studentToEdit.getAddress(), studentToEdit.getRemark(),
                                            studentToEdit.getTags(), grade);
        editedStudent.setStudentClass(studentToEdit.getStudentClass());
        model.setStudent(studentToEdit, editedStudent);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.commitTeachBook();
        return new CommandResult(String.format(MESSAGE_GRADE_PERSON_SUCCESS, editedStudent));
    }
}
