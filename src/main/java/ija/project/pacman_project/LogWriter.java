package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Class for logging the game. Writes maze to the file when created and pacman and it logs the state of the objects when notified.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class LogWriter extends PrintWriter implements Observable.Observer {
    /**
     * Lock for synchronization. Only one thread can write to the file at a time.
     */
    private static final Object lock = new Object();
    /**
     * Last seen pacman state.
     */
    private PacmanObject lastPacmanState;
    /**
     * Last seen states of all ghosts in maze.
     */
    private final List<MazeObject> lastGhostsState = new ArrayList<>();

    /**
     * Creates a new LogWriter object. The file is created if it does not exist.
     * 
     * @param file File to be written to.
     * @param maze Maze to be logged.
     */
    public LogWriter(File file, Maze maze) throws FileNotFoundException {
        super(file);
        synchronized (lock) {
            printMaze(maze);
        }
        maze.getPacMan().addLogObserver(this);
        for (MazeObject o : maze.getGhosts()) {
            o.addLogObserver(this);
        }
        for(int row = 0; row < maze.numRows(); row++){
            for(int col = 0; col < maze.numCols(); col++){
                maze.getField(row, col).addLogObserver(this);
            }
        }
    }

    @Override
    public void update(Observable var1) {
        privateUpdate(var1);
    }

    /**
     * Writes the update to the file. This method is synchronized.
     * Pacman log: P row/col score lives availableBombs [p] [k]
     * Ghost log: G<id> row/col isEatable
     * Field log: F row/col - indicates that the field was swapped
     * @param var1 Observable object that was updated
     */
    private void privateUpdate(Observable var1) {
        synchronized (lock) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            String timestamp = formatter.format(now);
            print("# " + timestamp + "\n");
            if (var1 instanceof MazeObject modelObject){
                if (modelObject instanceof PacmanObject) {
                    PacmanObject pacman = ((PacmanObject) modelObject);
                    print("P " + modelObject.getField().getRow() + "/" + modelObject.getField().getCol() + " " + modelObject.getScore()
                            + " " + modelObject.getLives() + " " + pacman.getAvailableBombs() + (pacman.pointCollected ? " p" : "")
                            + (pacman.keyCollected ? " k" : "") + "\n");
                    lastPacmanState = (PacmanObject) modelObject;
                } else if (modelObject instanceof GhostObject) {
                    print("G" + ((GhostObject) modelObject).getId() + " " + modelObject.getField().getRow() + "/"
                            + modelObject.getField().getCol() + " " + ((GhostObject) modelObject).isEatable() + "\n");
                    updateGhostState((GhostObject) modelObject);
                } else if( modelObject instanceof BombObject){
                    print("B " + modelObject.getField().getRow() + "/" + modelObject.getField().getCol() + " " + ((BombObject) modelObject).getTimeToDetonation() + "\n");
                }
            }else if(var1 instanceof Field modelField){
                print("F " + modelField.getRow() + "/" + modelField.getCol() + "\n");
            }
            flush();
        }
    }

    /**
     * Method to update last seen state of a given ghost
     *
     * @param gh A ghost whose state must be updated
     */
    private void updateGhostState(GhostObject gh) {
        ListIterator<MazeObject> it = this.lastGhostsState.listIterator();
        while (it.hasNext()) {
            GhostObject ghost = (GhostObject) it.next();
            if (ghost.getId() == gh.getId()) {
                it.set(gh);
                return;
            }
        }
        // If there's no ghost in list yet
        this.lastGhostsState.add(gh);
    }

    /**
     * Returns last seen state of a pacman
     *
     * @return PacmanObject Last seen state of a pacman
     */
    public PacmanObject getLastPacmanState() {
        return this.lastPacmanState;
    }

    /**
     * Returns a list of last ghosts states
     *
     * @return List of MazeObjects
     */
    public List<MazeObject> getLastGhostsState() {
        return this.lastGhostsState;
    }

    /**
     * Prints the maze representation to the file.
     * 
     * @param maze Maze to be printed.
     */
    private void printMaze(Maze maze) {
        println((maze.numRows() - 2) + " " + (maze.numCols() - 2) + " " + maze.getGhosts().size());
        for (int row = 1; row < maze.numRows() - 1; row++) {
            for (int column = 1; column < maze.numCols() - 1; column++) {
                if (maze.getField(row, column) instanceof WallField) {
                    print('X');
                } else if (maze.getField(row, column) instanceof TargetField) {
                    print('T');
                } else if (maze.getField(row, column) instanceof PathField) {
                    if (maze.getField(row, column).get().size() == 0) {
                        print('.');
                    } else {
                        for (MazeObject o : maze.getField(row, column).get()) {
                            if (o instanceof PacmanObject) {
                                print('S');
                            } else if (o instanceof GhostObject) {
                                print('G');
                            } else if (o instanceof KeyObject) {
                                print('K');
                            }
                        }
                    }
                }
            }
            println();
        }
        println();
        println("--- LOG");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String timestamp = formatter.format(now);
        // Saving start positions of objects in the maze
        print("# " + timestamp + "\n");
        print("P " + maze.getPacMan().getStartField().getRow() + "/" + maze.getPacMan().getStartField().getCol()
                + " 0 3 3\n");
        for (MazeObject gh : maze.getGhosts()) {
            print("# " + timestamp + "\n");
            print("G" + ((GhostObject) gh).getId() + " " + gh.getStartField().getRow() + "/"
                    + gh.getStartField().getCol() + " false\n");
        }
        flush();
    }
}
