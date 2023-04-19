package ija.project.pacman_project;

import ija.project.common.Field;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PacManController implements Initializable {
    private Timer timer;
    private PacManModel model;
    private PacManView view;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public PacManController(PacManModel model, PacManView view){
        this.model = model;
        this.view = view;
        updateGame();
        this.startTimer();
    }

    private void startTimer(){
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        updateGame();
                    }
                });
            }
        };

        double FPS = 24.0;
        long FPSms = (long)(1000.0 / FPS);
        this.timer.schedule(timerTask, 0, FPSms);
    }

    private void updateGame(){
        System.out.println("Updating...");
        this.view.updateGame(model);
    }


    public void handleKeyPress(KeyCode code) {
        switch (code) {
            case W:
                System.out.println("Key W");
                model.movePacman(Field.Direction.U);
                break;
            case A:
                System.out.println("Key A");
                model.movePacman(Field.Direction.L);
                break;
            case S:
                System.out.println("Key S");
                model.movePacman(Field.Direction.D);
                break;
            case D:
                System.out.println("Key D");
                model.movePacman(Field.Direction.R);
                break;
            default:
                break;
        }

    }
}