package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedStudent.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.*;

public class JsonAdaptedStudentTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ID = "E123";
    private static final String INVALID_GROUP = "5";
    private static final String INVALID_ASSESSMENT = " ";
    private static final String INVALID_SCORE = "101";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_ID = BENSON.getId().toString();
    private static final List<String> VALID_GROUPS = BENSON.getGroups().stream()
            .map(Group::getName)
            .collect(Collectors.toList());
    private static final Map<String, String> VALID_SCORES = new HashMap<>();

    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    private static final List<Group> GROUP_LIST = BENSON.getGroups();
    private static final List<Assessment> ASSESSMENT_LIST = List.copyOf(
            BENSON.getScores().stream().map(Score::getAssessment).collect(Collectors.toList()));

    @BeforeAll
    static void beforeAll() {
        BENSON.getScores().forEach(score ->
                VALID_SCORES.put(score.getAssessment().getName(), score.getScore()));
    }

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedStudent student = new JsonAdaptedStudent(BENSON);
        assertEquals(BENSON, student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(INVALID_NAME, VALID_ID, VALID_GROUPS, VALID_SCORES, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(null, VALID_ID, VALID_GROUPS, VALID_SCORES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, INVALID_ID, VALID_GROUPS, VALID_SCORES, VALID_TAGS);
        String expectedMessage = ID.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, null, VALID_GROUPS, VALID_SCORES, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, ID.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_invalidGroups_notAddedToPerson() throws IllegalValueException {
        List<String> invalidGroups = new ArrayList<>(VALID_GROUPS);
        invalidGroups.add(INVALID_GROUP);
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, VALID_ID, invalidGroups, VALID_SCORES, VALID_TAGS);
        Student modelStudent = student.toModelType(GROUP_LIST, ASSESSMENT_LIST);
        assertTrue(modelStudent.getGroups().stream()
                .noneMatch(group -> group.getName().equals(INVALID_GROUP)));
    }

    @Test
    public void toModelType_invalidAssessment_notAddedToPerson() throws IllegalValueException {
        Map<String, String> invalidScores = new HashMap<>(VALID_SCORES);
        invalidScores.put(INVALID_ASSESSMENT, INVALID_SCORE);
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, VALID_ID, VALID_GROUPS, invalidScores, VALID_TAGS);
        Student modelStudent = student.toModelType(GROUP_LIST, ASSESSMENT_LIST);
        assertTrue(modelStudent.getScores().stream()
                .noneMatch(score -> score.getAssessment().getName().equals(INVALID_ASSESSMENT)));
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, VALID_ID, VALID_GROUPS, VALID_SCORES, invalidTags);
        assertThrows(IllegalValueException.class, () -> student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

}
