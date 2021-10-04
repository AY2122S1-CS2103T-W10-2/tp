package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;

import seedu.address.logic.commands.SelectClassCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.classobject.ClassName;
import seedu.address.model.classobject.ClassNameContainsKeywordsPredicate;


/**
 * Parses input arguments and creates a new FindCommand object
 */
public class SelectClassCommandParser implements Parser<SelectClassCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
//    public FindClassCommand parse(String args) throws ParseException {
//        String trimmedArgs = args.trim();
//        if (trimmedArgs.isEmpty()) {
//            throw new ParseException(
//                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClassCommand.MESSAGE_USAGE));
//        }
//
//        String[] nameKeywords = trimmedArgs.split("\\s+");
//
//        return new FindClassCommand(new ClassNameContainsKeywordsPredicate(Arrays.asList(nameKeywords)));
//    }
    public SelectClassCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectClassCommand.MESSAGE_USAGE));
        }
        return new SelectClassCommand(new ClassName(trimmedArgs)); // TODO: better write how to parse "select" command
    }

}