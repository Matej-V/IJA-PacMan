package ija.project.pacman_project;

import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.game.*;
import ija.project.common.Field;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class PacManModel {
    String currentMap;
    Maze maze;
    public PacmanObject pacman;
    public List<String> ghostsPaths;

    String fileID;


    public PacManModel() {
        generateGame();
        this.pacman = (PacmanObject) this.maze.getPacMan();
        this.ghostsPaths = new ArrayList<>();
    }

    public void generateGame() {
        chooseRandomMap();
    }

    public void chooseRandomMap() {
        File folder = new File("./src/main/resources/ija/project/pacman_project/maps");
        File[] listOfFiles = folder.listFiles();
        List<File> files = Arrays.asList(listOfFiles);
        Random rand = new Random();
        // Get random file from list if same as current map, get another one
        File randomFile = files.get(rand.nextInt(files.size()));
        while (randomFile.getName().equals(currentMap)) {
            randomFile = files.get(rand.nextInt(files.size()));
        }
        // Set new map
        setMap(randomFile.getName(), 0);
    }

    /**
     * Method for setting map of the game
     * @param map chosen map
     * @param mode selected mode, 0 for new game, 1 for save
     */
    public void setMap(String map, int mode) {
        this.currentMap = map;
        this.loadFile(PacManApp.class.getResource("maps/" + currentMap), mode);
    }

    /**
     * Method for loading file from game resources
     *
     * @param file path to file (either just a map or a save)
     * @param mode selected mode, 0 for new game, 1 for save
     */
    public void loadFile(URL file, int mode) {
        if (mode == 0) {
            this.fileID = file.getFile().substring(file.getFile().length() - 6, file.getFile().length() - 4);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.openStream()))) {
            String line;
            MazeConfigure cfg = new MazeConfigure();
            line = br.readLine();
            String[] words = line.split("\\s+");

            Integer lives = 3;
            Integer score = 0;
            if (mode == 1) {
                lives = Integer.parseInt(words[2]);
                score = Integer.parseInt(words[3]);
                Integer ghostsAmount = Integer.parseInt(words[4]);
                for (int i = 0; i < ghostsAmount + 1; i++) {
                    line = br.readLine();
                    String[] p = line.split("\\s+");
                    if (i == 0) { // here is always pacman path
                        this.pacman.setPath(p[1]);
                        continue;
                    }
                    this.ghostsPaths.add(line);
                }
            }

            cfg.startReading(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            while ((line = br.readLine()) != null) {
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

    public void loadGame() {
        this.loadFile(PacManApp.class.getResource("saves/" + "save" + fileID + ".save"), 1);
    }

    public void saveFile(){
        URL folder = PacManApp.class.getResource("saves");
        String path = folder.toString() + "save" + fileID + ".save";
    }

    public void moveGhosts() {
        ListIterator<MazeObject> it = this.maze.ghosts().listIterator();
        while (it.hasNext()){
            GhostObject ghost = (GhostObject) it.next();
            chaseAlgorithm(ghost);
            ghost.move(ghost.getDirection());
        }
    }

    public void movePacman() {
        this.pacman.move(pacman.getDirection());
    }

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
}
