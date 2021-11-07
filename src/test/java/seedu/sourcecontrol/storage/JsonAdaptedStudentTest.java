package seedu.sourcecontrol.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.storage.JsonAdaptedStudent.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;

public class JsonAdaptedStudentTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_ID = "E123";
    private static final String INVALID_GROUP = "5";
    private static final String INVALID_ASSESSMENT = "8";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_ID = BENSON.getId().toString();
    private static final List<String> VALID_GROUPS = BENSON.getGroups().stream()
            .map(group -> group.name)
            .collect(Collectors.toList());
    private static final List<String> VALID_ASSESSMENTS = BENSON.getScores().keySet().stream()
            .map(Assessment::getName)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    // TODO: Make a typical groups, typical assessments, and link them all together
    private static final List<Group> GROUP_LIST = BENSON.getGroups();
    private static final List<Assessment> ASSESSMENT_LIST = List.copyOf(BENSON.getScores().keySet());

    @BeforeAll
    static void beforeAll() {
        ASSESSMENT_LIST.forEach(assessment ->
                assessment.setScore(BENSON.getId(), BENSON.getScores().get(assessment)));
    }

    @Test
    public void toModelType_validStudentDetails_returnsStudent() throws Exception {
        JsonAdaptedStudent student = new JsonAdaptedStudent(BENSON);
        assertEquals(BENSON, student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(INVALID_NAME, VALID_ID, VALID_GROUPS, VALID_ASSESSMENTS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(null, VALID_ID, VALID_GROUPS, VALID_ASSESSMENTS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_invalidId_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, INVALID_ID, VALID_GROUPS, VALID_ASSESSMENTS, VALID_TAGS);
        String expectedMessage = Id.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_nullId_throwsIllegalValueException() {
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, null, VALID_GROUPS, VALID_ASSESSMENTS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Id.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () ->
                student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

    @Test
    public void toModelType_invalidGroups_notAddedToStudent() throws IllegalValueException {
        List<String> invalidGroups = new ArrayList<>(VALID_GROUPS);
        invalidGroups.add(INVALID_GROUP);
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, VALID_ID, invalidGroups, VALID_ASSESSMENTS, VALID_TAGS);
        Student modelStudent = student.toModelType(GROUP_LIST, ASSESSMENT_LIST);
        assertTrue(modelStudent.getGroups().stream()
                .noneMatch(group -> group.name.equals(INVALID_GROUP)));
    }

    @Test
    public void toModelType_invalidAssessment_notAddedToStudent() throws IllegalValueException {
        List<String> invalidAssessments = new ArrayList<>(VALID_ASSESSMENTS);
        invalidAssessments.add(INVALID_ASSESSMENT);
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, VALID_ID, VALID_GROUPS, invalidAssessments, VALID_TAGS);
        Student modelStudent = student.toModelType(GROUP_LIST, ASSESSMENT_LIST);
        assertTrue(modelStudent.getScores().keySet().stream()
                .noneMatch(assessment -> assessment.name.equals(INVALID_ASSESSMENT)));
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedStudent student =
                new JsonAdaptedStudent(VALID_NAME, VALID_ID, VALID_GROUPS, VALID_ASSESSMENTS, invalidTags);
        assertThrows(IllegalValueException.class, () -> student.toModelType(GROUP_LIST, ASSESSMENT_LIST));
    }

}
