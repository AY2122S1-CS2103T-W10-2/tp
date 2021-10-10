package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.teachbook.model.Model;

/**
 * Lists all persons in the teach book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all students";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredStudentList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
