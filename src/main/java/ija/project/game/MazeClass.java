package ija.project.game;

import java.util.ArrayList;
import ija.project.common.*;
import java.util.List;

/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Class representing maze. Maze is a 2D array of fields.
 */
public class MazeClass implements Maze {
    /**
     * Number of rows in the maze
     */
    private final int numOfRows;
    /**
     * Number of columns in the maze
     */
    private final int numOfCols;
    /**
     * Existing fields in the maze
     */
    protected List<List<Field>> fields;
    /**
     * Existing ghosts in the maze
     */
    private List<MazeObject> ghosts = new ArrayList<MazeObject>();
    /**
     * Existing keys in the maze
     */
    private List<MazeObject> keys = new ArrayList<MazeObject>();

    /**
     * Collected keys in the maze.
     */
    private List<MazeObject> oldKeys = new ArrayList<MazeObject>();
    /**
     * Pacman in the maze
     */
    private MazeObject PacMan;
    /**
     * The number of keys that need to be collected to pass the maze
     */
    public int keysToCollect;
    /**
     * Target field in the maze
     */
    public Field target;

    /**
     * Constructor.
     *
     * @param rows number of rows of the maze
     * @param cols number of columns of the maze
     */
    public MazeClass(int rows, int cols) {
        this.numOfRows = rows + 2;
        this.numOfCols = cols + 2;
        this.fields = new ArrayList<List<Field>>();
        for (int row = 0; row < this.numOfRows; row++) {
            fields.add(new ArrayList<Field>(cols));
            for (int column = 0; column < this.numOfCols; column++) {
                if (row == 0 || column == 0 || row == (this.numOfRows - 1) || column == (this.numOfCols - 1)) {
                    WallField field = new WallField(row, column);
                    field.setMaze(this);
                    this.fields.get(row).add(field);
                } else {
                    this.fields.get(row).add(null);
                }
            }
        }
    }

    /**
     * Associates ghosts with the maze.
     *
     * @param ghosts ghosts to be associated with the maze
     */
    public void setGhosts(List<MazeObject> ghosts) {
        this.ghosts = ghosts;
    }

    /**
     * Associates pacman with the maze.
     *
     * @param pacMan pacman to be associated with the maze
     */
    public void setPacMan(MazeObject pacMan){
        this.PacMan = pacMan;
    }

    /**
     * Returns a field at a given position.
     * 
     * @param row row of the field
     * @param col column of the field
     * @return Field at the given position
     */
    @Override
    public Field getField(int row, int col) {
        if (row < 0 || row >= this.numOfRows || col < 0 || col >= this.numOfCols) {
            return null;
        }
        return this.fields.get(row).get(col);
    }

    /**
     * Returns ghosts in the maze.
     *
     * @return List of ghosts
     */
    @Override
    public List<MazeObject> getGhosts() {
        return new ArrayList<>(this.ghosts);
    }

    /**
     * Returns pacman in the maze.
     *
     * @return Pacman in the maze
     */
    public MazeObject getPacMan(){
        return this.PacMan;
    }


    /**
     * Returns number of rows in the maze.
     * 
     * @return The number of rows in the maze
     */
    @Override
    public int numRows() {
        return (this.numOfRows);
    }

    /**
     * Returns number of columns in the maze.
     * 
     * @return The number of columns in the maze
     */
    @Override
    public int numCols() {
        return (this.numOfCols);
    }

    /**
     * Moves all objects to their start positions. Calls a method for every object to move to its start field.
     *
     * @throws GameException Exception to handle game scenarios such as completed game or lost game.
     */
    @Override
    public void moveObjectsToStart() throws GameException {
        PacMan.moveToStart();
        for (MazeObject mazeObject : ghosts) {
            GhostObject ghost = (GhostObject) mazeObject;
            ghost.moveToStart();
        }
    }

    /**
     * Adds key to the maze.
     *
     * @param key Key to add to the maze
     * @return true if key was added to maze, otherwise false
     */
    public boolean addKey(MazeObject key) {
        if(key != null) {
            this.keysToCollect++;
            this.keys.add(key);
            return true;
        }
        return false;
    }

    /**
     * Removes key from the maze.
     *
     * @param key Key to remove from the maze
     */
    public void removeKey(MazeObject key){
        ((KeyObject)key).collectKey();
        this.keysToCollect--;
        this.keys.remove(key);
        this.oldKeys.add(key);
    }

    /**
     * Check if pacman can complete the maze.
     *
     * @return true if all the keys are collected, false otherwise
     */
    public boolean canComplete(){
        return (keys.size() == 0);
    }

    /**
     * Returns target field in the maze
     *
     * @return Field target
     */
    public Field getTarget() {
        return this.target;
    }

    /**
     * Returns the list of collected keys.
     * @return list of keys
     */
    public List<MazeObject> getOldKeys() {
        return this.oldKeys;
    }
}

