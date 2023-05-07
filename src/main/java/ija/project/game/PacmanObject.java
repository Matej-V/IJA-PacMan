package ija.project.game;

import ija.project.common.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class representing the pacman object.
 */
public class PacmanObject extends AbstractObservableObject implements MazeObject {
    /**
     * Current field where Pacman is located.
     */
    private Field field;
    /**
     * Pacman's starting field.
     */
    private final PathField startField;
    /**
     * Number of Pacman Lives.
     */
    private Integer lives;
    /**
     * Pacman's score.
     */
    private Integer score;
    /**
     * The direction of the pacman's movement.
     */
    private Field.Direction direction;
    /**
     * Lock for the pacman object, to ensure that only one thread can access move method at a time.
     */
    ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * Indicator if the pacman has collected the point on the move.
     */
    public boolean pointCollected;
    /**
     * Indicator if the pacman has collected a key on the move.
     */
    public boolean keyCollected;
    /**
     * Current gamemode of a pacman.
     */
    private boolean replayMode;

    /**
     * Constructor for PacmanObject.
     * 
     * @param field Field on which the object is located.
     */
    public PacmanObject(PathField field) {
        this.field = field;
        this.startField = field;
        this.lives = 3;
        this.score = 0;
        this.direction = Field.Direction.U;
    }

    /**
     * Verifies whether it is possible to move a pacman in the specified
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
     * Moves pacman in the specified direction if possible.
     *
     * @param dir Direction in which the object should be moved.
     * @return True if the move was successful, false otherwise.
     */
    @Override
    public boolean move(Field.Direction dir) throws GameException {
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
                if(field.hasKey()){
                    field.getMaze().removeKey(field.getKey());
                    this.keyCollected = true;
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
        notifyLogObservers();
        this.keyCollected = false;
        return true;
    }

    /**
     * Moves the object to the specified field if possible.
     *
     * @param field Field in which the object should be moved.
     * @return True if the move was successful, false otherwise.
     *
     * @throws GameException if game is lost or won.
     */
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
                    this.field.getMaze().removeKey(this.field.getKey());
                }
            }else {
                return false;
            }
        }finally {
            lock.writeLock().unlock();
        }
        notifyLogObservers();
        return true;
    }

    /**
     * Moves the pacman to the start position.
     *
     * @throws GameException if game is lost or won.
     */
    public void moveToStart() throws GameException {
        try {
            lock.writeLock().lock();
            move(startField);
            this.field = this.startField;
            decreaseLives();
            notifyObservers();
            notifyLogObservers();
        }finally {
            lock.writeLock().unlock();
        }
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
     * Returns score in current game.
     * @return Score of the pacman in the game.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the direction of the object.
     * {@link MazeObject#move(Field.Direction)} should be called with this return value of this method.
     *
     * @return Direction of the object.
     */
    @Override
    public Field.Direction getDirection() {
        return this.direction;
    }

    /**
     * Sets the direction of the object in which it should move.
     * Direction should be set before calling {@link MazeObject#move(Field.Direction)}.
     *
     * @param dir Direction in which object should move.
     */
    @Override
    public void setDirection(Field.Direction dir) {
        this.direction = dir;
        /* Notify view when direction is changed to change view of PacMan */
        notifyObservers();
    }

    /**
     * Check if object is PacMan.
     *
     * @return True if object is PacMan, otherwise false.
     */
    @Override
    public boolean isPacman() {
        return true;
    }

    /**
     * Returns start field of a pacman.
     *
     * @return PathField start field.
     */
    public PathField getStartField() {
        return this.startField;
    }

    /**
     * Method to set amount of pacman lives (from save file).
     *
     * @param lives Saved lives.
     */
    public void setLives(int lives){
        this.lives = lives;
        notifyObservers();
    }

    /**
     * Method to set score in current pacman game (from save file).
     *
     * @param score Saved score.
     */
    public void setScore(int score){
        this.score = score;
        notifyObservers();
    }

    /**
     * Updates score of the pacman in the game.
     *
     */
    public void updateScore(){
        this.score++;
        /* Notification for UI view update */
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
     * Sets mode of the pacman to replay.
     *
     */
    public void setReplayMode() {
        this.replayMode = true;
    }

    /**
     * Checks current pacman gamemode.
     *
     * @return true if mode is replay, false otherwise.
     */
    public boolean isReplayMode() {
        return this.replayMode;
    }
}
