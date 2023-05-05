package ija.project.pacman_project;

import ija.project.common.Maze;
import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogWriter extends PrintWriter implements Observable.Observer {
    private static final Object lock = new Object();

    public LogWriter(File file, Maze maze) throws FileNotFoundException {
        super(file);
        synchronized (lock) {
            printMaze(maze);
        }
        
        for(int row = 0; row < maze.numRows(); row++){
            for(int column = 0; column < maze.numCols(); column++){
                maze.getField(row, column).addLogObserver(this);
            }
        }
    }

    @Override
    public void update(Observable var1) {
        synchronized (lock) {
            privateUpdate(var1);
        }
    }

    private void privateUpdate(Observable var1){
        MazeObject modelO = (MazeObject)var1;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String timestamp = formatter.format(now);
        print("# " + timestamp + "\n");
        if(modelO instanceof PacmanObject){
            print("P " + modelO.getField().getRow() + "/" + modelO.getField().getCol() + (((PacmanObject)modelO).pointCollected ? " p": "") + "\n" );
        }else if(modelO instanceof GhostObject){
            print("G" + ((GhostObject) modelO).getId() + " " + modelO.getField().getRow() + "/" + modelO.getField().getCol() + "\n");
        }else if (modelO instanceof KeyObject) {
            print("P " + modelO.getField().getRow() + "/" + modelO.getField().getCol() + " k" + "\n");
        }
        flush();
    }

    private void printMaze(Maze maze){
        println((maze.numRows() - 2) + " "+ (maze.numCols() - 2) + " " + maze.ghosts().size());
        for (int row = 1; row < maze.numRows() - 1; row++) {
            for (int column = 1; column < maze.numCols() - 1 ; column++) {
                if ( maze.getField(row, column) instanceof WallField) {
                    print('X');
                }else if (maze.getField(row, column) instanceof TargetField) {
                    print('T');
                }else if (maze.getField(row, column) instanceof PathField) {
                    if (maze.getField(row, column).get().size() == 0) {
                        print('.');
                    }else{
                        for (MazeObject o: maze.getField(row, column).get()) {
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
