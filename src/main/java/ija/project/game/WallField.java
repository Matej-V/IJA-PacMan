package ija.project.game;

import ija.project.common.*;
import java.util.List;

/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Class representing wall field.
 */
public class WallField extends AbstractObservableField implements Field {
    /**
     * Row of the field.
     */
    private final int row;
    /**
     * Column of the field.
     */
    private final int col;
    /**
     * Maze to which the field belongs
     */
    private Maze maze;

    /**
     * Constructor.
     * 
     * @param row row of the field
     * @param col column of the field
     */
    public WallField(int row, int col) {
        this.row = row;
        this.col = col;
        this.maze = null;
    }

    /**
     * Associates the field with the maze.
     *
     * @param maze maze to which the field belongs
     */
    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    /**
     * Returns neighboring field in the given direction.
     *
     * @param dirs direction.
     * @return Field neighboring field in the given direction.
     */
    @Override
    public Field nextField(Direction dirs) {
        return switch (dirs) {
            case D -> this.maze.getField(this.row + 1, this.col);
            case L -> this.maze.getField(this.row, this.col - 1);
            case R -> this.maze.getField(this.row, this.col + 1);
            case U -> this.maze.getField(this.row - 1, this.col);
        };
    }

    /**
     * Puts the object on the field.
     *
     * @param object object to be put on the field.
     * @throws UnsupportedOperationException since it is impossible to place any object on the wall field.
     */
    public boolean put(MazeObject object) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Error");
    }

    /**
     * Removes the object from the field.
     *
     * @param object object to be removed from the field.
     * @return false since wall field cannot contain any object.
     */
    public boolean remove(MazeObject object) {
        System.out.println("Unsupported operation");
        return false;
    }

    /**
     * Checks whether the field is empty.
     *
     * @return true since wall field never contains an object.
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * Returns the list of objects on the field.
     *
     * @return null since wall field never contains an object.
     */
    @Override
    public List<MazeObject> get() {
        return null;
    }

    /**
     * Checks whether the field is path field.
     *
     * @return false since it is wall field.
     */
    @Override
    public boolean canMove() {
        return false;
    }


    /**
     * Returns the maze to which the field belongs.
     *
     * @return maze to which the field belongs.
     */
    @Override
    public Maze getMaze() {
        return maze;
    }

    /**
     * Returns the row of the field.
     *
     * @return row of the field
     */
    @Override
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column of the field.
     *
     * @return column of the field
     */
    @Override
    public int getCol() {
        return this.col;
    }

    /**
     * Check if the field has point.
     *
     * @return false since wall field cannot contain a point
     */
    @Override
    public boolean hasPoint() {
        return false;
    }

    /**
     * Check if the field has key.
     *
     * @return false since wall field cannot contain a key
     */
    public boolean hasKey() {
        return false;
    }

    /**
     * Get the key from the field.
     *
     * @return KeyObject
     */
    @Override
    public KeyObject getKey() {
        return null;
    }

    /**
     * Compares objects. Objects are equal if both represent WallField and
     * are at the same position.
     * 
     * @param obj object to be compared with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WallField wall) {
            return (wall.row == this.row && wall.col == this.col);
        }
        return false;
    }


}
