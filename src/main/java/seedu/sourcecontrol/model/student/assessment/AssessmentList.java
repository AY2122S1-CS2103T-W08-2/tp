package seedu.sourcecontrol.model.student.assessment;

import static java.util.Objects.requireNonNull;
import static seedu.sourcecontrol.commons.util.CollectionUtil.equalsIgnoreOrder;
import static seedu.sourcecontrol.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.exceptions.AssessmentNotFoundException;
import seedu.sourcecontrol.model.student.exceptions.DuplicateAssessmentException;
import seedu.sourcecontrol.model.student.id.Id;

/**
 * Represents a list of assessments.
 * Guarantees: immutable; is valid as declared in {@link #isValidNumOfAssessments(String)} (String)}
 */
public class AssessmentList {

    public static final String MESSAGE_CONSTRAINTS =
            "Number of assessments should be a positive integer";
    public static final String VALIDATION_REGEX = "\\d+";
    public final List<Assessment> assessments = new ArrayList<>();

    /**
     * Returns true if a given string is a valid number of assessments.
     */
    public static boolean isValidNumOfAssessments(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if the list contains an equivalent assessment as the given argument.
     */
    public boolean contains(Assessment toCheck) {
        requireNonNull(toCheck);
        return assessments.stream().anyMatch(toCheck::isSameAssessment);
    }

    /**
     * Adds an assessment to the list.
     * The assessment must not already exist in the list.
     */
    public void add(Assessment toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateAssessmentException();
        }
        assessments.add(toAdd);
    }

    /**
     * Updates the assessment list accordingly to the student info.
     *
     * @see SourceControl#addStudent(Student)
     */
    public void update(Student toUpdate) {
        requireNonNull(toUpdate);

        Map<Assessment, Score> scores = toUpdate.getScores();

        for (Assessment assessment : scores.keySet()) {
            if (!contains(assessment)) {
                add(assessment);
            }
            int index = assessments.indexOf(assessment);
            Id id = toUpdate.getId();
            Score score = scores.get(assessment);
            assessments.get(index).setScore(id, score);
        }
    }

    /**
     * Removes the equivalent assessment from the list.
     * The assessment must exist in the list.
     */
    public void remove(Assessment toRemove) {
        requireNonNull(toRemove);
        if (!assessments.remove(toRemove)) {
            throw new AssessmentNotFoundException();
        }
    }

    /**
     * Removes all references to the student from the assessment list
     */
    public void removeStudent(Student toRemove) {
        requireNonNull(toRemove);

        for (Assessment assessment : assessments) {
            assessment.getScores().remove(toRemove.getId());
        }
    }

    /**
     * Removes all references to the previous student and adds in references to the edited student
     */
    public void replaceStudent(Student previous, Student edited) {
        requireAllNonNull(previous, edited);

        removeStudent(previous);
        update(edited);
    }

    /**
     * Replaces the assessment {@code target} in the list with {@code editedAssessment}.
     * {@code target} must exist in the list.
     * The assessment identity of {@code editedAssessment} must not be the same as another existing assessment
     * in the list.
     */
    public void setAssessment(Assessment target, Assessment editedAssessment) {
        requireAllNonNull(target, editedAssessment);

        int index = assessments.indexOf(target);
        if (index == -1) {
            throw new AssessmentNotFoundException();
        }

        if (!target.isSameAssessment(editedAssessment) && contains(editedAssessment)) {
            throw new DuplicateAssessmentException();
        }

        assessments.set(index, editedAssessment);
    }

    public void setAssessments(AssessmentList replacement) {
        requireNonNull(replacement);
        Collections.copy(assessments, replacement.assessments);
    }

    /**
     * Replaces the contents of this list with {@code assessments}.
     * {@code assessments} must not contain duplicate assessments.
     */
    public void setAssessments(List<Assessment> assessments) {
        requireAllNonNull(assessments);
        if (!assessmentsAreUnique(assessments)) {
            throw new DuplicateAssessmentException();
        }

        this.assessments.clear();
        this.assessments.addAll(assessments);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AssessmentList // instanceof handles nulls
                && equalsIgnoreOrder(assessments, ((AssessmentList) other).assessments)); // state check
    }

    @Override
    public int hashCode() {
        return assessments.hashCode();
    }

    /**
     * Returns true if {@code assessments} contains only unique assessments.
     */
    private boolean assessmentsAreUnique(List<Assessment> assessments) {
        for (int i = 0; i < assessments.size() - 1; i++) {
            for (int j = i + 1; j < assessments.size(); j++) {
                if (assessments.get(i).isSameAssessment(assessments.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
