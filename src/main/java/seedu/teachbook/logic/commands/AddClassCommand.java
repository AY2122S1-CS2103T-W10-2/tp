package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.commons.core.Messages.MESSAGE_DUPLICATE_CLASS;

import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.classobject.Class;

/**
 * Adds a student to the teachbook.
 */
public class AddClassCommand extends Command {

    public static final String COMMAND_WORD = "addClass";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds a class to the teachbook.\n"
            + "Parameters: CLASSNAME\n"
            + "Example: " + COMMAND_WORD + " " + "Class A";

    public static final String MESSAGE_SUCCESS = "New class added: %1$s";

    private final Class toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddClassCommand(Class classObj) {
        requireNonNull(classObj);
        toAdd = classObj;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasClass(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_CLASS);
        }

        model.addClass(toAdd);
        model.commitTeachBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd), false, false,
                true, true, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddClassCommand // instanceof handles nulls
                && toAdd.equals(((AddClassCommand) other).toAdd));
    }

}
