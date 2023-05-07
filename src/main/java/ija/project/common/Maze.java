package ija.project.common;

import ija.project.game.GameException;

import java.util.List;

/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Interface representing Maze.
 */
public interface Maze {

    /**
     * Associates ghsots with the maze.
     * 
     * @param ghosts ghosts to be associated with the maze
     */
    void setGhosts(List<MazeObject> ghosts);


    /**
     * Associates pacman with the maze.
     * 
     * @param pacman pacman to be associated with the maze
     */
    void setPacMan(MazeObject pacman);

    /**
     * Returns a field at a given position.
     *
     * @param row row of the field
     * @param col column of the field
     * @return Field at the given position
     */
    Field getField(int row, int col);

    /**
     * Returns a list of ghosts in the maze.
     *
     * @return List of ghosts
     */
    List<MazeObject> getGhosts();

    /**
     * Returns pacman in the maze.
     *
     * @return Pacman in the maze
     */
    MazeObject getPacMan();

    /**
     * Returns number of rows in the maze.
     * 
     * @return The number of rows in the maze
     */
    int numRows();

    /**
     * Returns number of columns in the maze.
     * 
     * @return The number of columns in the maze
     */
    int numCols();
    
    /**
     * Moves all objects to their start positions. Calls a method for every object to move to its start field.
     *
     * @throws GameException Exception to handle game scenarios such as completed game or lost game.
     */
    void moveObjectsToStart() throws GameException;

    /**
     * Adds key to the maze.
     *
     * @param key Key to add to the maze
     * @return true if key was added to maze, otherwise false
     */
    boolean addKey(MazeObject key);

    /**
     * Removes key from the maze.
     *
     * @param key Key to remove from the maze
     */
    void removeKey(MazeObject key);

    /**
     * Check if pacman can complete the maze.
     *
     * @return true if all the keys are collected, false otherwise
     */
    boolean canComplete();

    /**
     * Returns target field in the maze
     *
     * @return Field target
     */
    Field getTarget();

    /**
     * Returns the list of collected keys.
     * @return list of keys
     */
    List<MazeObject> getOldKeys();
}
