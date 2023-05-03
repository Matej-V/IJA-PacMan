package ija.project.pacman_project;

import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.game.*;
import ija.project.common.Field;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacManModel {
    private String currentMap;
    public Maze maze;
    public PacmanObject pacman;
    public int totalScore;

    public PacManModel() {
        newGame();
    }

    public void generateGame() {
        chooseRandomMap();
        pacman = (PacmanObject) this.maze.getPacMan();
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
        setMap(randomFile.getName());
    }

    public void setMap(String map) {
        this.currentMap = map;
        this.loadFile(PacManApp.class.getResource("maps/" + currentMap));
    }

    public void loadFile(URL file) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.openStream()))) {
            String line;
            MazeConfigure cfg = new MazeConfigure();
            line = br.readLine();
            String[] words = line.split("\\s+");
            cfg.startReading(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            while ((line = br.readLine()) != null) {
                cfg.processLine(line);
            }
            cfg.stopReading();
            this.maze = cfg.createMaze();
            this.pacman = (PacmanObject) this.maze.getPacMan();
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
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
        checkColision();
    }

    /**
     * Moves Pacman in currently set direction
     * @throws GameException
     */
    public void movePacman() throws GameException {
        this.pacman.move(pacman.getDirection());
        checkColision();
        checkWin();
    }


    /**
     * Checks if Pacman and Ghosts are on the same field. If so, checks if Ghost is eatable. If yes, Ghost is moved to start. If not, Pacman and Ghosts are moved to start.
     * @throws GameException
     */
    private void checkColision() throws GameException{
        for (MazeObject mazeObject : maze.ghosts()) {
            GhostObject ghost = (GhostObject) mazeObject;
            if (ghost.getField().equals(pacman.getField())) {
                if(ghost.isEatable()){
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
        if(pacman.getField() instanceof TargetField){
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
     * Generates next maze for player. Used when previous game completed successfully. Total score is as sum of previous games.
     */
    public void nextGame(){
        generateGame();
    }

    /**
     * Generates new game for player. Total score is set to 0.
     */
    public void newGame(){
        totalScore = 0;
        generateGame();
    }

    public void updateTotalScore(){
        totalScore += pacman.getScore();
    }

}
