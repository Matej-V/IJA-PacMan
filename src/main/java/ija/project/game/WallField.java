package ija.project.game;

import ija.project.common.*;
//import ija.ija2022.homework2.tool.common.AbstractObservableField;

/**
 * Class representing the wall field.
 */
public class WallField extends AbstractObservableField implements Field {
    private final int row; // row of the field
    private final int col; // column of the field
    private Maze maze; // maze to which the field belongs

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

    public boolean put(MazeObject object) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Error");
    }

    public boolean remove(MazeObject object) {
        System.out.println("Unsupported operation");
        return false;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public MazeObject get() {
        return null;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    /**
     *
     * Compares objects. Objects are equal if both represent WallField and
     * are at the same position
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

    @Override
    public boolean contains(MazeObject object) {
        return false;
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
