package seedu.sourcecontrol.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.SCORE_DESCRIPTOR_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.SCORE_DESCRIPTOR_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailureWithFilteredListChange;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.logic.commands.AddScoreCommand.ScoreDescriptor;
import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.testutil.AssessmentBuilder;
import seedu.sourcecontrol.testutil.ScoreBuilder;
import seedu.sourcecontrol.testutil.ScoreDescriptorBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with Model) and unit tests for AddScoreCommand.
 */
public class AddScoreCommandTest {

    private final Model model = new ModelManager(getTypicalSourceControl(), new UserPrefs());

    private final Student simpleAmy = new StudentBuilder()
            .withName(VALID_NAME_AMY)
            .withId(VALID_ID_AMY).build();

    private final Assessment simpleAssessment = new AssessmentBuilder()
            .withValue(VALID_ASSESSMENT_AMY).build();

    private final Score simpleScore = new ScoreBuilder()
            .withValue(VALID_SCORE_AMY).build();

    @Test
    public void execute_addScoreByName_success() {
        ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();
        AddScoreCommand addScoreCommand = new AddScoreCommand(scoreDescriptor);

        model.addStudent(simpleAmy);
        model.addAssessment(simpleAssessment);

        Student expectedAmy = new StudentBuilder(simpleAmy)
                .withScores(Map.of(simpleAssessment, simpleScore)).build();

        Assessment expectedAssessment = new AssessmentBuilder(simpleAssessment)
                .withScores(Map.of(VALID_ID_AMY, VALID_SCORE_AMY)).build();

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.setStudent(simpleAmy, expectedAmy);
        expectedModel.setAssessment(simpleAssessment, expectedAssessment);

        String expectedMessage = String.format(AddScoreCommand.MESSAGE_ADD_SUCCESS, expectedAmy);

        assertCommandSuccess(addScoreCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addScoreById_success() {
        ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withId(VALID_ID_AMY)
                .withScore(VALID_SCORE_AMY).build();
        AddScoreCommand addScoreCommand = new AddScoreCommand(scoreDescriptor);

        model.addStudent(simpleAmy);
        model.addAssessment(simpleAssessment);

        Student expectedAmy = new StudentBuilder(simpleAmy)
                .withScores(Map.of(simpleAssessment, simpleScore)).build();

        Assessment expectedAssessment = new AssessmentBuilder(simpleAssessment)
                .withScores(Map.of(VALID_ID_AMY, VALID_SCORE_AMY)).build();

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.setStudent(simpleAmy, expectedAmy);
        expectedModel.setAssessment(simpleAssessment, expectedAssessment);

        String expectedMessage = String.format(AddScoreCommand.MESSAGE_ADD_SUCCESS, expectedAmy);

        assertCommandSuccess(addScoreCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_nonexistentAssessment_failure() {
        ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();
        AddScoreCommand addScoreCommand = new AddScoreCommand(scoreDescriptor);

        model.addStudent(simpleAmy);

        String expectedMessage = AddScoreCommand.MESSAGE_NONEXISTENT_ASSESSMENT;

        assertCommandFailure(addScoreCommand, model, expectedMessage);
    }

    @Test
    public void execute_nonexistentStudent_failure() {
        ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();
        AddScoreCommand addScoreCommand = new AddScoreCommand(scoreDescriptor);

        model.addAssessment(simpleAssessment);

        String expectedMessage = AddScoreCommand.MESSAGE_NONEXISTENT_STUDENT;

        assertCommandFailure(addScoreCommand, model, expectedMessage);
    }

    @Test
    public void execute_duplicateStudentName_failure() {
        ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore(VALID_SCORE_AMY).build();
        AddScoreCommand addScoreCommand = new AddScoreCommand(scoreDescriptor);

        Student duplicateAmy = new StudentBuilder()
                .withName(VALID_NAME_AMY)
                .withId(VALID_ID_BOB).build();

        model.addStudent(simpleAmy);
        model.addStudent(duplicateAmy);
        model.addAssessment(simpleAssessment);

        String expectedMessage = AddScoreCommand.MESSAGE_DUPLICATE_STUDENT_NAME;

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());

        assertCommandFailureWithFilteredListChange(addScoreCommand, model, expectedMessage, expectedModel,
                VALID_NAME_AMY);
    }

    @Test
    public void execute_replaceScore_success() {
        ScoreDescriptor scoreDescriptor = new ScoreDescriptorBuilder()
                .withAssessment(VALID_ASSESSMENT_AMY)
                .withName(VALID_NAME_AMY)
                .withScore("50").build();
        AddScoreCommand addScoreCommand = new AddScoreCommand(scoreDescriptor);

        Assessment assessmentWithAmyGraded = new AssessmentBuilder(simpleAssessment)
                .withScores(Map.of(VALID_ID_AMY, VALID_SCORE_AMY)).build();

        model.addStudent(simpleAmy);
        model.addAssessment(assessmentWithAmyGraded);

        Student expectedAmy = new StudentBuilder(simpleAmy)
                .withScores(Map.of(simpleAssessment, new Score("50"))).build();

        Assessment expectedAssessment = new AssessmentBuilder(assessmentWithAmyGraded)
                .withScores(Map.of(VALID_ID_AMY, "50")).build();

        String expectedMessage = String.format(
                AddScoreCommand.MESSAGE_UPDATE_SUCCESS, new Score(VALID_SCORE_AMY), new Score("50"), expectedAmy);

        Model expectedModel = new ModelManager(new SourceControl(model.getSourceControl()), new UserPrefs());
        expectedModel.setStudent(simpleAmy, expectedAmy);
        expectedModel.setAssessment(simpleAssessment, expectedAssessment);

        assertCommandSuccess(addScoreCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equal() {
        final AddScoreCommand standardCommand = new AddScoreCommand(SCORE_DESCRIPTOR_AMY);

        // same values -> returns true
        ScoreDescriptor copyDescriptor = new ScoreDescriptor(SCORE_DESCRIPTOR_AMY);
        AddScoreCommand commandWithSameValues = new AddScoreCommand(copyDescriptor);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> returns true
        assertEquals(standardCommand, standardCommand);

        // null -> returns false
        assertNotEquals(standardCommand, null);

        // different types -> returns false
        assertNotEquals(standardCommand, new ClearCommand());

        // different descriptor -> returns false
        assertNotEquals(standardCommand, new AddScoreCommand(SCORE_DESCRIPTOR_BOB));
    }
}
