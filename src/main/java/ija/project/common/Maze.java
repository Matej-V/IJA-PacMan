package ija.project.common;

import javafx.scene.Group;

import java.util.List;

public interface Maze {

    public Group getGroup();

    /* TODO */
    public boolean setGhosts(List<MazeObject> ghosts);
    
    /**
     * Returns a field at a given position.
     * 
     * @param row row of the field
     * @param col column of the field
     * @return Field
     */
    Field getField(int row, int col);

    /**
     * Returns number of rows in the maze.
     * 
     * @return int number of rows
     */
    int numRows();

    /**
     * Returns number of columns in the maze.
     * 
     * @return int number of columns
     */
    int numCols();

    /**
     * Returns a list of ghosts in the maze.
     * 
     * @return List of ghosts
     */
    List<MazeObject> ghosts();

    /**
     * Prints a string representation of the maze to stdOut
     * 
     */
    void printMaze();

}
