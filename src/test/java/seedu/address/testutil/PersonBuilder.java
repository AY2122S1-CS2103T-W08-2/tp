package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.student.Group;
import seedu.address.model.student.ID;
import seedu.address.model.student.Name;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Student objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_ID = "E0543948";

    private Name name;
    private ID id;
    private List<Group> groups;
    private List<Score> scores;
    private Set<Tag> tags;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        id = new ID(DEFAULT_ID);
        groups = new ArrayList<>();
        scores = new ArrayList<>();
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code studentToCopy}.
     */
    public PersonBuilder(Student studentToCopy) {
        name = studentToCopy.getName();
        id = studentToCopy.getId();
        groups = new ArrayList<>(studentToCopy.getGroups());
        scores = new ArrayList<>(studentToCopy.getScores());
        tags = new HashSet<>(studentToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Student} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code ID} of the {@code Student} that we are building.
     */
    public PersonBuilder withId(String id) {
        this.id = new ID(id);
        return this;
    }

    /**
     * Parses the {@code groups} into a {@code List<Group} and set it to the {@code Student} that we are building.
     */
    public PersonBuilder withGroups(String... groups) {
        this.groups = Arrays.stream(groups)
                .map(Group::new)
                .collect(Collectors.toList());

        return this;
    }

    /**
     * Sets the {@code scores} of the {@code Student} that we are building.
     */
    public PersonBuilder withScores(List<Score> scores) {
        this.scores = scores;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Student} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Student build() {
        Student newStudent = new Student(name, id, tags);
        groups.forEach(newStudent::addGroup);
        scores.forEach(score -> Score.updateScore(score.getAssessment(), newStudent, score.getScore()));
        return newStudent;
    }

}
