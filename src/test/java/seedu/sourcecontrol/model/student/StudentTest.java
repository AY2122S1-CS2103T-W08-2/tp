package seedu.sourcecontrol.model.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_GROUP_RECITATION;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_ID_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_SCORES_BOB;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.ALICE;
import static seedu.sourcecontrol.testutil.TypicalStudents.AMY;
import static seedu.sourcecontrol.testutil.TypicalStudents.BOB;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.testutil.StudentBuilder;


public class StudentTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Student student = new StudentBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> student.getTags().remove(0));
    }

    @Test
    public void isSameStudent() {
        // same object -> returns true
        assertTrue(ALICE.isSameStudent(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameStudent(null));

        // same name and Id, all other attributes different -> returns true
        Student editedAlice = new StudentBuilder(ALICE).withGroups(VALID_GROUP_RECITATION).withScores(VALID_SCORES_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // different name, all other attributes same -> returns true
        editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertTrue(ALICE.isSameStudent(editedAlice));

        // different Id, all other attributes same -> returns false
        editedAlice = new StudentBuilder(ALICE).withId(VALID_ID_BOB).build();
        assertFalse(ALICE.isSameStudent(editedAlice));

        // name differs in case, all other attributes same -> returns true
        Student editedBob = new StudentBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSameStudent(editedBob));

        // Id differs in case, all other attributes same -> returns true
        editedBob = new StudentBuilder(BOB).withId(VALID_ID_BOB.toLowerCase()).build();
        assertEquals(editedBob.getId().value, VALID_ID_BOB);
        assertTrue(BOB.isSameStudent(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new StudentBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSameStudent(editedBob));
    }

    @Test
    public void isSameName() {
        //different name -> returns false
        assertFalse(AMY.isSameName(BOB));

        //different student, same name -> return true
        Student editedBob = new StudentBuilder(BOB).withName(VALID_NAME_AMY).build();
        assertTrue(AMY.isSameName(editedBob));

        //other student provided is null -> return false
        assertFalse(AMY.isSameName(null));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Student aliceCopy = new StudentBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different student -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Student editedAlice = new StudentBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different Id -> returns false
        editedAlice = new StudentBuilder(ALICE).withId(VALID_ID_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different groups -> returns false
        editedAlice = new StudentBuilder(ALICE).withGroups(VALID_GROUP_RECITATION).build();
        assertFalse(ALICE.equals(editedAlice));

        // different scores -> returns false
        editedAlice = new StudentBuilder(ALICE).withScores(VALID_SCORES_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new StudentBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }
}
