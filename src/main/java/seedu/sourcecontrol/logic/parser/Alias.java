package seedu.sourcecontrol.logic.parser;

import static seedu.sourcecontrol.commons.util.AppUtil.checkArgument;
import static seedu.sourcecontrol.commons.util.CollectionUtil.requireAllNonNull;

public class Alias {
    public static final String MESSAGE_CONSTRAINTS =
            "Aliases should only be one word comprised of only alphanumeric characters, and it should not be blank";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}]+";

    private final String commandWord;
    private final String aliasWord;

    /**
     * Creates an Alias with the given aliasWord and commandWord.
     */
    public Alias(String aliasWord, String commandWord) {
        requireAllNonNull(aliasWord, commandWord);
        checkArgument(isValidAlias(aliasWord), MESSAGE_CONSTRAINTS);
        this.commandWord = commandWord;
        this.aliasWord = aliasWord;
    }

    /**
     * Checks if the provided aliasWord matches the given constraints.
     */
    public static boolean isValidAlias(String aliasWord) {
        return aliasWord.matches(VALIDATION_REGEX);
    }

    /**
     * Replaces the first word of the userInput with the commandWord.
     */
    public String replaceFirst(String userInput) {
        assert userInput.startsWith(aliasWord);
        return userInput.replaceFirst(aliasWord, commandWord);
    }

    /**
     * Returns true if the alias does nothing (i.e. aliasWord maps to itself)
     */
    public boolean isRedundant() {
        return commandWord.equals(aliasWord);
    }

    public String getAliasWord() {
        return aliasWord;
    }

    public String getCommandWord() {
        return commandWord;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof Alias
                && aliasWord.equals(((Alias) other).aliasWord)
                && commandWord.equals(((Alias) other).commandWord));
    }
}
