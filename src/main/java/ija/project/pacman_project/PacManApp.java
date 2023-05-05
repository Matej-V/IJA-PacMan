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
        double width = Screen.getPrimary().getVisualBounds().getWidth();
        double height = Screen.getPrimary().getVisualBounds().getHeight();
        // Generate model
        PacManModel model = new PacManModel(stage);
        // Generate view
        this.view =  new PacManView(model, width, height);
        view.addObserver(this);
        // Generate controller
        controller = new PacManController(model, view);
        // Set controller for view
        view.setController(controller);
        // Generate main screen
        view.generateMainScreen();
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

    public void privateUpdate(){
        Scene newScene = new Scene(view.currentScene, Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        System.out.println(view.currentScene);
        newScene.setOnKeyPressed(controller::handleKeyPress);
        stage.setScene(newScene);
        stage.show();
    }
}
        