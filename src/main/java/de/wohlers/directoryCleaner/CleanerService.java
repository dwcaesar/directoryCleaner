package de.wohlers.directoryCleaner;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.io.File;

class CleanerService extends Service<Void> {
    private File root;

    @Override
    protected Task<Void> createTask() {
        return new CleanerTask(root);
    }

    void setRoot(File root) {
        this.root = root;
    }

}
