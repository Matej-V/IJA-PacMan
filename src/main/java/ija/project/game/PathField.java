package ija.project.game;

import ija.project.common.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the path field.
 */
public class PathField extends AbstractObservableField implements Field {
    private final int row; // row of the field
    private final int col; // column of the field
    protected Maze maze; // maze to which the field belongs
    protected final List<MazeObject> mazeObjects; // list of objects on the field - extension for future
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

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    @Override
    public Field nextField(Direction dirs) {
        return switch (dirs) {
            case D -> this.maze.getField(this.row + 1, this.col);
            case L -> this.maze.getField(this.row, this.col - 1);
            case R -> this.maze.getField(this.row, this.col + 1);
            case U -> this.maze.getField(this.row - 1, this.col);
        };
    }

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
        System.out.println("Notified PUT");
        notifyLogObservers(object);
        return true;
    }

    public boolean remove(MazeObject object) {
        this.mazeObjects.remove(object);
        notifyObservers();
        System.out.println("Notified REMOVE");
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.mazeObjects.isEmpty();
    }

    @Override
    public List<MazeObject> get() {
        return this.mazeObjects;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    /**
     *
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

    @Override
    public boolean contains(MazeObject object) {
        return this.mazeObjects.contains(object);
    }

    @Override
    public int getRow() {
        return this.row;
    }

    @Override
    public int getCol() {
        return this.col;
    }

    @Override
    public boolean hasPoint() {
        return this.point;
    }

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

    public KeyObject getKey() {
        for (MazeObject o : mazeObjects) {
            if (o instanceof KeyObject) {
                return (KeyObject) o;
            }
        }
        return null;
    }
}
