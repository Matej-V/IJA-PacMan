package ija.project.game;

import ija.project.common.*;

/**
 * Class representing the pacman object.
 */
public class PacmanObject implements MazeObject {
    private PathField field;
    private Integer lives;

    /**
     * Constructor for PacmanObject.
     * 
     * @param field field on which the object is located
     */
    public PacmanObject(PathField field) {
        this.field = field;
        this.lives = 3;
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
     * Returns the number of lives of the object.
     * 
     * @return Number of lives of the object.
     */
    @Override
    public boolean isPacman(){
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
        if (!canMove(dir)) {
            return false;
        } else {
            PathField nextField = (PathField) field.nextField(dir);
            field.remove(this);
            try {
                nextField.put(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            field = nextField;
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
     * Decreases the number of lives of the pacman.
     * 
     */
    public void decreaseLives(){
        this.lives--;
    }


}
