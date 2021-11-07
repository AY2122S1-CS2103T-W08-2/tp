package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ASSESSMENT;

import seedu.sourcecontrol.logic.commands.AddAssessmentCommand;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.assessment.Assessment;


/**
 * Parses input arguments and creates a new AddStudentCommand object
 */
public class AddAssessmentCommandParser implements Parser<AddAssessmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddStudentCommand
     * and returns an AddStudentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAssessmentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ASSESSMENT);

        if (argMultimap.getValue(PREFIX_ASSESSMENT).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAssessmentCommand.MESSAGE_USAGE));
        }

        Assessment assessment = ParserUtil.parseAssessment(argMultimap.getValue(PREFIX_ASSESSMENT).get());

        return new AddAssessmentCommand(assessment);
    }

}
