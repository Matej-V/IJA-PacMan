package ija.project.common;

public interface Field {

    /*
     * Direction of the field.
     */
    enum Direction {
        D, // down
        L, // left
        R, // right
        U // up
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
     * @return Field
     */
    Field nextField(Direction dirs);

    /**
     * Returns row of the field.
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
     * Returns the object on the field.
     * 
     * @return object on the field
     */
    MazeObject get();

    /**
     * Checks whether the field is path field.
     * 
     * @return True if the field is path field, false otherwise
     */
    boolean canMove();

    boolean contains(MazeObject object);
}
