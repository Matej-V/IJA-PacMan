package ija.project.pacman_project;

import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogWriter extends PrintWriter implements Observable.Observer {
    /**
     * Lock for synchronization. Only one thread can write to the file at a time.
     */
    private static final Object lock = new Object();

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
    }

    @Override
    public void update(Observable var1) {
        privateUpdate(var1);
    }

    /**
     * Writes the update to the file. This method is synchronized.
     * 
     * @param var1 Observable object that was updated
     */
    private void privateUpdate(Observable var1) {
        synchronized (lock) {
            MazeObject modelO = (MazeObject) var1;
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            String timestamp = formatter.format(now);
            print("# " + timestamp + "\n");
            if (modelO instanceof PacmanObject) {
                print("P " + modelO.getField().getRow() + "/" + modelO.getField().getCol() + " " + modelO.getScore()
                        + " " + modelO.getLives() + (((PacmanObject) modelO).pointCollected ? " p" : "") + "\n");
            } else if (modelO instanceof GhostObject) {
                print("G" + ((GhostObject) modelO).getId() + " " + modelO.getField().getRow() + "/"
                        + modelO.getField().getCol() + " " + ((GhostObject) modelO).isEatable() + "\n");
            } else if (modelO instanceof KeyObject) {
                print("P " + modelO.getField().getRow() + "/" + modelO.getField().getCol() + " k" + "\n");
            }
            flush();
        }
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
        print("# " + timestamp + "\n");
        flush();
    }
}
