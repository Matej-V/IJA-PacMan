package ija.project.game;

import ija.project.common.AbstractObservableObject;
import ija.project.common.Field;
import ija.project.common.Maze;
import ija.project.common.MazeObject;

import java.nio.file.Path;

public class KeyObject extends AbstractObservableObject implements MazeObject {
    private Field field;
    private Maze maze;

    public KeyObject(PathField field, Maze maze){
        this.field = field;
        this.maze = maze;
    }
    public void collectKey(){
        this.field.remove(this);
        this.field = null;
        this.maze.colllectKey();
    }
    @Override
    public boolean canMove(Field.Direction dir) {
        return false;
    }

    @Override
    public boolean move(Field.Direction dir) {
        return false;
    }

    @Override
    public boolean isPacman() {
        return super.isPacman();
    }

    @Override
    public Field getField() {
        return this.field;
    }

    @Override
    public int getLives() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getScore() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Field.Direction getDirection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDirection(Field.Direction dir) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void moveToStart() {
        throw new UnsupportedOperationException();
    }
}
