package ija.project.game;

import ija.project.common.*;
//import ija.ija2022.homework2.tool.common.AbstractObservableField;


import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the path field.
 */
public class PathField implements Field {
    private final int row; // row of the field
    private final int col; // column of the field
    private Maze maze; // maze to which the field belongs
    private final List<MazeObject> mazeObjects; // list of objects on the field - extension for future
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

    public boolean put(MazeObject object) {
        if(object instanceof PacmanObject){
            if(this.point){
                this.point = false;
            }
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

    @Override
    public boolean isEmpty() {
        return this.mazeObjects.isEmpty();
    }

    @Override
    public MazeObject get() {
        if (this.mazeObjects.isEmpty()) {
            return null;
        }
        return this.mazeObjects.get(0);
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

}
