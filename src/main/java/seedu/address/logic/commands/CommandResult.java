package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.commands.ShowCommand.Info;

import java.nio.file.Path;
import java.util.Objects;

import javafx.scene.layout.Pane;
import javafx.scene.chart.*;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    private final Info info;

    private final Chart chart;

    private final Pane pane;

    private final Path savePath;

    /** Help information should be shown to the user. */
    private final boolean showHelp;

    /** The application should exit. */
    private final boolean exit;


    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, Info info, Pane pane, Path savePath) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.info = info;
        this.pane = pane;
        this.chart = null;
        this.savePath = savePath;
        this.showHelp = false;
        this.exit = false;
    }

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, Info info, Chart chart, Path savePath, boolean showHelp, boolean exit) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.info = info;
        this.chart = chart;
        this.pane = null;
        this.savePath = savePath;
        this.showHelp = showHelp;
        this.exit = exit;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, null, null, null, false, false);
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * {@code info}, {@code chart}, and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser, Info info, Chart chart, Path savePath) {
        this(feedbackToUser, info, chart, savePath, false, false);
    }

    public CommandResult(String feedbackToUser, Info info) {
        this(feedbackToUser, info, null, null, false, false);
    }

    public CommandResult(String feedbackToUser, Chart chart, Path savePath) {
        this(feedbackToUser, null, chart, savePath, false, false);
    }

    public CommandResult(String feedbackToUser, boolean showHelp, boolean exit) {
        this(feedbackToUser, null, null, null, showHelp, exit);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public Info getInfo() {
        return info;
    }

    public boolean hasInfo() {
        return info != null;
    }

    public Path getSavePath() {
        return savePath;
    }

    public boolean hasSavePath() {
        return savePath != null;
    }

    public Chart getChart() {
        return chart;
    }

    public boolean hasChart() {
        return chart != null;
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isExit() {
        return exit;
    }

    public boolean hasPane() {return pane != null; }
    public Pane getPane() {return pane; }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult)) {
            return false;
        }

        CommandResult otherCommandResult = (CommandResult) other;
        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && showHelp == otherCommandResult.showHelp
                && exit == otherCommandResult.exit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, showHelp, exit);
    }

}
