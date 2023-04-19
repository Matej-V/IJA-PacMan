package ija.project.pacman_project;

import ija.project.common.Maze;
import ija.project.game.MazeConfigure;
import ija.project.game.PacmanObject;
import ija.project.common.Field;


import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class PacManModel {
    String currentMap;
    Maze maze;

    private PacmanObject pacman;

    public PacManModel(){
        generateGame();
    }
    public void generateGame(){
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
        setMap(randomFile.getName());
    }

    public void setMap(String map) {
        this.currentMap = map;
        this.loadFile(PacManApp.class.getResource("maps/" + currentMap));
    }
    public void loadFile(URL file){
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

    public void movePacman(Field.Direction dir){
        System.out.println("\tPacman in maze before: " + this.maze.getPacMan().getField());
        this.pacman.move(dir);
        this.maze.setPacMan(this.pacman);
        System.out.println("\tPacman in maze before: " + this.maze.getPacMan().getField());
        System.out.println("Model: moving pacman " + dir);
    }

    public Maze getMazeRepresentation(){
        return this.maze;
    }
}
