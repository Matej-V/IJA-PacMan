package ija.project.pacman_project;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PacManController implements Initializable {
//    private Timer timer;
    private PacManModel model;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public PacManController(PacManModel model){
        this.model = model;
//        this.startTimer();
    }

//    private void startTimer() {
//        this.timer = new java.util.Timer();
//        TimerTask timerTask = new TimerTask() {
//            public void run() {
//                Platform.runLater(new Runnable() {
//                    public void run() {
//                        updateGame();
//                        System.out.println("Updating...");
//                    }
//                });
//            }
//        };
//
//        this.timer.schedule(timerTask, 0, 100);
//    }

    private void updateGame(){
        //todo
    }

}