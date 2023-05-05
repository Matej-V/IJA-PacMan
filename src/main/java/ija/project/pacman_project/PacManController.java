package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.common.MazeObject;
import ija.project.game.GameException;
import ija.project.game.GhostObject;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.*;

public class PacManController{
    private final PacManModel model;
    private final PacManView view;
    double ghostsSpeed = 2;
    double pacManSpeed = 3.5;
    private final List<Timer> timers = new ArrayList<Timer>();
    private Thread eatableThread;

    public PacManController(PacManModel model, PacManView view){
        this.model = model;
        this.view = view;
    }

    /**
     * Handle key press event
     * Changes pacman direction based on pressed key
     */
    public void handleKeyPress(KeyEvent e) {
        if (model.gameState == PacManModel.GameState.DEFAULT || model.gameState == PacManModel.GameState.PAUSE) {
            switch (e.getCode()) {
                case UP, W -> model.maze.getPacMan().setDirection(Field.Direction.U);
                case LEFT, A -> model.maze.getPacMan().setDirection(Field.Direction.L);
                case DOWN, S -> model.maze.getPacMan().setDirection(Field.Direction.D);
                case RIGHT, D -> model.maze.getPacMan().setDirection(Field.Direction.R);
                // pause game
                case P -> {
                    if (timers.size() == 0) {
                        resumeTimersThreads();
                        view.gameBox.getChildren().remove(view.currentScene.getChildren().size() - 1);
                    } else {
                        model.changeGameState(PacManModel.GameState.PAUSE);
                        cancelTimersThreads();
                        StackPane pausePane = new StackPane();
                        Text text = new Text("Game paused");
                        text.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-fill: white;");
                        pausePane.getChildren().add(text);
                        view.gameBox.getChildren().add(pausePane);
                    }
                }
                case R -> {
                    cancelTimersThreads();
                    model.changeGameState(PacManModel.GameState.REPLAY);
                    view.generateGame();
                    try {
                        model.replaySave();
                    } catch (IOException | GameException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        }
    }

    /**
     * Starts separate thread for enabling ghosts to be eaten by pacman
     */
    public void setEatableThread() {
        eatableThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    // Set ghost to be eatable
                    for(MazeObject ghost : model.maze.ghosts()){
                        ((GhostObject)ghost).setEatable(true);
                    }
                    // Wait for eatableDuration seconds
                    Thread.sleep(5000);
                    // Set ghost to be uneatable
                    for(MazeObject ghost : model.maze.ghosts()){
                        ((GhostObject)ghost).setEatable(false);
                    }
                    // Wait for interval seconds
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        eatableThread.start();
    }

    /**
     * Set timer for moving ghosts and pacman
     * Ghosts movement is handled differently than pacman's in separate task
     */
    private void setMoveTimer() {
        Timer timer = new Timer();
        timers.add(timer);
        TimerTask ghostsTask = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    try {
                        model.moveGhosts();
                    } catch (GameException e) {
                        handleGameEvent(e);
                    }
                });
            }
        };
        TimerTask pacManTask = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    try {
                        model.movePacman();
                    } catch (GameException e) {
                        handleGameEvent(e);
                    }
                });
            }
        };

        /* Speed in field per second */
        timer.schedule(ghostsTask, 0, (long)(1000.0 / ghostsSpeed));
        timer.schedule(pacManTask, 0, (long)(1000.0 / pacManSpeed));
    }

    /**
     * Handle game event
     */
    private void handleGameEvent(GameException e){
        cancelTimersThreads();
        if (e.type == GameException.TypeOfException.CompletedGame){
            System.out.println("Completed");
            view.generateSuccessScreen();
            model.endLogging();
        }else if (e.type == GameException.TypeOfException.LostGame){
            System.out.println("Lost");
            view.generateEndScreen();
            model.endLogging();
        }else {
            cancelTimersThreads();
            model.endLogging();
            System.out.println("Unexpected Exception");
            e.getStackTrace();
            Platform.exit();
        }
    }

    public void newGame(){
        cancelTimersThreads();
        model.newGame();
        view.generateGame();
        setMoveTimer();
        setEatableThread();
    }

    /*
     * Terminates all timers
     */
    private void cancelTimersThreads(){
        for (Timer timer : timers) {
            timer.cancel();
        }
        timers.clear();
        if(eatableThread != null) eatableThread.interrupt();

    }

    private void resumeTimersThreads(){
        setMoveTimer();
        setEatableThread();
    }

    /**
     * Handle window close event
     */
    public void handleClose(WindowEvent windowEvent){
        cancelTimersThreads();
        model.endLogging();
        Platform.exit();
    }
}