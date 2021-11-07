package seedu.sourcecontrol.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.sourcecontrol.logic.commands.CommandResult;
import seedu.sourcecontrol.logic.commands.exceptions.CommandException;
import seedu.sourcecontrol.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    // To keep track of input history so user can switch between past inputs
    private final List<String> pastInputs = new ArrayList<>();
    private int inputIndex = -1;
    private String currentInput;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            pastInputs.add(0, commandText);
            inputIndex = -1;
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Switches between past inputs when user keys up or down.
     */
    @FXML
    private void handleUserKeystroke(KeyEvent keyEvent) {
        if (inputIndex == -1) {
            // save the current input so it's accessible when returning
            currentInput = commandTextField.getText();
        }

        switch (keyEvent.getCode()) {
        case UP:
            if (inputIndex + 1 < pastInputs.size()) {
                inputIndex++;
                commandTextField.setText(pastInputs.get(inputIndex));
                commandTextField.end();
            }
            break;
        case DOWN:
            if (inputIndex > -1) {
                inputIndex--;
                String newText = inputIndex == -1 ? currentInput : pastInputs.get(inputIndex);
                commandTextField.setText(newText);
                commandTextField.end();
            }
            break;
        default:
            // nothing special to do otherwise
            break;
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.sourcecontrol.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
