package seedu.sourcecontrol.model.student.group;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.sourcecontrol.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.student.exceptions.DuplicateGroupException;
import seedu.sourcecontrol.model.student.exceptions.GroupNotFoundException;

public class GroupListTest {

    private static final Group TUTORIAL = new Group("T03A");
    private static final Group RECITATION = new Group("R05C");

    private final GroupList groups = new GroupList();

    @Test
    public void contains_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.contains(null));
    }

    @Test
    public void contains_groupNotInList_returnsFalse() {
        assertFalse(groups.contains(TUTORIAL));
    }

    @Test
    public void contains_groupInList_returnsTrue() {
        groups.add(TUTORIAL);
        assertTrue(groups.contains(TUTORIAL));
    }

    @Test
    public void contains_groupWithSameNameInList_returnsTrue() {
        groups.add(TUTORIAL);
        assertTrue(groups.contains(new Group(TUTORIAL.name)));
    }

    @Test
    public void add_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.add(null));
    }

    @Test
    public void add_duplicateGroup_success() {
        groups.add(TUTORIAL);
        groups.add(TUTORIAL);
        GroupList expectedGroupList = new GroupList();
        expectedGroupList.add(TUTORIAL);
        assertEquals(expectedGroupList, groups);
    }

    @Test
    public void setGroup_nullTargetGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.setGroup(null, TUTORIAL));
    }

    @Test
    public void setGroup_nullEditedGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.setGroup(TUTORIAL, null));
    }

    @Test
    public void setGroup_targetGroupNotInList_throwsGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () -> groups.setGroup(TUTORIAL, TUTORIAL));
    }

    @Test
    public void setGroup_editedGroupIsSameGroup_success() {
        groups.add(TUTORIAL);
        groups.setGroup(TUTORIAL, TUTORIAL);
        GroupList expectedGroupList = new GroupList();
        expectedGroupList.add(TUTORIAL);
        assertEquals(expectedGroupList, groups);
    }

    @Test
    public void setGroup_editedGroupHasSameName_success() {
        groups.add(TUTORIAL);
        Group editedPath01 = new Group(TUTORIAL.name);
        groups.setGroup(TUTORIAL, editedPath01);
        GroupList expectedGroupList = new GroupList();
        expectedGroupList.add(editedPath01);
        assertEquals(expectedGroupList, groups);
    }

    @Test
    public void setGroup_editedGroupHasDifferentIdentity_success() {
        groups.add(TUTORIAL);
        groups.setGroup(TUTORIAL, TUTORIAL);
        GroupList expectedUniqueGroupList = new GroupList();
        expectedUniqueGroupList.add(TUTORIAL);
        assertEquals(expectedUniqueGroupList, groups);
    }

    @Test
    public void remove_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.remove(null));
    }

    @Test
    public void remove_studentDoesNotExist_throwsGroupNotFoundException() {
        assertThrows(GroupNotFoundException.class, () -> groups.remove(TUTORIAL));
    }

    @Test
    public void remove_existingGroup_removesGroup() {
        groups.add(TUTORIAL);
        groups.remove(TUTORIAL);
        GroupList expectedUniqueGroupList = new GroupList();
        assertEquals(expectedUniqueGroupList, groups);
    }

    @Test
    public void setGroups_nullUniqueGroupList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.setGroups((GroupList) null));
    }

    @Test
    public void setGroups_groups_replacesOwnListWithProvidedUniqueGroupList() {
        groups.add(TUTORIAL);
        GroupList expectedUniqueGroupList = new GroupList();
        expectedUniqueGroupList.add(TUTORIAL);
        groups.setGroups(expectedUniqueGroupList);
        assertEquals(expectedUniqueGroupList, groups);
    }

    @Test
    public void setGroups_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> groups.setGroups((List<Group>) null));
    }

    @Test
    public void setGroups_list_replacesOwnListWithProvidedList() {
        groups.add(TUTORIAL);
        List<Group> providedList = new ArrayList<>();
        providedList.add(TUTORIAL);
        groups.setGroups(providedList);
        GroupList expectedGroupList = new GroupList();
        expectedGroupList.add(TUTORIAL);
        assertEquals(expectedGroupList, groups);
    }

    @Test
    public void setGroups_listWithDuplicateGroups_throwsDuplicateGroupException() {
        List<Group> listWithDuplicateGroups = Arrays.asList(TUTORIAL, TUTORIAL);
        assertThrows(DuplicateGroupException.class, () -> groups.setGroups(listWithDuplicateGroups));
    }
}
