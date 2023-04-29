package ija.project.common;

/**
 * Interface representing the maze object.
 */
public interface MazeObject extends Observable{
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
     */
    boolean move(Field.Direction dir);

    /**
     * Returns true if the object is a pacman.
     * 
     * @return True if the object is a pacman, false otherwise.
     */
    default boolean isPacman(){
        return false;
    }
    

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

    int getScore();

    Field.Direction getDirection();

    void setDirection(Field.Direction dir);

    void moveToStart();

}
