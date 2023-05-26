package ija.project.common;

import ija.project.game.KeyObject;
import java.util.List;

/**
 * Interface representing the field object.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public interface Field extends Observable {

    /**
     * Direction of the field.
     */
    enum Direction {
        D, // down
        L, // left
        R, // right
        U // up
        ;

        /**
         * Returns the opposite direction.
         * 
         * @param dir direction
         * @return opposite direction
         */
        public Direction opposite(Direction dir) {
            if (dir == D) {
                return U;
            } else if (dir == U) {
                return D;
            } else if (dir == L) {
                return R;
            } else if (dir == R) {
                return L;
            } else {
                return null;
            }
        }
    }

    /**
     * Associates the field with the maze.
     * 
     * @param maze maze to which the field belongs
     */
    void setMaze(Maze maze);

    /**
     * Returns neighboring field in the given direction.
     * 
     * @param dirs direction
     * @return Field neighboring field in the given direction.
     */
    Field nextField(Direction dirs);

    /**
     * Puts the object on the field.
     * 
     * @param object object to be put on the field
     * @return row of the field
     * @throws Exception if the field is WallField exception
     *                   UnsupportedOperationException is thrown
     */
    boolean put(MazeObject object) throws Exception;

    /**
     * Removes the object from the field.
     * 
     * @param object object to be removed from the field
     */
    boolean remove(MazeObject object);

    /**
     * Checks whether the field is empty.
     * 
     * @return True if the field is empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the list of objects on the field.
     * 
     * @return list of objects on the field
     */
    List<MazeObject> get();

    /**
     * Checks whether the field is path field.
     * 
     * @return True if the field is path field, false otherwise
     */
    boolean canMove();

    /**
     * Returns the maze to which the field belongs.
     * 
     * @return maze to which the field belongs
     */
    Maze getMaze();

    /**
     * Returns the row of the field.
     * 
     * @return row of the field
     */
    int getRow();

    /**
     * Returns the column of the field.
     * 
     * @return column of the field
     */
    int getCol();

    /**
     * Check if the field has point.
     *
     * @return True if the field has point, false otherwise
     */
    boolean hasPoint();

    /**
     * Check if the field has key.
     *
     * @return True if the field has key, false otherwise
     */
    boolean hasKey();

    /**
     * Get the key from the field.
     *
     * @return KeyObject
     */
    KeyObject getKey();

}
