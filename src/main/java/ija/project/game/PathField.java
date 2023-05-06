package ija.project.game;

import ija.project.common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the path field.
 */
public class PathField extends AbstractObservableField implements Field {
    /**
     * Row of the field.
     */
    private final int row;
    /**
     * Column of the field
     */
    private final int col;
    /**
     * Maze to which the field belongs.
     */
    protected Maze maze;
    /**
     * List of objects on the field.
     */
    protected final List<MazeObject> mazeObjects;
    /**
     * Indicates whether the field contains a point or not.
     */
    public boolean point;

    /**
     * Constructor.
     *
     * @param row row of the field
     * @param col column of the field
     */
    public PathField(int row, int col) {
        this.row = row;
        this.col = col;
        this.mazeObjects = new ArrayList<MazeObject>();
        this.maze = null;
        this.point = true;
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
     * @param dirs direction
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
     * @return row of the field.
     * @throws Exception if the field is WallField exception
     *                   UnsupportedOperationException is thrown.
     */
    public boolean put(MazeObject object) throws GameException{
        if (object instanceof PacmanObject) {
            ((PacmanObject) object).pointCollected = false;
            if (this.point) {
                ((PacmanObject) object).updateScore();
                ((PacmanObject) object).pointCollected = true;
                this.point = false;
            }
        }
        this.mazeObjects.add(object);
        notifyObservers();
        return true;
    }

    /**
     * Removes the object from the field.
     *
     * @param object object to be removed from the field.
     */
    public boolean remove(MazeObject object) {
        this.mazeObjects.remove(object);
        notifyObservers();
        return true;
    }

    /**
     * Checks whether the field is empty.
     *
     * @return True if the field is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.mazeObjects.isEmpty();
    }

    /**
     * Returns the list of objects on the field.
     *
     * @return list of objects on the field
     */
    @Override
    public List<MazeObject> get() {
        return this.mazeObjects;
    }

    /**
     * Checks whether the field is path field.
     *
     * @return True if the field is path field, false otherwise
     */
    @Override
    public boolean canMove() {
        return true;
    }

    /**
     * Returns the maze to which the field belongs.
     *
     * @return maze to which the field belongs
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
     * @return True if the field has point, false otherwise
     */
    @Override
    public boolean hasPoint() {
        return this.point;
    }

    /**
     * Check if the field has key.
     *
     * @return True if the field has key, false otherwise
     */
    public boolean hasKey() {
        boolean result = false;
        for (MazeObject o : mazeObjects) {
            if (o instanceof KeyObject) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Get the key from the field.
     *
     * @return KeyObject
     */
    public KeyObject getKey() {
        for (MazeObject o : mazeObjects) {
            if (o instanceof KeyObject) {
                return (KeyObject) o;
            }
        }
        return null;
    }

    /**
     * Compares objects. Objects are equal if both represent PathField and
     * are at the same position
     *
     * @param obj object to be compared with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PathField path) {
            return path.row == this.row && path.col == this.col;
        }
        return false;
    }
}
