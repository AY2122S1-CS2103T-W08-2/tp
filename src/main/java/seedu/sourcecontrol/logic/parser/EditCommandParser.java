package seedu.sourcecontrol.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_ID;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.sourcecontrol.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.logic.commands.EditCommand;
import seedu.sourcecontrol.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ID, PREFIX_GROUP, PREFIX_TAG);

        if (argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(ParserUtil.MESSAGE_INVALID_INDEX);
        }

        EditCommand.EditStudentDescriptor editStudentDescriptor = new EditStudentDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editStudentDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_ID).isPresent()) {
            editStudentDescriptor.setId(ParserUtil.parseID(argMultimap.getValue(PREFIX_ID).get()));
        }
        parseGroupsForEdit(argMultimap.getAllValues(PREFIX_GROUP)).ifPresent(editStudentDescriptor::setGroups);
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editStudentDescriptor::setTags);

        if (!editStudentDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editStudentDescriptor);
    }

    /**
     * Parses {@code Collection<String> groups} into a {@code List<Group>} if {@code groups} is non-empty.
     * If {@code groups} contain only one element which is an empty string, it will be parsed into a
     * {@code List<Group>} containing zero groups.
     */
    private Optional<List<Group>> parseGroupsForEdit(Collection<String> groups) throws ParseException {
        assert groups != null;

        if (groups.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> groupList = groups.size() == 1 && groups.contains("") ? Collections.emptyList() : groups;
        return Optional.of(ParserUtil.parseGroups(groupList));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

}
