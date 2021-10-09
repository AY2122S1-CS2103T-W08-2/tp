package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final ID id;

    // Data fields
    private final List<Group> groups = new ArrayList<>();
    private final Map<Assessment, Score> scores = new HashMap<>();
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, ID id, List<Group> groups, Map<Assessment, Score> scores, Set<Tag> tags) {
        requireAllNonNull(name, id, groups, scores, tags);
        this.name = name;
        this.id = id;
        this.groups.addAll(groups);
        this.scores.putAll(scores);
        this.tags.addAll(tags);
    }

    public Name getName() {
        return name;
    }

    public ID getId() {
        return id;
    }

    /**
     * Returns an immutable list of groups, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public List<Group> getGroups() {
        return Collections.unmodifiableList(groups);
    }

    /**
     * Returns an immutable map of assessment scores, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Map<Assessment, Score> getScores() {
        return Collections.unmodifiableMap(scores);
    }

    /**
     * Adds all scores provided
     */
    public void addScores(Map<Assessment, Score> scores) {
        // TODO: feel free to edit this as you need
        this.scores.putAll(scores);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both students have the same name.
     * This defines a weaker notion of equality between two students.
     */
    public boolean isSameStudent(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }

        return otherStudent != null
                && otherStudent.getName().equals(getName())
                && otherStudent.getId().equals(getId());
    }

    /**
     * Returns true if both students have the same identity and data fields.
     * This defines a stronger notion of equality between two students.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return otherStudent.getName().equals(getName())
                && otherStudent.getId().equals(getId())
                && otherStudent.getGroups().equals(getGroups())
                && otherStudent.getScores().equals(getScores())
                && otherStudent.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, id, groups, scores, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append("; NUSNET ID: ")
                .append(getId());

        List<Group> groups = getGroups();
        if (!groups.isEmpty()) {
            builder.append("; Groups: ");
            groups.forEach(builder::append);
        }

        Map<Assessment, Score> scores = getScores();
        if (!scores.isEmpty()) {
            builder.append("; Assessment Scores: ")
                    .append(scores);
        }

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }

}
