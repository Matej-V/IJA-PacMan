package ija.project.common;

import ija.project.game.GameException;

/**
 * Interface representing the maze object.
 */
public interface MazeObject extends Observable {
    /**
     * Verifies whether it is possible to move the object in the specified
     * direction.
     * 
     * @param dir Direction in which the object should be moved.
     * @return True if the move is possible, false otherwise.
     */
    boolean canMove(Field.Direction dir);

    /**
     * Moves the object in the specified direction if possible.
     * 
     * @param dir Direction in which the object should be moved.
     * @return True if the move was successful, false otherwise.
     *
     * @throws GameException if game is lost or won
     */
    boolean move(Field.Direction dir) throws GameException;

    /**
     * Moves the object to the specified field if possible.
     * 
     * @param field Field in which the object should be moved.
     * @return True if the move was successful, false otherwise.
     *
     * @throws GameException if game is lost or won.
     */
    boolean move(Field field) throws GameException;

    /**
     * Move the object to the start field.
     *
     * @throws GameException if game is lost or won.
     */
    void moveToStart() throws GameException;
    
    /**
     * Returns the field on which the object is located.
     * 
     * @return Field on which the object is located.
     */
    Field getField();

    /**
     * Returns the number of lives of the object.
     * 
     * @return Number of lives of the object.
     */
    int getLives();

    /**
     * Returns the score of the object.
     * 
     * @return Score of the object.
     */
    int getScore();

    /**
     * Returns the direction of the object.
     * {@link MazeObject#move(Field.Direction)} should be called with this return value of this method.
     * 
     * @return Direction of the object.
     */
    Field.Direction getDirection();

    /**
     * Sets the direction of the object in which it should move.
     * Direction should be set before calling {@link MazeObject#move(Field.Direction)}.
     * 
     * @param dir Direction in which object should move
     */
    void setDirection(Field.Direction dir);

    /**
     * Returns true if the object is a pacman.
     * 
     * @return True if the object is a pacman, false otherwise.
     */
    default boolean isPacman(){
        return false;
    }
    
}
