package it.polimi.ingsw.am37.client.gui;

import it.polimi.ingsw.am37.client.gui.controller.GenericController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {

    public static final Object waitObject = new Object();
    private static Scene activeScene;
    private static GenericController activeController;


    private SceneController() {
    }

    public static void createScene(String path) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(GuiApp.class.getResource(path));
        Parent rootLayout = null;
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        activeScene = new Scene(rootLayout);
        activeController = loader.getController();
    }

    public static void switchScreen(String path) {
        Stage stage = (Stage) activeScene.getWindow();
        createScene(path);
        stage.setScene(activeScene);
    }

    public static Scene getActiveScene() {
        return activeScene;
    }

    public static GenericController getActiveController() {
        return activeController;
    }
}
