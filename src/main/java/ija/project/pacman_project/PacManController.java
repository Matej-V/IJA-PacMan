package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.game.GhostObject;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class PacManController implements Initializable {
    private Timer timer;
    private PacManModel model;
    double ghostsSpeed = 2;
    double pacManSpeed = 3.5;
    private List<Timer> timers = new ArrayList<Timer>();
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public PacManController(PacManModel model){
        this.model = model;
        this.setMoveTimer();
    }

    /**
     * Handle key press event
     * Changes pacman direction based on pressed key
     * @param e
     */
    public void handleKeyPress(KeyEvent e) {
        switch (e.getCode()) {
            case UP, W -> model.pacman.setDirection(Field.Direction.U);
            case LEFT, A -> model.pacman.setDirection(Field.Direction.L);
            case DOWN, S -> model.pacman.setDirection(Field.Direction.D);
            case RIGHT, D -> model.pacman.setDirection(Field.Direction.R);
        }
    }

    /*
     * Set timer for moving ghosts and pacman
     * Ghosts movement is handled differently than pacman's in separate task
     */
    public void setMoveTimer() {
        Timer timer = new Timer();
        timers.add(timer);
        TimerTask ghostsTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        model.moveGhosts();
                    }
                });
            }
        };
        TimerTask pacManTask = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        model.movePacman();
                    }
                });
            }
        };
        /* Speed in field per second */
        timer.schedule(ghostsTask, 0, (long)(1000.0 / ghostsSpeed));
        timer.schedule(pacManTask, 0, (long)(1000.0 / pacManSpeed));
    }

    /**
     * Handle window close event
     * @param windowEvent
     */
    public void handleClose(WindowEvent windowEvent){
        for (Timer timer : timers) {
            timer.cancel();
        }
        Platform.exit();
    }
}