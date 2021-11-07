package seedu.sourcecontrol.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;
import seedu.sourcecontrol.model.util.SampleDataUtil;

/**
 * A utility class to help with building Student objects.
 */
public class StudentBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_ID = "E0543947";

    private Name name;
    private Id id;
    private List<Group> groups;
    private Map<Assessment, Score> scores;
    private Set<Tag> tags;

    /**
     * Creates a {@code StudentBuilder} with the default details.
     */
    public StudentBuilder() {
        name = new Name(DEFAULT_NAME);
        id = new Id(DEFAULT_ID);
        groups = new ArrayList<>();
        scores = new LinkedHashMap<>();
        tags = new HashSet<>();
    }

    /**
     * Initializes the StudentBuilder with the data of {@code studentToCopy}.
     */
    public StudentBuilder(Student studentToCopy) {
        name = studentToCopy.getName();
        id = studentToCopy.getId();
        groups = new ArrayList<>(studentToCopy.getGroups());
        scores = new LinkedHashMap<>(studentToCopy.getScores());
        tags = new HashSet<>(studentToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Student} that we are building.
     */
    public StudentBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Id} of the {@code Student} that we are building.
     */
    public StudentBuilder withId(String id) {
        this.id = new Id(id);
        return this;
    }

    /**
     * Parses the {@code groups} into a {@code List<Group} and set it to the {@code Student} that we are building.
     */
    public StudentBuilder withGroups(String... groups) {
        this.groups = SampleDataUtil.getGroupList(groups);
        return this;
    }

    /**
     * Sets the {@code scores} of the {@code Student} that we are building.
     */
    public StudentBuilder withScores(Map<Assessment, Score> scores) {
        this.scores = scores;
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Student} that we are building.
     */
    public StudentBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    public Student build() {
        return new Student(name, id, groups, scores, tags);
    }

}
