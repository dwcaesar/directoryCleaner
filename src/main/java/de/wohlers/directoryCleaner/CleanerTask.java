package de.wohlers.directoryCleaner;

import com.sun.jna.platform.FileUtils;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class CleanerTask extends Task<Void> {
    private static final Pattern TO_EXCLUDE = Pattern.compile("^.*(thumbs.db|desktop.ini|.jpg)$");
    static FileUtils fileUtils = FileUtils.getInstance();
    private File root;

    CleanerTask(File root) {
        this.root = root;
    }

    private static void tryDeleteFolder(Path folder, Path root) {
        if (Files.isDirectory(folder)) {
            try {
                Files.list(folder).forEach((child) -> tryDeleteFolder(child, root));
                if (canBeDeleted(folder) && !folder.equals(root)) {
                    removeFolder(folder);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void removeFolder(Path root) throws IOException {
        fileUtils.moveToTrash(new File[]{root.toFile()});
    }

    private static boolean canBeDeleted(Path file) throws IOException {
        return Files.isDirectory(file) && Files.list(file).filter(o -> !TO_EXCLUDE.matcher(o.toString()).matches() || Files.isDirectory(o)).collect(Collectors.toList()).isEmpty();
    }

    @Override
    protected Void call() throws Exception {
        tryDeleteFolder(root.toPath(), root.toPath());
        return null;
    }
}
