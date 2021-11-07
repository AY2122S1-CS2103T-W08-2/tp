package seedu.sourcecontrol.testutil;

import java.util.LinkedHashMap;
import java.util.Map;

import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.id.Id;

/**
 * A utility class to help with building Assessment object.
 */
public class AssessmentBuilder {

    public static final String DEFAULT_VALUE = "P01";

    private Map<Id, Score> scores;
    private String value;

    /**
     * Creates a {@code AssessmentBuilder} with the default details.
     */
    public AssessmentBuilder() {
        scores = new LinkedHashMap<>();
        value = DEFAULT_VALUE;
    }

    /**
     * Initializes the AssessmentBuilder with the data of {@code assessmentToCopy}.
     */
    public AssessmentBuilder(Assessment assessmentToCopy) {
        scores = assessmentToCopy.getScores();
        value = assessmentToCopy.getName();
    }

    /**
     * Sets the {@code scores} of the {@code assessment} that we are building.
     */
    public AssessmentBuilder withScores(Map<String, String> scores) {
        Map<Id, Score> scoresAdapted = new LinkedHashMap<>();
        for (Map.Entry<String, String> score : scores.entrySet()) {
            scoresAdapted.put(new Id(score.getKey()), new Score(score.getValue()));
        }
        this.scores = scoresAdapted;
        return this;
    }

    /**
     * Sets the {@code value} of the {@code assessment} that we are building.
     */
    public AssessmentBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * Returns an {@code Assessment}.
     */
    public Assessment build() {
        Assessment assessment = new Assessment(value);
        assessment.getScores().putAll(scores);
        return assessment;
    }
}
