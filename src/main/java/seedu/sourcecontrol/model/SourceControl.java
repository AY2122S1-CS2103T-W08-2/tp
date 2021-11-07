package seedu.sourcecontrol.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.UniqueStudentList;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.AssessmentList;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.group.GroupList;

/**
 * Wraps all data at the Source Control level
 * Duplicates are not allowed (by .isSameStudent comparison)
 */
public class SourceControl implements ReadOnlySourceControl {

    private final UniqueStudentList students;
    private final GroupList groups;
    private final AssessmentList assessments;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        students = new UniqueStudentList();
        groups = new GroupList();
        assessments = new AssessmentList();
    }

    public SourceControl() {}

    /**
     * Creates an SourceControl using the Students in the {@code toBeCopied}
     */
    public SourceControl(ReadOnlySourceControl toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setStudents(students);
    }

    /**
     * Replaces the contents of the group list with {@code groups}.
     * {@code groups} must not contain duplicate groups.
     */
    public void setGroups(List<Group> groups) {
        this.groups.setGroups(groups);
    }

    /**
     * Replaces the contents of the assessment list with {@code assessments}.
     * {@code assessments} must not contain duplicate assessments.
     */
    public void setAssessments(List<Assessment> assessments) {
        this.assessments.setAssessments(assessments);
    }

    /**
     * Resets the existing data of this {@code SourceControl} with {@code newData}.
     */
    public void resetData(ReadOnlySourceControl newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
        setGroups(newData.getGroupList());
        setAssessments(newData.getAssessmentList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the Source Control application.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the Source Control application.
     * The student must not already exist in the Source Control application.
     * Any new groups that the student has are added into the group list.
     * Any new assessments that the student has are added into the assessment list.
     */
    public void addStudent(Student s) {
        groups.update(s);
        assessments.update(s);
        students.add(s);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the Source Control application.
     * The student identity of {@code editedStudent} must not be the same as
     * another existing student in the Source Control application.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        // ID has changed, update IDs in assessments
        if (!target.getId().equals(editedStudent.getId())) {
            assessments.replaceStudent(target, editedStudent);
        }

        // Update group memberships
        groups.replaceStudent(target, editedStudent);

        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code SourceControl}.
     * {@code key} must exist in the Source Control application.
     */
    public void removeStudent(Student key) {
        students.remove(key);
        groups.removeStudent(key);
        assessments.removeStudent(key);
    }

    //// group-level operations

    /**
     * Returns true if a group with the same identity as {@code group} exists in the Source Control application.
     */
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return groups.contains(group);
    }

    /**
     * Adds a group to the Source Control application.
     * The group must not already exist in the Source Control application.
     */
    public void addGroup(Group g) {
        groups.add(g);
    }

    /**
     * Returns group in Source Control with same identity as {@code groupToMatch} if exists.
     */
    public Group getGroup(Group groupToMatch) {
        requireNonNull(groupToMatch);
        for (Group group : getGroupList()) {
            if (group.equals(groupToMatch)) {
                return group;
            }
        }
        return null;
    }

    //// assessment-level operations

    /**
     * Returns true if an assessment with the same identity as {@code assessment} exists in
     * the Source Control application.
     */
    public boolean hasAssessment(Assessment assessment) {
        requireNonNull(assessment);
        return assessments.contains(assessment);
    }

    /**
     * Returns assessment in Source Control with same identity as {@code assessmentToMatch} if exists.
     */
    public Assessment getAssessment(Assessment assessmentToMatch) {
        requireNonNull(assessmentToMatch);
        for (Assessment assessment : getAssessmentList()) {
            if (assessment.equals(assessmentToMatch)) {
                return assessment;
            }
        }
        return null;
    }

    /**
     * Adds an assessment to the Source Control application.
     * The assessment must not already exist in the Source Control application.
     */
    public void addAssessment(Assessment a) {
        assessments.add(a);
    }

    /**
     * Replaces the given assessment {@code target} in the list with {@code editedAssessment}.
     * {@code target} must exist in the Source Control application.
     * The assessment identity of {@code editedStudent} must not be the same as
     * another existing assessment in the Source Control application.
     */
    public void setAssessment(Assessment target, Assessment editedAssessment) {
        requireNonNull(editedAssessment);

        assessments.setAssessment(target, editedAssessment);
    }

    //// util methods

    @Override
    public String toString() {
        return students.asUnmodifiableObservableList().size() + " students";
        // TODO: refine later
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public List<Group> getGroupList() {
        return groups.groups;
    }

    @Override
    public List<Assessment> getAssessmentList() {
        return assessments.assessments;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SourceControl // instanceof handles nulls
                && students.equals(((SourceControl) other).students)
                && groups.equals(((SourceControl) other).groups)
                && assessments.equals(((SourceControl) other).assessments));
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, groups, assessments);
    }
}
