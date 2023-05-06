package ija.project.common;

import ija.project.game.GameException;

import java.util.List;

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
     * @return Field
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
     * @return pacman
     */
    MazeObject getPacMan();

    /**
     * Returns number of rows in the maze.
     * 
     * @return number of rows
     */
    int numRows();

    /**
     * Returns number of columns in the maze.
     * 
     * @return number of columns
     */
    int numCols();
    
    /**
     * Moves all objects to their start positions. Calls a method for every object to move to its start field.
     * @throws GameException
     */
    void moveObjectsToStart() throws GameException;

    /**
     * Add key to the maze.
     */
    boolean addKey(MazeObject key);

    /**
     * Collect key from the maze.
     */
    void removeKey(MazeObject key);

    /**
     * Check if pacman can complete the maze.
     * @return true if all the keys are collected, false otherwise
     */
    boolean canComplete();
}
