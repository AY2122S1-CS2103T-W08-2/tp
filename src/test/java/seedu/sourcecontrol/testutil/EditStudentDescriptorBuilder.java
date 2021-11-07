package seedu.sourcecontrol.testutil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.sourcecontrol.logic.commands.EditCommand.EditStudentDescriptor;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.model.student.group.Group;
import seedu.sourcecontrol.model.student.id.Id;
import seedu.sourcecontrol.model.student.name.Name;
import seedu.sourcecontrol.model.student.tag.Tag;

/**
 * A utility class to help with building EditStudentDescriptor objects.
 */
public class EditStudentDescriptorBuilder {

    private EditStudentDescriptor descriptor;

    public EditStudentDescriptorBuilder() {
        descriptor = new EditStudentDescriptor();
    }

    public EditStudentDescriptorBuilder(EditStudentDescriptor descriptor) {
        this.descriptor = new EditStudentDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditStudentDescriptor} with fields containing {@code student}'s details
     */
    public EditStudentDescriptorBuilder(Student student) {
        descriptor = new EditStudentDescriptor();
        descriptor.setName(student.getName());
        descriptor.setId(student.getId());
        descriptor.setGroups(student.getGroups());
        descriptor.setTags(student.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code id} of the {@code EditStudentDescriptor} that we are building.
     */
    public EditStudentDescriptorBuilder withId(String id) {
        descriptor.setId(new Id(id));
        return this;
    }

    /**
     * Parses the {@code groups} into a {@code List<Group>} and set it to the {@code EditStudentDescriptor}
     * that we are building.
     */
    public EditStudentDescriptorBuilder withGroups(String... groups) {
        List<Group> groupList = Stream.of(groups).map(Group::new).collect(Collectors.toList());
        descriptor.setGroups(groupList);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditStudentDescriptor}
     * that we are building.
     */
    public EditStudentDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditStudentDescriptor build() {
        return descriptor;
    }
}
