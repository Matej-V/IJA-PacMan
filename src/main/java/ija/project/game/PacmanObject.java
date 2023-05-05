package ija.project.game;

import ija.project.common.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class representing the pacman object.
 */
public class PacmanObject extends AbstractObservableObject implements MazeObject {
    private Field field;
    private final PathField startField;
    private Integer lives;
    private Integer score;
    private Field.Direction direction;
    private final List<Field.Direction> path;
    ReadWriteLock lock = new ReentrantReadWriteLock();
    public boolean pointCollected;

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
        this.path = new ArrayList<>();
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
    public boolean move(Field.Direction dir) throws GameException{
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
                if(this.field.hasKey()){
                    this.field.getKey().collectKey();
                }
            }
            System.out.println("Pacman: " + getField().getRow() + getField().getCol());
        }finally {
            lock.writeLock().unlock();
        }
        notifyLogObservers(this);
        return true;
    }

    @Override
    public boolean move(Field field) throws GameException {
        try {
            lock.writeLock().lock();
            if(field.canMove()){
                //set direction accroding to the field position and current position
                if(this.field.getRow() == field.getRow()){
                    if(this.field.getCol() < field.getCol()){
                        setDirection(Field.Direction.R);
                    }else{
                        setDirection(Field.Direction.L);
                    }
                }else{
                    if(this.field.getRow() < field.getRow()){
                        setDirection(Field.Direction.D);
                    }else{
                        setDirection(Field.Direction.U);
                    }
                }

                this.field.remove(this);
                if (((PathField)field).put(this)) {
                    this.field = field;
                }
                if(this.field.hasKey()){
                    this.field.getKey().collectKey();
                }
            }else {
                return false;
            }
        }finally {
            lock.writeLock().unlock();
        }
        notifyLogObservers(this);
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
     * Method to set amount of pacman lives (from save file)
     * @param lives Saved lives
     */
    public void setLives(int lives){
        this.lives = lives;
        notifyObservers();
    }

    /**
     * Returns score in current game
     * @return Score of the pacman in the game
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Method to set score in current pacman game (from save file)
     * @param score Saved score
     */
    public void setScore(int score){
        this.score = score;
        notifyObservers();
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
    public void decreaseLives() throws GameException {
        this.lives--;
        System.out.println("Lives: "+ lives);
        /* Notification for UI view update */
        if(lives == 0){
            throw new GameException(GameException.TypeOfException.LostGame);
        }
        notifyObservers();
    }

    /**
     * Moves the pacman to the start position.
     * 
     */
    public void moveToStart() throws GameException {
        try {
            lock.writeLock().lock();
            move(startField);
            this.field = this.startField;
            decreaseLives();
            notifyObservers();
            notifyLogObservers(this);
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void updatePath(Field.Direction dir){
        this.path.add(dir);
    }

    public void setPath(String line){
        for (char c : line.toCharArray()) {
            switch (c) {
                case 'R' -> this.path.add(Field.Direction.R);
                case 'L' -> this.path.add(Field.Direction.L);
                case 'D' -> this.path.add(Field.Direction.D);
                case 'U' -> this.path.add(Field.Direction.U);
            }
        }
    }

    public void moveFromSave() throws GameException {
        for (Field.Direction value : this.path) {
            this.move(value);
        }
    }
}
