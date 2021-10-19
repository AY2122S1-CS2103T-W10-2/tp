package seedu.teachbook.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.teachbook.commons.core.index.DefaultIndices.INDEX_NO_CLASS;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.teachbook.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.teachbook.logic.commands.exceptions.CommandException;
import seedu.teachbook.model.Model;
import seedu.teachbook.model.student.Student;

/**
 * Adds a student to the teachbook.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a student to the teachbook. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MESSAGE_SUCCESS = "New student added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This student already exists in the class";
    public static final String MESSAGE_NO_CLASS = "Add the first class before adding any student!";

    private final Student toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Student student) {
        requireNonNull(student);
        toAdd = student;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.getCurrentlySelectedClassIndex().equals(INDEX_NO_CLASS)) {
            throw new CommandException(MESSAGE_NO_CLASS);
        }

        if (model.hasStudent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addStudent(toAdd);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd), false, false,
                true, false);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
