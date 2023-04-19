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
    final private double FPS = 5.0;
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

        long FPSms = (long)(1000.0 / FPS);
        this.timer.schedule(timerTask, 0, FPSms);
    }

    public void movePacman(Field.Direction dir){
        model.movePacman(dir);
    }

    private void updateGame(){
        System.out.println("Updating");
        view.generateGame();
    }

}