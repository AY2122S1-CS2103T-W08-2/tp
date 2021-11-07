package seedu.sourcecontrol.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.CollectionUtil.requireAllNonNull;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.sourcecontrol.commons.core.index.Index;
import seedu.sourcecontrol.commons.util.FileUtil;
import seedu.sourcecontrol.commons.util.StringUtil;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer. ";
    public static final String MESSAGE_EMPTY_PATH = "Path should not be empty. ";
    public static final String MESSAGE_INVALID_PATH = "Path provided is invalid. ";
    public static final String MESSAGE_DIRECTORY = "Path provided should not be for a directory. ";
    public static final String MESSAGE_WRONG_EXTENSION = "Path provided should end with %1$s. ";

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
     * Parses {@code Collection<String> names} into a {@code List<Name>}.
     */
    public static List<Name> parseNames(Collection<String> names) throws ParseException {
        requireNonNull(names);
        final List<Name> nameList = new ArrayList<>();
        for (String name : names) {
            nameList.add(parseName(name));
        }
        return nameList;
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
     * Parses a {@code String Id} into an {@code Id}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Id} is invalid.
     */
    public static Id parseID(String id) throws ParseException {
        requireNonNull(id);
        String trimmedID = id.trim();
        if (!Id.isValidID(trimmedID)) {
            throw new ParseException(Id.MESSAGE_CONSTRAINTS);
        }
        return new Id(trimmedID);
    }

    /**
     * Parses {@code Collection<String> ids} into a {@code List<Id>}.
     */
    public static List<Id> parseIds(Collection<String> ids) throws ParseException {
        requireNonNull(ids);
        final List<Id> idList = new ArrayList<>();
        for (String id : ids) {
            idList.add(parseID(id));
        }
        return idList;
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

    /**
     * Parses a given path into a path relative to the app's folder, and ensures that the path has the right extension.
     */
    public static Path parsePath(String path, String extension) throws ParseException {
        requireAllNonNull(path, extension);
        if (path.equals("")) {
            throw new ParseException(MESSAGE_EMPTY_PATH);
        }

        if (!FileUtil.isValidPath(path)) {
            throw new ParseException(MESSAGE_INVALID_PATH);
        }

        if (new File(path).isDirectory()) {
            throw new ParseException(MESSAGE_DIRECTORY);
        }

        if (!path.endsWith(extension)) {
            throw new ParseException(String.format(MESSAGE_WRONG_EXTENSION, extension));
        }

        return FileUtil.pathOf(path);
    }
}
