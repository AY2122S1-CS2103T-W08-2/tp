package seedu.sourcecontrol.testutil;

import seedu.sourcecontrol.model.SourceControl;
import seedu.sourcecontrol.model.student.Student;

/**
 * A utility class to help with building SourceControl objects.
 * Example usage: <br>
 *     {@code SourceControl ab = new SourceControlBuilder().withStudent("John", "Doe").build();}
 */
public class SourceControlBuilder {

    private SourceControl sourceControl;

    public SourceControlBuilder() {
        sourceControl = new SourceControl();
    }

    public SourceControlBuilder(SourceControl sourceControl) {
        this.sourceControl = sourceControl;
    }

    /**
     * Adds a new {@code Student} to the {@code SourceControl} that we are building.
     */
    public SourceControlBuilder withStudent(Student student) {
        sourceControl.addStudent(student);
        return this;
    }

    public SourceControl build() {
        return sourceControl;
    }
}
