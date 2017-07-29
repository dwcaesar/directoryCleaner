package de.wohlers.directoryCleaner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Daniel on 08.05.2016.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        controller.init(primaryStage);
        primaryStage.setTitle("Directory Cleaner");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
