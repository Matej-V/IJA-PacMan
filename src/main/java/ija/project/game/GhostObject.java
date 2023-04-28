package ija.project.game;

import ija.project.common.AbstractObservableObject;
import ija.project.common.Field;
import ija.project.common.MazeObject;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GhostObject extends AbstractObservableObject implements MazeObject {
    private PathField field;
    private final PathField startField;
    public Color color;
    private Field.Direction direction;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructor for GhostObject.
     *
     * @param field field on which the object is located
     */
    public GhostObject(PathField field, Color color) {
        this.field = field;
        this.startField = field;
        this.color = color;
        this.direction = Field.Direction.values()[new Random().nextInt(Field.Direction.values().length)];
    }

    /**
     * Verifies whether it is possible to move the object in the specified
     * direction.
     *
     * @param dir Direction in which the object should be moved.
     * @return True if the move is possible, false otherwise.
     */
    @Override
    public boolean canMove(Field.Direction dir) {
        Field nextField = field.nextField(dir);
        return nextField instanceof PathField;
    }

    /**
     * Moves the object in the specified direction if possible.
     *
     * @param dir Direction in which the object should be moved.
     * @return True if the move was successful, false otherwise.
     */
    @Override
    public boolean move(Field.Direction dir) {
        try {
            lock.writeLock().lock();
            if (!canMove(dir)) {
                lock.writeLock().unlock();
                return false;
            } else {
                PathField nextField = (PathField) field.nextField(dir);
                this.field.remove(this);
                if (nextField.put(this)) {
                    this.field = nextField;
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
        return true;
    }
    
    /**
     * Returns true if the object is a pacman.
     *
     * @return True if the object is a pacman, false otherwise.
     */
    @Override
    public boolean isPacman() {
        return false;
    }


    /**
     * Returns the field on which the object is located.
     *
     * @return Field on which the object is located.
     */
    public Field getField() {
        return this.field;
    }

    /* Return 0 or error? TODO */
    public int getLives() {
        return 0;
    }

    @Override
    public Field.Direction getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(Field.Direction dir) {
        notifyObservers();
        this.direction = dir;
    }

    /**
     * Removes object from a current <code>field</code> and moves it to a starting field <code>startField</code>
     */
    @Override
    public void moveToStart() {
        try {
            lock.writeLock().lock();
            this.field.remove(this);
            this.startField.put(this);
            this.field = this.startField;
        }finally {
            lock.writeLock().unlock();
        }
    }
}
