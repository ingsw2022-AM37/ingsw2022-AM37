package it.polimi.ingsw.am37.client.gui;

import it.polimi.ingsw.am37.client.gui.controller.GenericController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class used for creating and switching scenes
 */
public class SceneController {

    public static final Object waitObject = new Object();
    private static SceneData sceneData;


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
        sceneData = new SceneData(new Scene(rootLayout), loader.getController());
    }

    public static void switchScreen(String path) {
        Stage stage = (Stage) sceneData.activeScene().getWindow();
        createScene(path);
        stage.setScene(sceneData.activeScene());
    }

    public static Scene getActiveScene() {
        return sceneData.activeScene();
    }

    public static GenericController getActiveController() {
        try {
            return sceneData.activeController();
        }catch (NullPointerException e){
            ;
        }
        return null;
    }

    /**
     * This record encapsulate the data of each scene in an atomic constructor, so the data are created and updated
     * atomically
     *
     * @param activeScene      the current active scene
     * @param activeController the controller of the active scene
     */
    private record SceneData(Scene activeScene, GenericController activeController) {
    }
}
