package seedu.sourcecontrol.commons.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Writes and reads files
 */
public class FileUtil {

    private static final String CHARSET = "UTF-8";
    private static Path enclosingFolder;

    public static boolean isFileExists(Path file) {
        return Files.exists(file) && Files.isRegularFile(file);
    }

    /**
     * Returns true if {@code path} can be converted into a {@code Path} via {@link Paths#get(String)},
     * otherwise returns false.
     * @param path A string representing the file path. Cannot be null.
     */
    public static boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException ipe) {
            return false;
        }
        return true;
    }

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     * @throws IOException if the file or directory cannot be created.
     */
    public static void createIfMissing(Path file) throws IOException {
        if (!isFileExists(file)) {
            createFile(file);
        }
    }

    /**
     * Creates a file if it does not exist along with its missing parent directories.
     */
    public static void createFile(Path file) throws IOException {
        if (Files.exists(file)) {
            return;
        }

        createParentDirsOfFile(file);

        Files.createFile(file);
    }

    /**
     * Creates parent directories of file if it has a parent directory
     */
    public static void createParentDirsOfFile(Path file) throws IOException {
        Path parentDir = file.getParent();

        if (parentDir != null) {
            Files.createDirectories(parentDir);
        }
    }

    /**
     * Assumes file exists
     */
    public static String readFromFile(Path file) throws IOException {
        return new String(Files.readAllBytes(file), CHARSET);
    }

    /**
     * Writes given string to a file.
     * Will create the file if it does not exist yet.
     */
    public static void writeToFile(Path file, String content) throws IOException {
        Files.write(file, content.getBytes(CHARSET));
    }

    /**
     * Gets the path of the folder which the jar file is located in (or whatever is used to launch the app)
     */
    private static Path getAppEnclosingFolder() {
        try {
            // @@author jonas-chow-reused
            // taken from https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
            if (enclosingFolder == null) {
                enclosingFolder = Path.of(new File(FileUtil.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI()).getPath()).getParent();
            }
            // @@author jonas-chow
            return enclosingFolder;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    /**
     * Gets the absolute path, relative to where the jar file is located.
     */
    public static Path pathOf(String first, String... more) {
        Path path = Path.of(first, more);
        return getAppEnclosingFolder() == null ? path : enclosingFolder.resolve(path);
    }

    /**
     * Gets the path, relative to where the jar file is located.
     */
    public static String getRelativePathString(Path path) {
        return getAppEnclosingFolder() == null
                ? path.toString()
                : Path.of(".").resolve(getAppEnclosingFolder().relativize(path)).toString();
    }
}
