package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;

/**
 * Reverts the {@code model}'s address book to a later state.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_SUCCESS = "Redo success";
    public static final String MESSAGE_FAILURE = "No more commands to redo!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canRedoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.redoTeachBook();
        model.updateCurrentlySelectedClass(model.getCurrentlySelectedClassIndex());
        // TODO: Check if this showClass should be set to false
        return new CommandResult(MESSAGE_SUCCESS, false, false,
                true, true, false);
    }

}
