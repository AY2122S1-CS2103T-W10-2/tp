package seedu.teachbook.logic.parser;

import static seedu.teachbook.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.teachbook.commons.core.index.Index;
import seedu.teachbook.logic.commands.MarkCommand;
import seedu.teachbook.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new MarkCommand object
 */
public class MarkCommandParser implements Parser<MarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MarkCommand
     * and returns a MarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MarkCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();

        if (trimmedArgs.equals("all")) {
            return new MarkCommand();
        }

        List<Index> indices;

        try {
            indices = ParserUtil.parseIndices(trimmedArgs);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkCommand.MESSAGE_USAGE), pe);
        }

        return new MarkCommand(indices);
    }
}
