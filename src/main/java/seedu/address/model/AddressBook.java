package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.AssessmentList;
import seedu.address.model.student.Group;
import seedu.address.model.student.GroupList;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;


/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameStudent comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

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

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Students in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
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
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
        setGroups(newData.getGroupList());
        setAssessments(newData.getAssessmentList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the address book.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the address book.
     * The student must not already exist in the address book.
     * Any new groups that the student has are added into the group list.
     */
    public void addStudent(Student s) {
        List<Group> studentGroups = s.getGroups();
        for (Group group : studentGroups) {
            if (!groups.contains(group)) {
                groups.add(group);
            }
        }
        students.add(s);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same as another existing student in the
     * address book.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        students.setStudent(target, editedStudent);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeStudent(Student key) {
        students.remove(key);
    }

    //// group-level operations

    /**
     * Returns true if a group with the same identity as {@code group} exists in the address book.
     */
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return groups.contains(group);
    }

    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group g) {
        groups.add(g);
    }

    //// assessment-level operations

    /**
     * Returns true if an assessment with the same identity as {@code assessment} exists in the address book.
     */
    public boolean hasAssessment(Assessment assessment) {
        requireNonNull(assessment);
        return assessments.contains(assessment);
    }

    /**
     * Adds an assessment to the address book.
     * The assessment must not already exist in the address book.
     */
    public void addAssessment(Assessment a) {
        assessments.add(a);
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
                || (other instanceof AddressBook // instanceof handles nulls
                && students.equals(((AddressBook) other).students)
                && groups.equals(((AddressBook) other).groups)
                && assessments.equals(((AddressBook) other).assessments));
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, groups, assessments);
    }
}
