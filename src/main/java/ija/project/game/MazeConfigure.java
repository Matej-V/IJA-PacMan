package ija.project.game;

import ija.project.common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for configuring the maze.
 */
public class MazeConfigure {
    private int rows;
    private int cols;
    private int rowToBeProcessed;
    private boolean errorIndicator;
    private ArrayList<MazeObject> ghosts = new ArrayList<MazeObject>();
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
                    startField.put(pacmanObject1);
                }
                case 'G', 'g' -> {
                    PathField field = new PathField(this.rowToBeProcessed, c + 1);
                    field.setMaze(this.maze);
                    GhostObject ghostObject = new GhostObject(field);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1, field);
                    field.put(ghostObject);
                    this.ghosts.add(ghostObject);
                }
                case 'T', 't' -> {
                    TargetField target = new TargetField(this.rowToBeProcessed, c + 1);
                    target.setMaze(this.maze);
                    this.maze.fields.get(this.rowToBeProcessed).set(c+1, target);
                }
                // TODO key field
                case 'K', 'k' -> {
                    PathField keyField = new PathField(this.rowToBeProcessed, c + 1);
                    keyField.setMaze(this.maze);
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
     * @return Maze Maze object
     */
    public Maze createMaze() {
        if (!this.errorIndicator) {
            maze.setGhosts(this.ghosts);
            return maze;
        }
        return null;
    }

}
