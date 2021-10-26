package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.teachbook.commons.core.Messages;
import seedu.teachbook.commons.core.index.GeneralIndex;
import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.classobject.Class;
import seedu.teachbook.model.classobject.ClassNameDescriptor;
import seedu.teachbook.model.classobject.exceptions.NoClassWithNameException;

/**
 * Deletes a class identified using its name.
 */
public class DeleteClassCommand extends Command {

    public static final String COMMAND_WORD = "deleteClass";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the class identified by its name.\n"
            + "Parameters: CLASSNAME (case-sensitive)\n"
            + "Example: " + COMMAND_WORD + " Class A";

    public static final String MESSAGE_DELETE_CLASS_SUCCESS = "Deleted Class: %1$s";

    private final ClassNameDescriptor className;

    public DeleteClassCommand(ClassNameDescriptor className) {
        this.className = className;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Class> classes = model.getUniqueClassList();
        GeneralIndex classIndex;

        try {
            classIndex = model.getIndexOfClass(className);
        } catch (NoClassWithNameException exception) {
            throw new CommandException(Messages.MESSAGE_CLASS_DOES_NOT_EXIST);
        }

        Class classToDelete = classes.get(classIndex.getZeroBased());
        model.deleteClass(classToDelete);
        model.commitTeachBook();
        return new CommandResult(String.format(MESSAGE_DELETE_CLASS_SUCCESS, classToDelete),
                false, false, true, true, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteClassCommand // instanceof handles nulls
                && className.equals(((DeleteClassCommand) other).className)); // state check
    }
}
