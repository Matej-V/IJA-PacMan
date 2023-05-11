package ija.project.game;

import ija.project.common.Field;
import ija.project.common.MazeObject;

/**
 * Class representing the bomb in the maze.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class BombObject extends AbstractObservableObject implements MazeObject {
    /**
     * Field to which the object belongs to
     */
    Field field;


    int timeToDetonation;

    public BombObject(Field field, Observer logWriter){
        this.field = field;
        addLogObserver(logWriter);
        setTimer(3);
    }
    @Override
    public boolean canMove(Field.Direction dir) {
        return false;
    }

    @Override
    public boolean move(Field.Direction dir) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean move(Field field) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void moveToStart() {

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
    public PathField getStartField() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the time to detonation. This time is in seconds. Set when the bomb is created to 3 seconds.
     * @return time to detonation
     */
    public int getTimeToDetonation(){
        return timeToDetonation;
    }

    /**
     * Sets the time to detonation. This time is in seconds.
     * @param newValue new time to detonation
     */
    public void setTimer(int newValue){
        this.timeToDetonation = newValue;
        notifyObservers();
        notifyLogObservers();
    }
}
