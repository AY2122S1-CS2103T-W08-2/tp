package seedu.sourcecontrol.model.student.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;
import static seedu.sourcecontrol.testutil.TypicalStudents.CARL;
import static seedu.sourcecontrol.testutil.TypicalStudents.ELLE;
import static seedu.sourcecontrol.testutil.TypicalStudents.FIONA;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.testutil.GroupBuilder;
import seedu.sourcecontrol.testutil.IdBuilder;
import seedu.sourcecontrol.testutil.StudentBuilder;

public class GroupTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Group(null));
    }

    @Test
    public void constructor_invalidGroupGroup_throwsIllegalArgumentException() {
        String invalidValue = "";
        assertThrows(IllegalArgumentException.class, () -> new Group(invalidValue));
    }

    @Test
    public void isValidGroup() {
        // null Group name
        assertThrows(NullPointerException.class, () -> Group.isValidGroup(null));

        // invalid Group name
        assertFalse(Group.isValidGroup("")); // empty string
        assertFalse(Group.isValidGroup(" ")); // spaces only

        // valid Group name
        assertTrue(Group.isValidGroup("T03C")); // upper case
        assertTrue(Group.isValidGroup("r05b")); // lower case
        assertTrue(Group.isValidGroup("r03A")); // mix of upper lower case
        assertTrue(Group.isValidGroup("r03 A")); // with spaces
        assertTrue(Group.isValidGroup("r")); // words only
        assertTrue(Group.isValidGroup("03")); // numbers only
    }

    @Test
    public void getValue() {
        assertEquals(new Group("T02A").getName(), "T02A");
    }

    @Test
    public void getStudents() {
        List<Id> studentList = Arrays.asList(CARL.getId(), ELLE.getId(), FIONA.getId());
        assertEquals(new Group("T02A", studentList).getStudents(), studentList);
    }

    @Test
    public void removeStudent() {
        Group group = new GroupBuilder().withStudents(CARL, ELLE, FIONA).build();
        group.removeStudent(ELLE.getId());
        Group result = new GroupBuilder().withStudents(CARL, FIONA).build();
        assertTrue(group.equals(result));
    }

    @Test
    public void hasStudent_null_throwsNullPointerException() {
        Group group = new GroupBuilder().build();
        assertThrows(NullPointerException.class, () -> group.hasStudent(null));
    }

    @Test
    public void hasStudent_notIncluded() {
        Id id = new IdBuilder().build();
        Group group = new GroupBuilder().build();
        assertFalse(() -> group.hasStudent(id));
    }

    @Test
    public void hasStudent_included() {
        Student student = new StudentBuilder().build();
        Group group = new GroupBuilder().withStudents(student).build();
        assertTrue(() -> group.hasStudent(student.getId()));
    }
}
