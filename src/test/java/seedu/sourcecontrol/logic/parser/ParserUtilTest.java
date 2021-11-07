package seedu.sourcecontrol.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.parser.exceptions.ParseException;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_SCORE = "11 .01";
    private static final String INVALID_ASSESSMENT = " ";
    private static final String INVALID_ID = "E12345";
    private static final String INVALID_GROUP = " ";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_SCORE = "11.01";
    private static final String VALID_ASSESSMENT = "P01";
    private static final String VALID_ID = "E1234567";
    private static final String VALID_GROUP = "T02A";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_STUDENT, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_STUDENT, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseScore_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseScore((String) null));
    }

    @Test
    public void parseScore_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseScore(INVALID_SCORE));
    }

    @Test
    public void parseScore_validValueWithoutWhitespace_returnsScore() throws Exception {
        Score expectedScore = new Score(VALID_SCORE);
        assertEquals(expectedScore, ParserUtil.parseScore(VALID_SCORE));
    }

    @Test
    public void parseScore_validValueWithWhitespace_returnsTrimmedScore() throws Exception {
        String scoreWithWhitespace = WHITESPACE + VALID_SCORE + WHITESPACE;
        Score expectedScore = new Score(VALID_SCORE);
        assertEquals(expectedScore, ParserUtil.parseScore(scoreWithWhitespace));
    }

    @Test
    public void parseAssessment_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAssessment((String) null));
    }

    @Test
    public void parseAssessment_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAssessment(INVALID_ASSESSMENT));
    }

    @Test
    public void parseAssessment_validValueWithoutWhitespace_returnsAssessment() throws Exception {
        Assessment expectedAssessment = new Assessment(VALID_ASSESSMENT);
        assertEquals(expectedAssessment, ParserUtil.parseAssessment(VALID_ASSESSMENT));
    }

    @Test
    public void parseAssessment_validValueWithWhitespace_returnsTrimmedAssessment() throws Exception {
        String assessmentWithWhitespace = WHITESPACE + VALID_ASSESSMENT + WHITESPACE;
        Assessment expectedAssessment = new Assessment(VALID_ASSESSMENT);
        assertEquals(expectedAssessment, ParserUtil.parseAssessment(assessmentWithWhitespace));
    }

    @Test
    public void parseID_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseID((String) null));
    }

    @Test
    public void parseID_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseID(INVALID_ID));
    }

    @Test
    public void parseID_validValueWithoutWhitespace_returnsID() throws Exception {
        Id expectedId = new Id(VALID_ID);
        assertEquals(expectedId, ParserUtil.parseID(VALID_ID));
    }

    @Test
    public void parseID_validValueWithWhitespace_returnsTrimmedID() throws Exception {
        String idWithWhitespace = WHITESPACE + VALID_ID + WHITESPACE;
        Id expectedId = new Id(VALID_ID);
        assertEquals(expectedId, ParserUtil.parseID(idWithWhitespace));
    }

    @Test
    public void parseGroup_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseGroup((String) null));
    }

    @Test
    public void parseGroup_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseGroup(INVALID_GROUP));
    }

    @Test
    public void parseGroup_validValueWithoutWhitespace_returnsGroup() throws Exception {
        Group expectedGroup = new Group(VALID_GROUP);
        assertEquals(expectedGroup, ParserUtil.parseGroup(VALID_GROUP));
    }

    @Test
    public void parseGroup_validValueWithWhitespace_returnsTrimmedGroup() throws Exception {
        String groupWithWhitespace = WHITESPACE + VALID_GROUP + WHITESPACE;
        Group expectedGroup = new Group(VALID_GROUP);
        assertEquals(expectedGroup, ParserUtil.parseGroup(groupWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }
}
