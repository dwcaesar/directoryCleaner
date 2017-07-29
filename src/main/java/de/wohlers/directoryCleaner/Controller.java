package de.wohlers.directoryCleaner;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by Daniel on 08.05.2016.
 */
public class Controller {
    public TextField rootField;
    public Label statusLabel;
    public Button executeButton;
    public Button selectRootButton;
    private DirectoryChooser directoryChooser;
    private Stage primaryStage;
    private File selectedDirectory;

    public void onSelect(@SuppressWarnings("UnusedParameters") ActionEvent actionEvent) {
        selectedDirectory = directoryChooser.showDialog(primaryStage.getOwner());
        if (selectedDirectory == null) {
            rootField.setText("");
            statusLabel.setText("Bereit");
        } else {
            try {
                rootField.setText(selectedDirectory.getCanonicalPath());
                statusLabel.setText("Bereit");
            } catch (IOException e) {
                rootField.setText(e.toString());
                statusLabel.setText("Fehler");
            }
        }
    }

    public void onExecute(@SuppressWarnings("UnusedParameters") ActionEvent actionEvent) {
        if (!CleanerTask.fileUtils.hasTrash()) {
            noTrash();
        } else if (selectedDirectory != null) {
            statusLabel.setText("Arbeite");
            selectRootButton.setDisable(true);
            executeButton.setDisable(true);
            startService();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Es soll ein Verzeichnis ausgewählt sein.");
            alert.show();
            selectRootButton.setDisable(false);
            executeButton.setDisable(false);
        }

    }

    void init(Stage primaryStage) {

        if (!CleanerTask.fileUtils.hasTrash()) {
            noTrash();
        }

        this.primaryStage = primaryStage;
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Stammverzeichnis wählen");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    private void noTrash() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Kein Papierkorb.");
        alert.setHeaderText("Es können hiermit keine Daten gelöscht werden.");
        alert.setContentText("Es muss ein unterstützter Papierkorb zur Verfügung stehen.");
    }

    private void startService() {
        CleanerService cleaner = new CleanerService();

        cleaner.setOnSucceeded(event -> {
            selectRootButton.setDisable(false);
            executeButton.setDisable(false);
            statusLabel.setText("Bereit");
        });

        cleaner.setOnFailed(event -> {
            selectRootButton.setDisable(false);
            executeButton.setDisable(false);
            statusLabel.setText("Fehler");
        });

        cleaner.setRoot(selectedDirectory);
        cleaner.start();
    }

}
