package ija.project.game;

import ija.project.common.AbstractObservableField;
import ija.project.common.Field;
import ija.project.common.Maze;
import ija.project.common.MazeObject;

import java.util.ArrayList;
import java.util.List;

public class TargetField extends AbstractObservableField implements Field {
    private final int row; // row of the field
    private final int col; // column of the field
    private Maze maze; // maze to which the field belongs
    private final List<MazeObject> mazeObjects; // list of objects on the field - extension for future

    /**
     * Constructor.
     *
     * @param row row of the field
     * @param col column of the field
     */
    public TargetField(int row, int col) {
        this.row = row;
        this.col = col;
        this.mazeObjects = new ArrayList<MazeObject>();
        this.maze = null;
    }
    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public Field nextField(Direction dirs) {
        return switch (dirs) {
            case D -> this.maze.getField(this.row + 1, this.col);
            case L -> this.maze.getField(this.row, this.col - 1);
            case R -> this.maze.getField(this.row, this.col + 1);
            case U -> this.maze.getField(this.row - 1, this.col);
        };
    }

    public boolean put(MazeObject object){
        if(object instanceof PacmanObject){
            for(MazeObject mz : this.mazeObjects){
                if(mz instanceof GhostObject){
                    ((PacmanObject) object).decreaseLives();
                }
            }
        }
        if(object instanceof GhostObject){
            for(MazeObject mz : this.mazeObjects){
                if(mz instanceof PacmanObject){
                    ((PacmanObject) this.mazeObjects.get(0)).decreaseLives();
                }
            }
        }

        this.mazeObjects.add(object);
        //notifyObservers();
        return true;
    }

    public boolean remove(MazeObject object) {
        this.mazeObjects.remove(object);
        //notifyObservers();
        return true;
    }

    public boolean isEmpty() {
        return this.mazeObjects.isEmpty();
    }

    public MazeObject get() {
        if (this.mazeObjects.isEmpty()) {
            return null;
        }
        return this.mazeObjects.get(0);
    }

    public boolean canMove() {
        return true;
    }

    /**
     *
     * Compares objects. Objects are equal if both represent TargetField and
     * are at the same position
     *
     * @param obj object to be compared with
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TargetField target) {
            return target.row == this.row && target.col == this.col;
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
        return false;
    }
}
