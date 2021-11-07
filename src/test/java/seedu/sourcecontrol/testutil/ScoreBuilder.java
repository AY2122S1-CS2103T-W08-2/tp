package seedu.sourcecontrol.testutil;

import seedu.sourcecontrol.model.student.assessment.Score;

/**
 * A utility class to help with building Score objects.
 */
public class ScoreBuilder {

    public static final String DEFAULT_VALUE = "11.01";

    private String value;

    /**
     * Creates a {@code ScoreBuilder} with the default details.
     */
    public ScoreBuilder() {
        value = DEFAULT_VALUE;
    }

    /**
     * Initializes the ScoreBuilder with the data of {@code scoreToCopy}.
     */
    public ScoreBuilder(Score scoreToCopy) {
        value = scoreToCopy.getValue();
    }

    /**
     * Sets the {@code value} of the {@code score} that we are building.
     */
    public ScoreBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public Score build() {
        return new Score(value);
    }
}
