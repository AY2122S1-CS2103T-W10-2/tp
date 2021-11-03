package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.commons.core.index.DefaultIndices.INDEX_LIST_ALL;
import static seedu.teachbook.model.Model.PREDICATE_SHOW_ALL_STUDENTS;

import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.student.StudentIsAbsentPredicate;

/**
 * Lists all students in the currently selected class to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all students of the currently selected class or " // TODO: not really currently selected class
            + "lists all students from all classes by passing in an optional parameter \"all\".\n"
            + "Lists all absent students by passing in an optional parameter \"absent\".\n"
            + "Parameters: [all||absent]\n"
            + "Examples: " + COMMAND_WORD + ", " + COMMAND_WORD + " all" + ", " + COMMAND_WORD + " absent";

    public static final String MESSAGE_SUCCESS = "Listed all students";
    public static final String MESSAGE_SUCCESS_LIST_ALL = "Currently displaying all students from all classes";
    public static final String MESSAGE_SUCCESS_LIST_ABSENT = "Listed all absent students";
    public static final String MESSAGE_ALREADY_LIST_ALL = "Already displaying students from all classes\n"
            + "Use \"" + COMMAND_WORD + "\" to display the full list.";

    private final boolean isAll;
    private final boolean isAbsentee;

    public ListCommand(boolean isAll, boolean isAbsentee) {
        this.isAll = isAll;
        this.isAbsentee = isAbsentee;
    }

    public ListCommand() {
        this(false, false);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (isAll) {
            if (model.isListAll()) {
                throw new CommandException(MESSAGE_ALREADY_LIST_ALL);
            }
            model.updateCurrentlySelectedClass(INDEX_LIST_ALL);
            model.commitTeachBook();
            return new CommandResult(MESSAGE_SUCCESS_LIST_ALL, false, false, true, true);
        }

        if (isAbsentee) {
            model.updateFilteredStudentList(new StudentIsAbsentPredicate());
            model.commitTeachBook();
            return new CommandResult(MESSAGE_SUCCESS_LIST_ABSENT);
        }

        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_STUDENTS);
        model.commitTeachBook();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
