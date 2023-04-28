package ija.project.game;

import javafx.scene.Group;

import java.util.ArrayList;

import ija.project.common.*;

import java.util.List;

/**
 * Class representing the maze.
 */
public class MazeClass implements Maze {
    private final int numOfRows;
    private final int numOfCols;
    protected List<List<Field>> fields;
    private List<MazeObject> ghosts = new ArrayList<MazeObject>();
    private MazeObject PacMan;
    private final Group mazeGroup;

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
        this.mazeGroup = new Group();
    }

    public Group getGroup(){
        return this.mazeGroup;
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
    public List<MazeObject> ghosts() {
        return new ArrayList<>(this.ghosts);
    }

    /**
     * Prints a string representation of the maze to stdOut
     * 
     */
    public void printMaze() {
        for (int row = 0; row < this.numOfRows; row++) {
            for (int column = 0; column < this.numOfCols; column++) {
                if (this.fields.get(row).get(column) instanceof WallField) {
                    System.out.print('X');
                }
                if (this.fields.get(row).get(column) instanceof PathField) {
                    if( this.fields.get(row).get(column).get() != null ){
                        System.out.print('S');
                    }else{
                        System.out.print('.');
                    }
                }
                if (this.fields.get(row).get(column) == null) {
                    System.out.print('-');
                }
            }
            System.out.println();
        }
    }

    @Override
    public void moveObjectsToStart() {
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

}

