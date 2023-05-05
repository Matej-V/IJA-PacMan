package ija.project.pacman_project;

import ija.project.common.Observable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacManApp extends Application implements Observable.Observer {
    private Stage stage;
    private PacManView view;
    private PacManController controller;
    ExecutorService threadPool = Executors.newWorkStealingPool();

    @Override
    public void start(Stage stage) {
        /**
         * Width and height of the screen
         */
        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        // Generate controller
        view =  new PacManView(width, height);
        controller = new PacManController(view);
        view.setController(controller);
        // Generate view


        view.addObserver(this);

        // Set controller for view
        // Generate main screen
        view.generateMainScreen();
        // Set scene
        Scene scene = new Scene(view.currentScene, width, height);
        stage.setTitle("PacMan");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.setOnCloseRequest(e -> Platform.exit());
        this.stage = stage;
        stage.show();

        stage.setOnCloseRequest(controller::handleClose);
        scene.setOnKeyPressed(controller::handleKeyPress);
    }
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void update(Observable var1) {
        privateUpdate();
    }

    /**
     * Updates the scene in the stage
     */
    public void privateUpdate(){
        Scene newScene = new Scene(view.currentScene, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        System.out.println(view.currentScene);
        newScene.setOnKeyPressed(controller::handleKeyPress);
        stage.setScene(newScene);
    }
}
        