package ija.project.pacman_project;

import ija.project.common.Observable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PacManApp extends Application implements Observable.Observer {
    /**
     * Stage of the application
     */
    private Stage stage;
    /**
     * View generator for the application
     */
    private PacManView view;
    /**
     * Controller for the application
     */
    private PacManController controller;

    @Override
    public void start(Stage stage) {
        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        // Generate view
        view =  new PacManView(width, height);
        // Generate controller
        controller = new PacManController(view);
        // Set controller for view so maze can be accessed
        view.setController(controller);

        // Add observer to view so scene can be updated
        view.addObserver(this);

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
        newScene.setOnKeyPressed(controller::handleKeyPress);
        stage.setScene(newScene);
    }
}
        