package com.iam2kabhishek.painter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static volatile boolean javaFxLaunched = false;

    public static void multiLaunch(Class<? extends Application> applicationClass) {
        if (!javaFxLaunched) { // First time
            Platform.setImplicitExit(false);
            new Thread(() -> Application.launch(applicationClass)).start();
            javaFxLaunched = true;
        } else { // Next times
            Platform.runLater(() -> {
                try {
                    Application application = applicationClass.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        multiLaunch(App.class);
    }

}
