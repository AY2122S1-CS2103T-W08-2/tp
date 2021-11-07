package seedu.sourcecontrol.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.sourcecontrol.commons.exceptions.IllegalValueException;
import seedu.sourcecontrol.model.ReadOnlySourceControl;
import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.group.Group;

/**
 * An Immutable SourceControl that is serializable to JSON format.
 */
@JsonRootName(value = "sourcecontrol")
class JsonSerializableSourceControl {

    public static final String MESSAGE_DUPLICATE_STUDENT = "Student list contains duplicate student(s).";
    public static final String MESSAGE_DUPLICATE_ASSESSMENT = "Assessment list contains duplicate assessment(s).";
    public static final String MESSAGE_DUPLICATE_GROUP = "Group list contains duplicate group(s).";

    private final List<JsonAdaptedStudent> students = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();
    private final List<JsonAdaptedAssessment> assessments = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableSourceControl} with the given students.
     */
    @JsonCreator
    public JsonSerializableSourceControl(@JsonProperty("students") List<JsonAdaptedStudent> students,
                                         @JsonProperty("groups") List<JsonAdaptedGroup> groups,
                                         @JsonProperty("assessments") List<JsonAdaptedAssessment> assessments) {
        this.students.addAll(students);
        this.groups.addAll(groups);
        this.assessments.addAll(assessments);
    }

    /**
     * Converts a given {@code ReadOnlySourceControl} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableSourceControl}.
     */
    public JsonSerializableSourceControl(ReadOnlySourceControl source) {
        students.addAll(source.getStudentList().stream()
                .map(JsonAdaptedStudent::new)
                .collect(Collectors.toList()));
        groups.addAll(source.getGroupList().stream()
                .map(JsonAdaptedGroup::new)
                .collect(Collectors.toList()));
        assessments.addAll(source.getAssessmentList().stream()
                .map(JsonAdaptedAssessment::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Source Control into the model's {@code SourceControl} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public SourceControl toModelType() throws IllegalValueException {
        SourceControl sourceControl = new SourceControl();

        for (JsonAdaptedAssessment jsonAdaptedAssessment : assessments) {
            Assessment assessment = jsonAdaptedAssessment.toModelType();
            if (sourceControl.hasAssessment(assessment)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ASSESSMENT);
            }
            sourceControl.addAssessment(assessment);
        }

        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            Group group = jsonAdaptedGroup.toModelType();
            if (sourceControl.hasGroup(group)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_GROUP);
            }
            sourceControl.addGroup(group);
        }


        for (JsonAdaptedStudent jsonAdaptedStudent : students) {
            Student student = jsonAdaptedStudent.toModelType(
                    sourceControl.getGroupList(),
                    sourceControl.getAssessmentList());
            if (sourceControl.hasStudent(student)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_STUDENT);
            }
            sourceControl.addStudent(student);
        }

        return sourceControl;
    }

}
