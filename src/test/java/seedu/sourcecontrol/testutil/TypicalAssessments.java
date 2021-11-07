package seedu.sourcecontrol.testutil;

import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ASSESSMENT_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORE_BOB;

import java.util.Map;

import seedu.sourcecontrol.model.student.assessment.Assessment;

/**
 * A utility class containing a list of {@code Assessment} objects to be used in tests.
 */
public class TypicalAssessments {

    public static final Assessment PATH_05 = new AssessmentBuilder()
            .withValue(VALID_ASSESSMENT_AMY)
            .withScores(Map.of(VALID_ID_AMY, VALID_SCORE_AMY)).build();

    public static final Assessment MISSION_01 = new AssessmentBuilder()
            .withValue(VALID_ASSESSMENT_BOB)
            .withScores(Map.of(VALID_ID_BOB, VALID_SCORE_BOB)).build();
}
