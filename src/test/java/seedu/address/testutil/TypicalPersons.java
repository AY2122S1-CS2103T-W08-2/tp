package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.address.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCORES_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SCORES_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import seedu.address.model.AddressBook;
import seedu.address.model.student.Assessment;
import seedu.address.model.student.Score;
import seedu.address.model.student.Student;

/**
 * A utility class containing a list of {@code Student} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Student ALICE = new PersonBuilder().withName("Alice Pauline").withId("E0543948")
            .withGroups("T07A", "R01C")
            .withScores(Map.of(new Assessment("P01"), new Score("100")))
            .withTags("friends").build();
    public static final Student BENSON = new PersonBuilder().withName("Benson Meier").withId("E0473477")
            .withGroups("T04B", "R01D")
            .withScores(
                    Map.of(new Assessment("P01"), new Score("100"),
                            new Assessment("P02"), new Score("100"))
                    )
            .withTags("owesMoney", "friends").build();
    public static final Student CARL = new PersonBuilder().withName("Carl Kurz").withId("E0538201")
            .withGroups("T07D", "R03A")
            .withScores(Map.of(new Assessment("P01"), new Score("100"))).build();
    public static final Student DANIEL = new PersonBuilder().withName("Daniel Meier").withId("E0438241")
            .withGroups("T07D", "R02C").withTags("friends").build();
    public static final Student ELLE = new PersonBuilder().withName("Elle Meyer").withId("E0582305")
            .withScores(Map.of(new Assessment("P01"), new Score("73"))).build();
    public static final Student FIONA = new PersonBuilder().withName("Fiona Kunz").withId("E0537266")
            .withGroups("T03K", "R01C")
            .withScores(Map.of(new Assessment("P01"), new Score("84"))).build();
    public static final Student GEORGE = new PersonBuilder().withName("George Best").withId("E0555001")
            .withGroups("T05H", "R05B").build();

    // Manually added
    public static final Student HOON = new PersonBuilder().withName("Hoon Meier").withId("E0548201")
            .withGroups("T08A", "R07D")
            .withScores(Map.of(new Assessment("P01"), new Score("100"))).build();
    public static final Student IDA = new PersonBuilder().withName("Ida Mueller").withId("E0533301")
            .withGroups("T09E", "R06C")
            .withScores(Map.of(new Assessment("M01"), new Score("98"))).build();

    // Manually added - Student's details found in {@code CommandTestUtil}
    public static final Student AMY = new PersonBuilder().withName(VALID_NAME_AMY).withId(VALID_ID_AMY)
            .withGroups(VALID_GROUP_TUTORIAL)
            .withScores(VALID_SCORES_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Student BOB = new PersonBuilder().withName(VALID_NAME_BOB).withId(VALID_ID_BOB)
            .withGroups(VALID_GROUP_RECITATION)
            .withScores(VALID_SCORES_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Student student : getTypicalPersons()) {
            ab.addStudent(student);
        }
        List<Assessment> assessments = AssessmentBuilder.createAssessmentListFromStudents(getTypicalPersons());
        for (Assessment assessment : assessments) {
            ab.addAssessment(assessment);
        }
        return ab;
    }

    public static List<Student> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
