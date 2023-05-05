package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.game.*;
import javafx.application.Platform;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacManController{
    /** */
    private PacManView view;
    /**
     * Speed of ghosts
     */
    double ghostsSpeed = 2;
    /**
     * Speed of pacman
     */
    double pacManSpeed = 3.5;
    /**
     * List of timers for ghosts and pacman movement
     */
    private final List<Timer> timers = new ArrayList<Timer>();
    private List<Thread> threads = new ArrayList<Thread>();
    private String currentMap;
    public Maze maze;
    private File logFile;
    private LogWriter logWriter;
    public GameState gameState;
    private Stage stage;
    enum GameState{
        DEFAULT,
        REPLAY,
        REPLAY_REVERSE,
        PAUSE
    }

    public PacManController(PacManView view){
        this.view = view;
    }

    public void addView(PacManView view){
        this.view = view;
    }


    /**
     * Generates new game and sets timer and thread for eatable state of ghosts
     */
    public void newGame(){
        // stop moving if any
        cancelTimersThreads();
        generateGame();
        view.generateGame();
        startLogging();
        // start moving
        changeGameState(GameState.DEFAULT);
    }

    /**
     * Handle key press event
     * Changes pacman direction based on pressed key, pauses game or replays game
     */
    public void handleKeyPress(KeyEvent e) {
        if (gameState == GameState.DEFAULT || gameState == GameState.PAUSE) {
            switch (e.getCode()) {
                case UP, W -> maze.getPacMan().setDirection(Field.Direction.U);
                case LEFT, A -> maze.getPacMan().setDirection(Field.Direction.L);
                case DOWN, S -> maze.getPacMan().setDirection(Field.Direction.D);
                case RIGHT, D -> maze.getPacMan().setDirection(Field.Direction.R);
                // pause game
                case P -> {
                    if (timers.size() == 0) {
                        startTimersThreads();
                        view.gameBox.getChildren().remove(view.currentScene.getChildren().size() - 1);
                    } else {
                        changeGameState(GameState.PAUSE);
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
                    changeGameState(GameState.REPLAY);
//                    StackPane replayEndPane = new StackPane();
//                    Text text = new Text("Restart Game");
//                    text.onMouseClickedProperty().setValue(event -> {
//                        newGame();
//                    });
//                    text.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-fill: white;");
//                    replayEndPane.getChildren().add(text);
//                    view.gameBox.getChildren().add(replayEndPane);
                }
            }
        }
    }

    /**
     * Starts separate thread for enabling ghosts to be eaten by pacman
     */
    public void setEatableThread() {
        Thread eatableThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                    // Set ghost to be eatable
                    for(MazeObject ghost : maze.getGhosts()){
                        ((GhostObject)ghost).setEatable(true);
                    }
                    // Wait for eatableDuration seconds
                    Thread.sleep(5000);
                    // Set ghost to be uneatable
                    for(MazeObject ghost : maze.getGhosts()){
                        ((GhostObject)ghost).setEatable(false);
                    }
                    // Wait for interval seconds
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        threads.add(eatableThread);
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
                        moveGhosts();
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
                        movePacman();
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
     * Handle game event (win, lose, unexpected exception)
     */
    private void handleGameEvent(GameException e){
        cancelTimersThreads();
        if (e.type == GameException.TypeOfException.CompletedGame){
            System.out.println("Completed");
            view.generateSuccessScreen();
            endLogging();
        }else if (e.type == GameException.TypeOfException.LostGame){
            System.out.println("Lost");
            view.generateEndScreen();
            endLogging();
        }else{
            cancelTimersThreads();
            endLogging();
            System.out.println("Unexpected Exception");
            e.getStackTrace();
            Platform.exit();
        }
    }

    /**
     * Terminates all timers and threads
     */
    private void cancelTimersThreads(){
        for (Timer t : timers)t.cancel();
        timers.clear();
        for (Thread th : threads) th.interrupt();
        threads.clear();
    }

    /**
     * Start all timers and threads
     */
    private void startTimersThreads(){
        setMoveTimer();
        setEatableThread();
    }

    /**
     * Handle window close event
     */
    public void handleClose(WindowEvent windowEvent){
        cancelTimersThreads();
        endLogging();
        Platform.exit();
    }


    public void generateGame() {
        chooseRandomMap();
    }

    public void chooseRandomMap() {
        File folder = new File("./src/main/resources/ija/project/pacman_project/maps");
        File[] listOfFiles;
        listOfFiles = folder.listFiles();
        assert listOfFiles != null;
        List<File> files = Arrays.asList(listOfFiles);
        Random rand = new Random();
        // Get random file from list if same as current map, get another one
        File randomFile = files.get(rand.nextInt(files.size()));
        while (randomFile.getName().equals(currentMap)) {
            randomFile = files.get(rand.nextInt(files.size()));
        }
        // Set new map
        setMap("maps/" + randomFile.getName());
    }

    /**
     * Method for setting map of the game
     * @param map chosen map
     */
    public void setMap(String map) {
        this.currentMap = map;
        this.loadFile(PacManApp.class.getResource(currentMap));
    }

    /**
     * Method for loading file from game resources
     *
     * @param file path to file (either just a map or a save)
     */
    public void loadFile(URL file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.openStream()))) {
            String line;
            MazeConfigure cfg = new MazeConfigure();
            line = br.readLine();
            String[] words = line.split("\\s+");

            int lives = 3;
            int score = 0;
            cfg.startReading(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            while (!Objects.equals(line = br.readLine(), null)) {
                if (line.trim().isEmpty()) {
                    break;
                }
                cfg.processLine(line);
            }
            cfg.stopReading();

            this.maze = cfg.createMaze();
            ((PacmanObject) this.maze.getPacMan()).setScore(score);
            ((PacmanObject) this.maze.getPacMan()).setLives(lives);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    public void loadGameFromSave() {
        setMap("saves/log.save");
    }

    /**
     * Moves Ghosts in currently set directions
     * @throws GameException
     */
    public void moveGhosts() throws GameException {
        for (MazeObject mazeObject : maze.getGhosts()) {
            GhostObject ghost = (GhostObject) mazeObject;
            chaseAlgorithm(ghost);
            ghost.move(ghost.getDirection());
        }
        checkCollision();
    }

    /**
     * Moves Pacman in currently set direction
     * @throws GameException
     */
    public void movePacman() throws GameException {
        maze.getPacMan().move(maze.getPacMan().getDirection());
        checkCollision();
        checkWin();
    }

    public void replaySave() throws IOException, GameException {

        Thread replayThread = new Thread(new Runnable() {
            @Override
            public void run(){
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(logFile));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                String line;
                boolean logStarted = false;
                LocalDateTime lastTimestamp = null;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
                while (true) {
                    try {
                        if ((line = reader.readLine()) == null) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (line.equals("--- LOG")) {
                        logStarted = true;
                        continue;
                    }
                    if (!logStarted) {
                        continue;
                    }

                    if (line.startsWith("#")) {
                        LocalDateTime timestamp = LocalDateTime.parse(line.substring(2), formatter);
                        if (lastTimestamp != null) {
                            Duration duration = Duration.between(lastTimestamp, timestamp);
                            String nextLine;
                            try {
                                if((nextLine = reader.readLine()) == null) break;

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                Thread.sleep(duration.toMillis());
                            } catch (InterruptedException e) {
                                break;
                            }

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    playOneMove(nextLine);
                                }
                            });
                        }
                        lastTimestamp = timestamp;
                    }
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        threads.add(replayThread);
        replayThread.start();

    }

    private void playOneMove(String nextLine){
        if (nextLine.startsWith("P")) {
            List<String> splitedLine = List.of(nextLine.split(" "));
            List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                    .map(Integer::parseInt)
                    .toList();
            int score= Integer.parseInt(splitedLine.get(2));
            int lives = Integer.parseInt(splitedLine.get(3));

            try {
                maze.getPacMan().move(maze.getField(coords.get(0), coords.get(1)));
                ((PacmanObject) maze.getPacMan()).setScore(score);
                ((PacmanObject) maze.getPacMan()).setLives(lives);
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
        } else if (nextLine.startsWith("G")) {
            List<String> splitedLine = List.of(nextLine.split(" "));
            List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                    .map(Integer::parseInt)
                    .toList();

            GhostObject ghost = (GhostObject) maze.getGhosts().get(Integer.parseInt(splitedLine.get(0).substring(1)));
            ghost.setEatable(Boolean.parseBoolean(splitedLine.get(2)));

            try {
                maze.getGhosts().get(Integer.parseInt(splitedLine.get(0).substring(1))).move(maze.getField(coords.get(0), coords.get(1)));
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Unexpected line format in saved file");
            try {
                throw new GameException(GameException.TypeOfException.Other);
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Checks if Pacman and Ghosts are on the same field. If so, checks if Ghost is eatable. If yes, Ghost is moved to start. If not, Pacman and Ghosts are moved to start.
     * @throws GameException
     */
    private void checkCollision() throws GameException{
        for (MazeObject mazeObject : maze.getGhosts()) {
            GhostObject ghost = (GhostObject) mazeObject;
            if (ghost.getField().equals(maze.getPacMan().getField())) {
                if(ghost.isEatable()){
                    ((PacmanObject) maze.getPacMan()).setScore(maze.getPacMan().getScore()+100);
                    ghost.moveToStart();
                }else{
                    maze.moveObjectsToStart();
                }
            }
        }
    }

    /**
     * Checks if Pacman is on TargetField and all keys are colledted. If so, throws CompletedGame exception.
     * @throws GameException
     */
    private void checkWin() throws GameException{
        if(maze.getPacMan().getField() instanceof TargetField){
            if(maze.canComplete()){
                throw new GameException(GameException.TypeOfException.CompletedGame);
            }
        }
    }


    /**
     * Chooses direction to move in for a ghost
     * @param ghost Ghost which direction will be set
     */
    public void chaseAlgorithm(MazeObject ghost) {
        Field.Direction dir = ghost.getDirection();
        //create list of available directions
        List<Field.Direction> availableDirections = new CopyOnWriteArrayList<>();
        for (Field.Direction d : Field.Direction.values()) {
            if (ghost.canMove(d)) {
                availableDirections.add(d);
            }
        }
        // if you can't continue in the same direction, choose random direction, try not to go back, if the only option is to go back, go back
        if (!availableDirections.contains(dir)) {
            availableDirections.remove(dir.opposite(dir));
            if (availableDirections.size() == 0) {
                dir = dir.opposite(dir);
            } else {
                Random rand = new Random();
                dir = availableDirections.get(rand.nextInt(availableDirections.size()));
            }
        }
        else {
            // if you can continue in the same direction, choose random direction with 30% chance, but try not to go back
            availableDirections.remove(dir.opposite(dir));
            Random rand = new Random();
            if (rand.nextInt(10) < 3) {
                dir = availableDirections.get(rand.nextInt(availableDirections.size()));
            }
        }
        ghost.setDirection(dir);
    }


    /**
     * Generates new game for player. Total score is set to 0.
     */

    public void startLogging(){
        logFile = new File("./src/main/resources/ija/project/pacman_project/saves/log.save");
        try {
            logWriter = new LogWriter(logFile, maze);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void endLogging(){
        if (logWriter != null)logWriter.close();
    }

    public void changeGameState(GameState newGamestate){
        gameState = newGamestate;
        switch (gameState){
            case REPLAY:
                cancelTimersThreads();
                endLogging();
                loadGameFromSave();
                view.generateGame();
                try {
                    replaySave();
                } catch (IOException | GameException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case DEFAULT:
                cancelTimersThreads();
                startTimersThreads();
                break;
        }

    }
}