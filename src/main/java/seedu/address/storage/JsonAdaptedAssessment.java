package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Assessment;

public class JsonAdaptedAssessment {
    private final String assessmentName;

    /**
     * Constructs a {@code JsonAdaptedAssessment} with the given {@code assessmentName}.
     */
    @JsonCreator
    public JsonAdaptedAssessment(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    /**
     * Converts a given {@code Assessment} into this class for Jackson use.
     */
    public JsonAdaptedAssessment(Assessment source) {
        assessmentName = source.getName();
    }

    @JsonValue
    public String getAssessmentName() {
        return assessmentName;
    }

    /**
     * Converts this Jackson-friendly adapted tag object into the model's {@code Assessment} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted assessment.
     */
    public Assessment toModelType() throws IllegalValueException {
        if (!Assessment.isValidAssessment(assessmentName)) {
            throw new IllegalValueException(Assessment.MESSAGE_CONSTRAINTS);
        }
        return new Assessment(assessmentName);
    }
}
