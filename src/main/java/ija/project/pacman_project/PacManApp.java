package ija.project.pacman_project;

import javafx.application.Application;
import javafx.scene.Scene;
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
        // Generate controller
        PacManController controller = new PacManController(model);
        // Initialize Controller
        controller.initialize(null, null);
        // Generate view
        PacManView view = new PacManView(model, controller);

        Scene scene = new Scene(view.generateGame(), 500, 550);

        //scene.setUserData(controller);

        stage.setTitle("PacMan App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}
        