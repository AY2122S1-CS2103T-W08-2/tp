package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
<<<<<<< HEAD
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
=======
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Score;
>>>>>>> v1.2-adapt-2
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
<<<<<<< HEAD
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
=======
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
>>>>>>> v1.2-adapt-2
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
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
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
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
<<<<<<< HEAD
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
=======
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
        ID expectedID = new ID(VALID_ID);
        assertEquals(expectedID, ParserUtil.parseID(VALID_ID));
    }

    @Test
    public void parseID_validValueWithWhitespace_returnsTrimmedID() throws Exception {
        String idWithWhitespace = WHITESPACE + VALID_ID + WHITESPACE;
        ID expectedID = new ID(VALID_ID);
        assertEquals(expectedID, ParserUtil.parseID(idWithWhitespace));
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
>>>>>>> v1.2-adapt-2
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
