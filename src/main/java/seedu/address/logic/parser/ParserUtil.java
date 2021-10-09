package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Score;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
      * Parses a {@code String score} into a {@code Score}.
      * Leading and trailing whitespaces will be trimmed.
      *
      * @throws ParseException if the given {@code score} is invalid.
      */
    public static Score parseScore(String score) throws ParseException {
        requireNonNull(score);
        String trimmedScore = score.trim();
        if (!Score.isValidScore(trimmedScore)) {
            throw new ParseException(Score.MESSAGE_CONSTRAINTS);
        }
        return new Score(trimmedScore);
    }

    /**
     * Parses a {@code String assessment} into an {@code Assessment}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code assessment} is invalid.
     */
    public static Assessment parseAssessment(String assessment) throws ParseException {
        requireNonNull(assessment);
        String trimmedAssessment = assessment.trim();
        if (!Assessment.isValidAssessment(trimmedAssessment)) {
            throw new ParseException(Assessment.MESSAGE_CONSTRAINTS);
        }
        return new Assessment(trimmedAssessment);
    }

    /**
      * Parses a {@code String ID} into an {@code ID}.
      * Leading and trailing whitespaces will be trimmed.
      *
      * @throws ParseException if the given {@code ID} is invalid.
      */
    public static ID parseID(String id) throws ParseException {
        requireNonNull(id);
        String trimmedID = id.trim();
        if (!ID.isValidID(trimmedID)) {
            throw new ParseException(ID.MESSAGE_CONSTRAINTS);
        }
        return new ID(trimmedID);
    }

    /**
     * Parses a {@code String Group} into an {@code Group}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Group} is invalid.
     */
    public static Group parseGroup(String group) throws ParseException {
        requireNonNull(group);
        String trimmedGroup = group.trim();
        if (!Group.isValidGroup(trimmedGroup)) {
            throw new ParseException(Group.MESSAGE_CONSTRAINTS);
        }
        return new Group(trimmedGroup);
    }

    /**
     * Parses {@code Collection<String> groups} into a {@code List<Group>}.
     */
    public static List<Group> parseGroups(Collection<String> groups) throws ParseException {
        requireNonNull(groups);
        final List<Group> groupList = new ArrayList<>();
        for (String groupName : groups) {
            groupList.add(parseGroup(groupName));
        }
        return groupList;
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
