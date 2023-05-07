package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.game.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PacManController class that is used for managing game logic. It is used for generating game, starting timers and threads and handling key press events.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class PacManController {
    /** */
    private final PacManView view;
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
    /**
     * List of threads to they can be managed
     */
    private List<Thread> threads = new ArrayList<Thread>();
    /**
     * Current map file string
     */
    private String currentMap;
    /**
     * Current maze model
     */
    public Maze maze;
    /**
     * Log file for logging game
     */
    private File logFile;
    /**
     * Log writer for writing to log file
     */
    private LogWriter logWriter;
    /**
     * Current game state
     */
    public GameState gameState;

    /**
     * Game state enum
     */
    enum GameState {
        DEFAULT,
        REPLAY,
        REPLAY_REVERSE,
        PAUSE,
        UNPAUSE
    }

    /**
     * PacManController constructor
     * 
     * @param view PacManView object that is used for generating game views
     */
    public PacManController(PacManView view) {
        this.view = view;
    }

    /**
     * Generates new game, loads map, generates maze model, starts all timer,
     * threads and logging
     */
    public void newGame() {
        // stop moving if any
        changeGameState(GameState.DEFAULT);
        generateGame();
        view.generateGame();
        startLogging();
        // start moving
        startTimersThreads();

    }

    /**
     * Handle key press event
     * Changes pacman direction based on pressed key, pauses game or replays game
     */
    public void handleKeyPress(KeyEvent e) {
        if (gameState == GameState.DEFAULT) {
            switch (e.getCode()) {
                case UP, W -> maze.getPacMan().setDirection(Field.Direction.U);
                case LEFT, A -> maze.getPacMan().setDirection(Field.Direction.L);
                case DOWN, S -> maze.getPacMan().setDirection(Field.Direction.D);
                case RIGHT, D -> maze.getPacMan().setDirection(Field.Direction.R);
            }
        }
        if (Objects.requireNonNull(e.getCode()) == KeyCode.P) {
            if (gameState == GameState.PAUSE) {
                changeGameState(GameState.UNPAUSE);
            } else {
                changeGameState(GameState.PAUSE);
            }
        }
        switch (e.getCode()) {
            case R -> {
                System.out.println("Changing to replay");
                changeGameState(GameState.REPLAY);
            }
            case B -> changeGameState(GameState.REPLAY_REVERSE);
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
                    for (MazeObject ghost : maze.getGhosts()) {
                        ((GhostObject) ghost).setEatable(true);
                    }
                    // Wait for eatableDuration seconds
                    Thread.sleep(5000);
                    // Set ghost to be uneatable
                    for (MazeObject ghost : maze.getGhosts()) {
                        ((GhostObject) ghost).setEatable(false);
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
        timer.schedule(ghostsTask, 0, (long) (1000.0 / ghostsSpeed));
        timer.schedule(pacManTask, 0, (long) (1000.0 / pacManSpeed));
    }

    /**
     * Handle game event (win, lose, unexpected exception)
     */
    private void handleGameEvent(GameException e) {
        cancelTimersThreads();
        if (e.type == GameException.TypeOfException.CompletedGame) {
            System.out.println("Completed");
            view.generateSuccessScreen();
            endLogging();
        } else if (e.type == GameException.TypeOfException.LostGame) {
            System.out.println("Lost");
            view.generateEndScreen();
            endLogging();
        } else {
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
    private void cancelTimersThreads() {
        System.out.println("Threads canceled");
        for (Timer t : timers)
            t.cancel();
        timers.clear();
        for (Thread th : threads)
            th.interrupt();
        threads.clear();
    }

    /**
     * Start all timers and threads
     */
    private void startTimersThreads() {
        setMoveTimer();
        setEatableThread();
    }

    /**
     * Handle window close event
     */
    public void handleClose(WindowEvent windowEvent) {
        cancelTimersThreads();
        endLogging();
        Platform.exit();
    }

    public void generateGame() {
        chooseRandomMap();
    }

    public void chooseRandomMap() {
        Module module = PacManApp.class.getModule();
        URL folderURL  = PacManApp.class.getResource("/ija/project/maps");
        Path folderPath = null;
        try {
            folderPath = Paths.get(folderURL.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            File[] listOfFiles = Files.list(folderPath)
                    .map(Path::toFile)
                    .toArray(File[]::new);
            assert listOfFiles != null;
            List<File> files = Arrays.asList(listOfFiles);
            Random rand = new Random();
            // Get random file from list if same as current map, get another one
            File randomFile = files.get(rand.nextInt(files.size()));
            while (randomFile.getName().equals(currentMap)) {
                randomFile = files.get(rand.nextInt(files.size()));
            }
            // Set new map
            setMap("/ija/project/maps/" + randomFile.getName());

//        PacManApp.class.getResource("currentMap");
//        File folder = new File("./src/main/resources/ija/project/maps");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Method for setting map of the game
     * 
     * @param map chosen map
     */
    public void setMap(String map) {
        this.currentMap = map;
        this.loadFile(PacManApp.class.getResource(currentMap));
    }

    public void setLoadedMap(String map) {
        this.currentMap = map;
        File file = new File(map);
        URI uri = file.toURI();
        URL url;
        try {
            url = uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.loadFile(url);
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

    /**
     * Moves Ghosts in currently set directions
     * @throws GameException when pacman loses game
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
     * @throws GameException when pacman loses or wins game
     */
    public void movePacman() throws GameException {
        maze.getPacMan().move(maze.getPacMan().getDirection());
        checkCollision();
        checkWin();
        if (checkTarget())
            ((TargetField) maze.getTarget()).setOpen();
    }

    /**
     * Method for MazeObjects controll on a separate thread
     * @throws GameException, IOException
     */
    public void replaySave() throws IOException, GameException {
        // Create new thread that will replay the save
        Thread replayThread = new Thread(() -> {
            System.out.println("Staring replay thread");
            BufferedReader reader;
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
                    // exit while point
                    if (((line = reader.readLine()) == null))
                        break;
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
                            if ((nextLine = reader.readLine()) == null)
                                break;

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            Thread.sleep(duration.toMillis());
                        } catch (InterruptedException e) {
                            break;
                        }

                        Platform.runLater(() -> playOneMove(nextLine));
                    }
                    lastTimestamp = timestamp;
                }
            }
            try {
                reader.close();
                System.out.println("Reader closed");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        threads.add(replayThread);
        replayThread.start();

    }

    /**
     * Method for MazeObjects controll on a separate thread
     * @throws GameException, IOException
     */
    public void replaySaveReverse() throws IOException, GameException {
        Thread reverseReplayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> moves = null;
                try {
                    moves = Files.readAllLines(logFile.toPath(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
                int end = moves.size() - 1;
                String currentMove = null;
                LocalDateTime lastTimestamp = null;
                for (int i = end; !(moves.get(i).equals("--- LOG")); i--) {
                    String line = moves.get(i);
                    if (i == end) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                playOneMoveReverse(line);
                            }
                        });
                    }

                    if (line.startsWith("#")) {
                        LocalDateTime timestamp = LocalDateTime.parse(line.split("\\s+")[1], formatter);
                        if (lastTimestamp != null) {
                            Duration duration = Duration.between(timestamp, lastTimestamp);
                            try {
                                Thread.sleep(duration.toMillis());
                            } catch (InterruptedException e) {
                                break;
                            }
                            String move = currentMove;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    playOneMoveReverse(move);
                                }
                            });
                        }
                        lastTimestamp = timestamp;
                    } else {
                        currentMove = line;
                    }
                }
            }
        });

        threads.add(reverseReplayThread);
        reverseReplayThread.start();
    }

    /**
     * Method to clear pacman path according to his move history.
     * Used for reverse replay functionality.
     */
    private void clearPacmanPath() {
        List<String> moves;
        try {
            moves = Files.readAllLines(logFile.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (int i = moves.size() - 1; !(moves.get(i).equals("--- LOG")); i--) {
            if (moves.get(i).startsWith("P")) {
                List<String> splitedLine = List.of(moves.get(i).split(" "));
                List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                        .map(Integer::parseInt)
                        .toList();
                PathField field = (PathField) maze.getField(coords.get(0), coords.get(1));
                field.point = false;
                if (splitedLine.size() == 6 && splitedLine.get(5).equals("k")) {
                    maze.removeKey(maze.getField(coords.get(0), coords.get(1)).getKey());
                }
            }

        }
    }

    /**
     * Play one move from log in reverse replay mode
     *
     * @param nextLine line containing move history from log
     */
    private void playOneMoveReverse(String nextLine) {
        List<String> splitedLine = List.of(nextLine.split(" "));
        List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                .map(Integer::parseInt)
                .toList();
        if (checkTarget()) {
            ((TargetField) maze.getTarget()).setOpen();
        } else {
            ((TargetField) maze.getTarget()).setClosed();
        }
        if (nextLine.startsWith("P")) {
            int score = Integer.parseInt(splitedLine.get(2));
            int lives = Integer.parseInt(splitedLine.get(3));
            boolean p = false;
            boolean k = false;
            if (splitedLine.size() == 5) {
                p = splitedLine.get(4).contains("p");
            } else if (splitedLine.size() == 6) {
                k = splitedLine.get(5).contains("k");
            }

            try {
                PathField field = (PathField) maze.getField(coords.get(0), coords.get(1));
                maze.getPacMan().move(field);
                if (p)
                    field.point = true;
                if (k)
                    field.setKey();
                ((PacmanObject) maze.getPacMan()).setScore(score);
                ((PacmanObject) maze.getPacMan()).setLives(lives);
            } catch (GameException e) {
                throw new RuntimeException(e);
            }

        } else if (nextLine.startsWith("G")) {
            GhostObject ghost = (GhostObject) maze.getGhosts().get(Integer.parseInt(splitedLine.get(0).substring(1)));
            ghost.setEatable(Boolean.parseBoolean(splitedLine.get(2)));

            try {
                ghost.move(maze.getField(coords.get(0), coords.get(1)));
            } catch (GameException e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * Function to replay one move according to line from log file
     * 
     * @param line line from log file with information about move
     */
    private void playOneMove(String line) {
        if (line.startsWith("P")) {
            List<String> splitedLine = List.of(line.split(" "));
            List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                    .map(Integer::parseInt)
                    .toList();
            int score = Integer.parseInt(splitedLine.get(2));
            int lives = Integer.parseInt(splitedLine.get(3));

            try {
                maze.getPacMan().move(maze.getField(coords.get(0), coords.get(1)));
                ((PacmanObject) maze.getPacMan()).setScore(score);
                ((PacmanObject) maze.getPacMan()).setLives(lives);
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
            if (checkTarget())
                ((TargetField) maze.getTarget()).setOpen();
        } else if (line.startsWith("G")) {
            List<String> splitedLine = List.of(line.split(" "));
            List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                    .map(Integer::parseInt)
                    .toList();

            GhostObject ghost = (GhostObject) maze.getGhosts().get(Integer.parseInt(splitedLine.get(0).substring(1)));
            ghost.setEatable(Boolean.parseBoolean(splitedLine.get(2)));

            try {
                maze.getGhosts().get(Integer.parseInt(splitedLine.get(0).substring(1)))
                        .move(maze.getField(coords.get(0), coords.get(1)));
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
     * Checks if Pacman and Ghosts are on the same field. If so, checks if Ghost is
     * eatable. If yes, Ghost is moved to start. If not, Pacman and Ghosts are moved
     * to start.
     * @throws GameException pacman looses all of lives
     */
    private void checkCollision() throws GameException {
        for (MazeObject mazeObject : maze.getGhosts()) {
            GhostObject ghost = (GhostObject) mazeObject;
            if (ghost.getField().equals(maze.getPacMan().getField())) {
                if (ghost.isEatable()) {
                    ((PacmanObject) maze.getPacMan()).setScore(maze.getPacMan().getScore() + 100);
                    ghost.moveToStart();
                } else {
                    maze.moveObjectsToStart();
                }
            }
        }
    }

    /**
     * Checks if Pacman is on TargetField and all keys are colledted. If so, throws
     * CompletedGame exception.
     * @throws GameException when pacman completes game
     */
    private void checkWin() throws GameException {
        if (maze.getPacMan().getField() instanceof TargetField) {
            if (checkTarget()) {
                throw new GameException(GameException.TypeOfException.CompletedGame);
            }
        }
    }

    private boolean checkTarget() {
        return maze.canComplete();
    }

    /**
     * Chooses direction to move in for a ghost
     * 
     * @param ghost Ghost which direction will be set
     */
    public void chaseAlgorithm(MazeObject ghost) {
        Field.Direction dir = ghost.getDirection();
        // create list of available directions
        List<Field.Direction> availableDirections = new CopyOnWriteArrayList<>();
        for (Field.Direction d : Field.Direction.values()) {
            if (ghost.canMove(d)) {
                availableDirections.add(d);
            }
        }
        // if you can't continue in the same direction, choose random direction, try not
        // to go back, if the only option is to go back, go back
        if (!availableDirections.contains(dir)) {
            availableDirections.remove(dir.opposite(dir));
            if (availableDirections.size() == 0) {
                dir = dir.opposite(dir);
            } else {
                Random rand = new Random();
                dir = availableDirections.get(rand.nextInt(availableDirections.size()));
            }
        } else {
            // if you can continue in the same direction, choose random direction with 30%
            // chance, but try not to go back
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

    public void startLogging() {
        logFile = new File("log.save");
        try {
            logWriter = new LogWriter(logFile, maze);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes logWriter
     */
    public void endLogging() {
        if (logWriter != null)
            logWriter.close();
    }

    /**
     * Method to set last seen state of MazeObjects like ghosts and pacman
     */
    public void setLastState() {
        PacmanObject pm = logWriter.getLastPacmanState();
        try {
            this.maze.getPacMan().move(pm.getField());
        } catch (GameException e) {
            throw new RuntimeException(e);
        }

        ListIterator<MazeObject> it = this.maze.getGhosts().listIterator();
        while (it.hasNext()) {
            GhostObject ghost = (GhostObject) it.next();
            for (MazeObject ghostState : logWriter.getLastGhostsState()) {
                if (ghost.getId() == ((GhostObject) ghostState).getId()) {
                    try {
                        ghost.move(ghostState.getField());
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /**
     * Change current game state and starts operations according to new game state
     */
    public void changeGameState(GameState newGamestate) {
        switch (newGamestate) {
            case REPLAY -> {
                gameState = newGamestate;
                cancelTimersThreads();
                endLogging();
                setLoadedMap("log.save");
                view.generateGame();
                try {
                    replaySave();
                } catch (IOException | GameException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case REPLAY_REVERSE -> {
                gameState = newGamestate;
                cancelTimersThreads();
                endLogging();
                setLoadedMap("log.save");
                clearPacmanPath();
                setLastState();
                MazeObject mz = this.maze.getPacMan();
                ((PacmanObject) mz).setReplayMode();
                view.generateGame();
                try {
                    replaySaveReverse();
                } catch (IOException | GameException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case DEFAULT -> {
                cancelTimersThreads();
                gameState = newGamestate;
            }
            case PAUSE -> {
                if (gameState == GameState.DEFAULT || gameState == GameState.PAUSE) {
                    gameState = newGamestate;
                    cancelTimersThreads();
                    StackPane pausePane = new StackPane();
                    Text text = new Text("Game paused");
                    text.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-fill: white;");
                    pausePane.getChildren().add(text);
                    view.gameBox.getChildren().add(pausePane);
                }
            }
            case UNPAUSE -> {
                changeGameState(GameState.DEFAULT);
                startTimersThreads();
                view.gameBox.getChildren().remove(view.currentScene.getChildren().size() - 1);
            }
        }

    }
}