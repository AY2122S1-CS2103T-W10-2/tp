package seedu.teachbook.logic.commands;

import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_GRADE;

import java.util.Arrays;

public class SetGradeCommand extends Command{

    public final String[] grades;
    public static final String COMMAND_WORD = "setGrade";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sets a grading system "
            + "Parameters: "
            + PREFIX_GRADE + "Grade "
            + PREFIX_GRADE + "A>B>C>D>E";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_GRADE_EXISTS = "Grade has already been set. Delete grades before setting again";

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public SetGradeCommand(String[] grades) {
        requireNonNull(grades);
        this.grades = grades;
    }

    public String gradeToString() {
        StringBuilder result = new StringBuilder();
        for(String grade : grades) {
            result.append(grade).append(" ");
        }
        return result.toString();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if(model.getGradeList().size() != 0) {
            throw new CommandException(MESSAGE_GRADE_EXISTS);
        }
        model.setGradeList(grades);
        return new CommandResult(String.format(MESSAGE_SUCCESS, gradeToString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof seedu.teachbook.logic.commands.SetGradeCommand // instanceof handles nulls
                && Arrays.equals(grades, ((SetGradeCommand) other).grades));
    }
}