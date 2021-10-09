package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Student}.
 */
class JsonAdaptedStudent {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Student's %s field is missing!";

    private final String name;
    private final String id;
    private final List<String> groups = new ArrayList<>();
    // Note: Scores are stored on the JSON-adapted Assessments, and retrieved from there to avoid duplicate objects
    private final List<String> assessments = new ArrayList<>();
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedStudent} with the given student details.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("id") String id,
                              @JsonProperty("groups") List<String> groups,
                              @JsonProperty("assessments") List<String> assessments,
                              @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.id = id;
        if (groups != null) {
            this.groups.addAll(groups);
        }
        if (assessments != null) {
            this.assessments.addAll(assessments);
        }
        if (tagged != null) {
            this.tagged.addAll(tagged);
        }
    }

    /**
     * Converts a given {@code Student} into this class for Jackson use.
     */
    public JsonAdaptedStudent(Student source) {
        name = source.getName().fullName;
        id = source.getId().value;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
        groups.addAll(source.getGroups().stream()
                .map(group -> group.value)
                .collect(Collectors.toList()));
        assessments.addAll(source.getScores().keySet().stream()
                .map(Assessment::getName)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted student object into the model's {@code Student} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted student.
     */
    public Student toModelType(List<Group> groupList, List<Assessment> assessmentList) throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (id == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, ID.class.getSimpleName()));
        }
        if (!ID.isValidID(id)) {
            throw new IllegalValueException(ID.MESSAGE_CONSTRAINTS);
        }
        final ID modelId = new ID(id);

        final Map<Assessment, Score> modelScores = new HashMap<>();

        assessmentList.stream()
                .filter(assessment -> assessments.contains(assessment.getName()))
                .forEach(assessment -> modelScores.put(assessment, assessment.scores.get(modelId)));

        final List<Group> modelGroups = groupList.stream()
                .filter(group -> groups.contains(group.value))
                .collect(Collectors.toList());

        final List<Tag> studentTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            studentTags.add(tag.toModelType());
        }
        final Set<Tag> modelTags = new HashSet<>(studentTags);


        return new Student(modelName, modelId, modelGroups, modelScores, modelTags);
    }
}
