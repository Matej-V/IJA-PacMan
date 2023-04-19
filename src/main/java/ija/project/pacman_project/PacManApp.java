package ija.project.pacman_project;

import ija.project.common.Field;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// import implementation

import java.io.IOException;

public class PacManApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Generate model
        PacManModel model = new PacManModel();
        // Generate view
        PacManView view = new PacManView(model);
        // Generate controller
        PacManController controller = new PacManController(model, view);
        // Initialize Controller
        controller.initialize(null, null);


        VBox gameBox = view.generateGame();
        Scene scene = new Scene(gameBox, 500, 550);

        //scene.setUserData(controller);

        stage.setTitle("PacMan");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed(controller::handleKeyPress);
    }
    public static void main(String[] args) {
        launch();
    }
}
        