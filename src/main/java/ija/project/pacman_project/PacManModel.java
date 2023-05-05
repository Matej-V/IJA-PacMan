package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.game.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import java.time.Duration;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PacManModel {
    private String currentMap;
    public Maze maze;
    public List<String> ghostsPaths;
    String fileID;
    File logFile;
    LogWriter logWriter;
    public GameState gameState;
    private Stage stage;

    enum GameState{
        DEFAULT,
        REPLAY,
        REPLAY_REVERSE,
        PAUSE
    }

    public PacManModel(Stage stage) {
        this.stage = stage;
        this.ghostsPaths = new ArrayList<>();
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

            if (this.ghostsPaths != null) {
                ListIterator<String> it = this.ghostsPaths.listIterator();
                while (it.hasNext()) {
                    String[] str = it.next().split("\\s+");
                    this.maze.registerGhostPath(str[0].charAt(0), str[1]);
                }
            }
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
        for (MazeObject mazeObject : maze.ghosts()) {
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

        new Thread(new Runnable() {
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
                        if (!((line = reader.readLine()) != null)) break;
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
                            Timer timer = new Timer();
                            String nextLine;
                            try {
                                nextLine = reader.readLine();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            try {
                                System.out.println("Sleeping" + duration.toMillis());
                                Thread.sleep(duration.toMillis());
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            Platform.runLater(new Runnable() {
                              @Override
                              public void run() {
                                  playOneMove(nextLine, timer);
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
        }).start();

    }

    private void playOneMove(String nextLine, Timer timer){
        if (nextLine.startsWith("P")) {
            System.out.println("Moving Pacman");
            List<String> splitedLine = List.of(nextLine.split(" "));
            List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                    .map(Integer::parseInt)
                    .toList();

            try {
                maze.getPacMan().move(maze.getField(coords.get(0), coords.get(1)));
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
        } else if (nextLine.startsWith("G")) {
            System.out.println("Moving Ghost");
            List<String> splitedLine = List.of(nextLine.split(" "));
            List<Integer> coords = Arrays.stream(splitedLine.get(1).split("/"))
                    .map(Integer::parseInt)
                    .toList();

            try {
                maze.ghosts().get(Integer.parseInt(splitedLine.get(0).substring(1))).move(maze.getField(coords.get(0), coords.get(1)));
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
        timer.cancel();
    }





    /**
     * Checks if Pacman and Ghosts are on the same field. If so, checks if Ghost is eatable. If yes, Ghost is moved to start. If not, Pacman and Ghosts are moved to start.
     * @throws GameException
     */
    private void checkCollision() throws GameException{
        for (MazeObject mazeObject : maze.ghosts()) {
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
    public void newGame(){
        gameState = GameState.DEFAULT;
        generateGame();
        startLogging();
    }

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
            case REPLAY: {
                endLogging();
                loadGameFromSave();
            }
        }

    }

}
