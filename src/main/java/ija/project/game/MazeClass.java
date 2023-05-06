package ija.project.game;

import javafx.scene.Group;

import java.io.*;
import java.util.ArrayList;

import ija.project.common.*;

import java.util.List;
import java.util.ListIterator;

/**
 * Class representing the maze.
 */
public class MazeClass implements Maze {
    private final int numOfRows;
    private final int numOfCols;
    protected List<List<Field>> fields;
    private List<MazeObject> ghosts = new ArrayList<MazeObject>();
    private List<MazeObject> keys = new ArrayList<MazeObject>();
    private MazeObject PacMan;
    public int keysToCollect;
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

    /* TODO */
    public void setGhosts(List<MazeObject> ghosts) {
        this.ghosts = ghosts;
    }

    /**
     * Returns a field at a given position.
     * 
     * @param row row of the field
     * @param col column of the field
     * @return Field
     */
    @Override
    public Field getField(int row, int col) {
        if (row < 0 || row >= this.numOfRows || col < 0 || col >= this.numOfCols) {
            return null;
        }
        return this.fields.get(row).get(col);
    }

    /**
     * Returns number of rows in the maze.
     * 
     * @return int number of rows
     */
    @Override
    public int numRows() {
        return (this.numOfRows);
    }

    /**
     * Returns number of columns in the maze.
     * 
     * @return int number of columns
     */
    @Override
    public int numCols() {
        return (this.numOfCols);
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


    @Override
    public void moveObjectsToStart() throws GameException {
        PacMan.moveToStart();
        for (MazeObject mazeObject : ghosts) {
            GhostObject ghost = (GhostObject) mazeObject;
            ghost.moveToStart();
        }
    }

    public void setPacMan(MazeObject pacMan){
        this.PacMan = pacMan;
    }

    public MazeObject getPacMan(){
        return this.PacMan;
    }

    public boolean canComplete(){
        return keysToCollect == 0;
    }

    public boolean addKey(MazeObject key){
        if(key != null) {
            this.keys.add(key);
            return true;
        }
        return false;
    }

    public void removeKey(MazeObject key){
        ((KeyObject)key).collectKey();
        this.keys.remove(key);
    }
}

