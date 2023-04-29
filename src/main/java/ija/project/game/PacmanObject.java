package ija.project.game;

import ija.project.common.*;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class representing the pacman object.
 */
public class PacmanObject extends AbstractObservableObject implements MazeObject {
    private PathField field;
    private final PathField startField;
    private Integer lives;
    private Integer score;
    private Field.Direction direction;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructor for PacmanObject.
     * 
     * @param field field on which the object is located
     */
    public PacmanObject(PathField field) {
        this.field = field;
        this.startField = field;
        this.lives = 3;
        this.score = 0;
        this.direction = Field.Direction.U;
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
     * Check if object is PacMan
     * @return True if object is PacMan, otherwise false
     */
    @Override
    public boolean isPacman() {
        return true;
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
                return false;
            } else {
                PathField nextField = (PathField) this.field.nextField(dir);
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
     * Returns the field on which the object is located.
     * 
     * @return Field on which the object is located.
     */
    public Field getField() {
        return this.field;
    }

    /**
     * Returns the number of lives of the pacman.
     * 
     * @return Number of lives of the pacman.
     */
    public int getLives() {
        return this.lives;
    }

    /**
     * Returns score in current game
     * @return Score of the pacman in the game
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Updates score of the pacman in the game
     *
     */
    public void updateScore(){
        this.score++;
        /* Notification for UI view update */
        notifyObservers();
    }

    @Override
    public Field.Direction getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(Field.Direction dir) {
        this.direction = dir;
        /* Notify view when direction is changed to change view of PacMan */
        notifyObservers();
    }

    /**
     * Decreases the number of lives of the pacman.
     * 
     */
    public void decreaseLives() {
        this.lives--;
        /* Notification for UI view update */
        notifyObservers();
    }

    /**
     * Moves the pacman to the start position.
     * 
     */
    public void moveToStart() {
        try {
            lock.writeLock().lock();
            this.field.remove(this);
            this.startField.put(this);
            this.field = this.startField;
            this.decreaseLives();
        }finally {
            lock.writeLock().unlock();
        }
    }
}
