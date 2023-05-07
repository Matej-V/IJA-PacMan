package ija.project.game;

import ija.project.common.*;
import java.util.ArrayList;

/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Class representing maze configuration. It reads the maze from the file and creates a maze.
 */
public class MazeConfigure {
    /**
     * Number of rows in the maze
     */
    private int rows;
    /**
     * Number of columns in the maze
     */
    private int cols;
    /**
     * The number of the line to be processed from the file
     */
    private int rowToBeProcessed;
    /**
     * Processing error indicator
     */
    private boolean errorIndicator;
    /**
     * Ghosts processed from a file
     */
    private final ArrayList<MazeObject> ghosts = new ArrayList<MazeObject>();
    /**
     * A maze to be configured
     */
    private MazeClass maze;

    /**
     * Constructor for MazeConfigure.
     */
    public MazeConfigure() {
        this.rowToBeProcessed = 1;
        this.errorIndicator = false;
        this.maze = null;
    }

    /**
     * Initializes the reading of the maze.
     * 
     * @param rows number of rows to be read
     * @param cols number of columns to be read
     */
    public void startReading(int rows, int cols) {
        this.maze = new MazeClass(rows, cols);
        this.rows = this.maze.numRows();
        this.cols = this.maze.numCols();
    }

    /**
     * Processes the line of the maze.
     *
     * @param line line to be processed
     * @return True if the line was processed, false otherwise
     */
    public boolean processLine(String line) {
        if ((this.cols - 2) != line.length()) {
            System.err.println("Chybna velkost riadku. [Row number: " + this.rowToBeProcessed + ", expected length: "
                    + (this.cols - 2) + ", actual length:" + line.length() + "]");
            this.errorIndicator = false;
            return false;
        }
        for (int c = 0; c < line.length(); c++) {
            char character = line.charAt(c);
            switch (character) {
                case '.' -> {
                    PathField pathField = new PathField(this.rowToBeProcessed, c + 1);
                    pathField.setMaze(this.maze);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1,pathField);
                }
                case 'X', 'x' -> {
                    WallField wallField = new WallField(this.rowToBeProcessed, c + 1);
                    wallField.setMaze(this.maze);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1,wallField);
                }
                case 'S', 's' -> {
                    PathField startField = new PathField(this.rowToBeProcessed, c + 1);
                    startField.setMaze(this.maze);
                    PacmanObject pacmanObject1 = new PacmanObject(startField);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1,startField);
                    try {
                        startField.put(pacmanObject1);
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                    this.maze.setPacMan(pacmanObject1);
                }
                case 'G', 'g' -> {
                    PathField field = new PathField(this.rowToBeProcessed, c + 1);
                    field.setMaze(this.maze);
                    GhostObject ghostObject = new GhostObject(field, ghosts.size());
                    this.maze.fields.get(this.rowToBeProcessed).set(c + 1, field);
                    try {
                        field.put(ghostObject);
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                    this.ghosts.add(ghostObject);
                }
                case 'T', 't' -> {
                    TargetField target = new TargetField(this.rowToBeProcessed, c + 1);
                    target.setMaze(this.maze);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1, target);
                    this.maze.target = target;
                }
                case 'K', 'k' -> {
                    PathField keyField = new PathField(this.rowToBeProcessed, c + 1);
                    keyField.setMaze(this.maze);
                    KeyObject key = new KeyObject(keyField);
                    try {
                        keyField.put(key);
                    } catch (GameException e) {
                        throw new RuntimeException(e);
                    }
                    this.maze.addKey(key);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1, keyField);
                }
                default -> {
                    System.err.println("Chybny vstupny prvok pola.");
                    this.errorIndicator = false;
                    return false;
                }
            }
        }
        this.rowToBeProcessed += 1;
        return true;
    }

    /**
     * Ends the reading of the map. End-of-file simulation.
     * 
     * @return True if the reading was successful, false otherwise
     */
    public boolean stopReading() {
        if ((this.rows - 1) != this.rowToBeProcessed || this.errorIndicator) {
            System.err.println("Nepodarilo sa vytvorit bludisko.");
            this.errorIndicator = true;
            return false;
        }
        return true;
    }

    /**
     * Creates the maze.
     * 
     * @return Maze object if the configuration was successful, null otherwise
     */
    public Maze createMaze() {
        if (!this.errorIndicator) {
            maze.setGhosts(this.ghosts);
            return maze;
        }
        return null;
    }
}
