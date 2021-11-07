package seedu.sourcecontrol.testutil;

import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_TUTORIAL;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORES_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORES_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.assessment.Assessment;
import seedu.sourcecontrol.model.student.assessment.Score;

/**
 * A utility class containing a list of {@code Student} objects to be used in tests.
 */
public class TypicalStudents {

    public static final Student ALICE = new StudentBuilder().withName("Alice Pauline").withId("E0543948")
            .withGroups("T07A", "R01C")
            .withScores(Map.of(new Assessment("P01"), new Score("100")))
            .withTags("friends").build();
    public static final Student BENSON = new StudentBuilder().withName("Benson Meier").withId("E0473477")
            .withGroups("T04B", "R01D")
            .withScores(
                    Map.of(new Assessment("P01"), new Score("100"),
                            new Assessment("P02"), new Score("100"))
                    )
            .withTags("owesMoney", "friends").build();
    public static final Student CARL = new StudentBuilder().withName("Carl Kurz").withId("E0538201")
            .withGroups("T07D", "R03A")
            .withScores(Map.of(new Assessment("P01"), new Score("100"))).build();
    public static final Student DANIEL = new StudentBuilder().withName("Daniel Meier").withId("E0438241")
            .withGroups("T07D", "R02C").withTags("friends").build();
    public static final Student ELLE = new StudentBuilder().withName("Elle Meyer").withId("E0582305")
            .withScores(Map.of(new Assessment("P01"), new Score("73"))).build();
    public static final Student FIONA = new StudentBuilder().withName("Fiona Kunz").withId("E0537266")
            .withGroups("T03K", "R01C")
            .withScores(Map.of(new Assessment("P01"), new Score("84"))).build();
    public static final Student GEORGE = new StudentBuilder().withName("George Best").withId("E0555001")
            .withGroups("T05H", "R05B").build();

    // Manually added
    public static final Student HOON = new StudentBuilder().withName("Hoon Meier").withId("E0548201")
            .withGroups("T08A", "R07D")
            .withScores(Map.of(new Assessment("P01"), new Score("100"))).build();
    public static final Student IDA = new StudentBuilder().withName("Ida Mueller").withId("E0533301")
            .withGroups("T09E", "R06C")
            .withScores(Map.of(new Assessment("M01"), new Score("98"))).build();

    // Manually added - Student's details found in {@code CommandTestUtil}
    public static final Student AMY = new StudentBuilder().withName(VALID_NAME_AMY).withId(VALID_ID_AMY)
            .withGroups(VALID_GROUP_TUTORIAL)
            .withScores(VALID_SCORES_AMY)
            .withTags(VALID_TAG_FRIEND).build();
    public static final Student BOB = new StudentBuilder().withName(VALID_NAME_BOB).withId(VALID_ID_BOB)
            .withGroups(VALID_GROUP_RECITATION)
            .withScores(VALID_SCORES_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalStudents() {} // prevents instantiation

    /**
     * Returns an {@code SourceControl} with all the typical students.
     */
    public static SourceControl getTypicalSourceControl() {
        SourceControl ab = new SourceControl();
        for (Student student : getTypicalStudents()) {
            ab.addStudent(student);
        }
        return ab;
    }

    public static List<Student> getTypicalStudents() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
