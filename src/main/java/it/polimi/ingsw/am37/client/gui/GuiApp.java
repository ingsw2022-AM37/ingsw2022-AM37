package it.polimi.ingsw.am37.client.gui;

import it.polimi.ingsw.am37.client.PlayerAbortException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

/**
 * Class used to start JavaFX
 */
public class GuiApp extends Application {
    public static final CountDownLatch latch = new CountDownLatch(1);
    private static GuiApp instance = null;

    public GuiApp() {
        initialize(this);
    }

    public static GuiApp waitForStartUp() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public static void initialize(GuiApp startUpTest0) {
        instance = startUpTest0;
        latch.countDown();
    }

    /**
     * The main entry point for all JavaFX applications. The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneController.createScene("/assets/scenes/Connection.fxml");
        primaryStage.setScene(SceneController.getActiveScene());
        primaryStage.setTitle("Welcome to Eryantis game!");
        primaryStage.getIcons()
                .add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/cranio_logo.jpg"))));
        primaryStage.show();
    }

    /**
     * This method is called when the application should stop, and provides a convenient place to prepare for
     * application exit and destroy resources.
     *
     * <p>
     * The implementation of this method provided by the Application class does nothing.
     * </p>
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @throws Exception if something goes wrong
     */
    @Override
    public void stop() throws Exception {
        Platform.exit();
        super.stop();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Runtime.getRuntime().halt(0);
            }
        }, 3000);
        System.exit(0);
        timer.cancel();
        throw new PlayerAbortException();
    }
}
