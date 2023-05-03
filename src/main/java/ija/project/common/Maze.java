package ija.project.common;

import javafx.scene.Group;

import java.util.List;

public interface Maze {

    Group getGroup();

    /* TODO */
    void setGhosts(List<MazeObject> ghosts);
    
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

    void setPacMan(MazeObject pacMan);

    MazeObject getPacMan();

    /**
     * Prints a string representation of the maze to stdOut
     * 
     */
    void printMaze();

    void moveObjectsToStart();

    void registerGhostPath(char id, String line);
}
