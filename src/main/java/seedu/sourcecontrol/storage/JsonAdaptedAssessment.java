package seedu.sourcecontrol.storage;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.id.Id;

public class JsonAdaptedAssessment {
    private final String name;
    private final Map<String, String> scores;

    /**
     * Constructs a {@code JsonAdaptedAssessment} with the given {@code assessmentName}.
     */
    @JsonCreator
    public JsonAdaptedAssessment(@JsonProperty("name") String name,
                                 @JsonProperty("scores") Map<String, String> scores) {
        this.name = name;
        this.scores = scores;
    }

    /**
     * Converts a given {@code Assessment} into this class for Jackson use.
     */
    public JsonAdaptedAssessment(Assessment source) {
        name = source.name;
        scores = new LinkedHashMap<>();
        for (Id id : source.scores.keySet()) {
            scores.put(id.value, source.scores.get(id).value);
        }
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Assessment} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted assessment.
     */
    public Assessment toModelType() throws IllegalValueException {
        if (!Assessment.isValidAssessment(name)) {
            throw new IllegalValueException(Assessment.MESSAGE_CONSTRAINTS);
        }

        Assessment assessment = new Assessment(name);

        // scores is not yet existent in the database
        if (scores == null) {
            return assessment;
        }

        for (String id : scores.keySet()) {
            if (!Id.isValidID(id)) {
                throw new IllegalValueException(Id.MESSAGE_CONSTRAINTS);
            }
            if (!Score.isValidScore(scores.get(id))) {
                throw new IllegalValueException(Score.MESSAGE_CONSTRAINTS);
            }
            assessment.scores.put(new Id(id), new Score(scores.get(id)));
        }
        return assessment;
    }
}
