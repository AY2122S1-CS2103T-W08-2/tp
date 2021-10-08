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
    // Keys: assessment names, values: scores
    private final Map<String, String> scores = new HashMap<>();
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedStudent} with the given student details.
     */
    @JsonCreator
    public JsonAdaptedStudent(@JsonProperty("name") String name, @JsonProperty("id") String id,
                              @JsonProperty("groups") List<String> groups,
                              @JsonProperty("scores") Map<String, String> scores,
                              @JsonProperty("tagged") List<JsonAdaptedTag> tagged) {
        this.name = name;
        this.id = id;
        if (groups != null) {
            this.groups.addAll(groups);
        }
        if (scores != null) {
            this.scores.putAll(scores);
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
                .map(Group::getName)
                .collect(Collectors.toList()));

        for (Score score : source.getScores()) {
            scores.put(score.getAssessment().getName(), score.getScore());
        }
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

        final List<Tag> studentTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            studentTags.add(tag.toModelType());
        }
        final Set<Tag> modelTags = new HashSet<>(studentTags);

        Student modelStudent = new Student(modelName, modelId, modelTags);

        groupList.forEach(group -> {
            if (groups.contains(group.getName())) {
                group.addStudent(modelStudent);
            }
        });

        assessmentList.forEach(assessment -> {
            if (scores.containsKey(assessment.getName())) {
                Score.updateScore(assessment, modelStudent, scores.get(assessment.getName()));
            }
        });

        return modelStudent;
    }
}
