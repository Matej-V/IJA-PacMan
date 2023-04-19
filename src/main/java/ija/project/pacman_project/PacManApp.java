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


        Scene scene = new Scene(view.generateGame(), 500, 550);

        //scene.setUserData(controller);

        stage.setTitle("PacMan App");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.W) {
                System.out.println("Key W");
                controller.movePacman(Field.Direction.U);
            } else if (keyEvent.getCode() == KeyCode.A) {
                System.out.println("Key A");
                controller.movePacman(Field.Direction.L);
            } else if (keyEvent.getCode() == KeyCode.S) {
                System.out.println("Key S");
                controller.movePacman(Field.Direction.D);
            } else if (keyEvent.getCode() == KeyCode.D) {
                System.out.println("Key D");
                controller.movePacman(Field.Direction.R);
            }
        });
    }
    public static void main(String[] args) {
        launch();
    }
}
        