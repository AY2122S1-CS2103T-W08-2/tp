package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Group;
import seedu.address.model.student.Student;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate student(s).";
    public static final String MESSAGE_DUPLICATE_ASSESSMENT = "Assessment list contains duplicate assessment(s).";
    public static final String MESSAGE_DUPLICATE_GROUP = "Group list contains duplicate group(s).";

    private final List<JsonAdaptedStudent> students = new ArrayList<>();
    private final List<JsonAdaptedGroup> groups = new ArrayList<>();
    private final List<JsonAdaptedAssessment> assessments = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given students.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("students") List<JsonAdaptedStudent> students,
                                       @JsonProperty("groups") List<JsonAdaptedGroup> groups,
                                       @JsonProperty("assessments") List<JsonAdaptedAssessment> assessments) {
        this.students.addAll(students);
        this.groups.addAll(groups);
        this.assessments.addAll(assessments);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
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
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();

        for (JsonAdaptedAssessment jsonAdaptedAssessment : assessments) {
            Assessment assessment = jsonAdaptedAssessment.toModelType();
            if (addressBook.hasAssessment(assessment)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_ASSESSMENT);
            }
            addressBook.addAssessment(assessment);
        }

        for (JsonAdaptedGroup jsonAdaptedGroup : groups) {
            Group group = jsonAdaptedGroup.toModelType();
            if (addressBook.hasGroup(group)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_GROUP);
            }
            addressBook.addGroup(group);
        }


        for (JsonAdaptedStudent jsonAdaptedStudent : students) {
            Student student = jsonAdaptedStudent.toModelType(
                    addressBook.getGroupList(),
                    addressBook.getAssessmentList());
            if (addressBook.hasStudent(student)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            addressBook.addStudent(student);
        }

        return addressBook;
    }

}
