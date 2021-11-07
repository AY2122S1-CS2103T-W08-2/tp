package seedu.sourcecontrol.logic.commands;

import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.sourcecontrol.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.sourcecontrol.testutil.TypicalStudents.getTypicalSourceControl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.sourcecontrol.model.Model;
import seedu.sourcecontrol.model.ModelManager;
import seedu.sourcecontrol.model.UserPrefs;
import seedu.sourcecontrol.model.student.Student;
import seedu.sourcecontrol.testutil.StudentBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddStudentCommand}.
 */
public class AddStudentCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalSourceControl(), new UserPrefs());
    }

    @Test
    public void execute_newStudent_success() {
        Student validStudent = new StudentBuilder().build();

        Model expectedModel = new ModelManager(model.getSourceControl(), new UserPrefs());
        expectedModel.addStudent(validStudent);

        assertCommandSuccess(new AddStudentCommand(validStudent), model,
                String.format(AddStudentCommand.MESSAGE_SUCCESS, validStudent), expectedModel);
    }

    @Test
    public void execute_duplicateStudent_throwsCommandException() {
        Student studentInList = model.getSourceControl().getStudentList().get(0);
        assertCommandFailure(new AddStudentCommand(studentInList), model, AddStudentCommand.MESSAGE_DUPLICATE_STUDENT);
    }

}
