package ija.project.game;

import ija.project.common.AbstractObservableObject;
import ija.project.common.Field;
import ija.project.common.MazeObject;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GhostObject extends AbstractObservableObject implements MazeObject {
    private PathField field;
    private final PathField startField;
    private char ID;
    public Color color;
    public List<Color> colors = new ArrayList<Color>(){{
       add(Color.GREENYELLOW);
       add(Color.HONEYDEW);
       add(Color.HOTPINK);
       add(Color.LIGHTBLUE);
       add(Color.LEMONCHIFFON);
    }};
    private Field.Direction direction;
    private List<Field.Direction> path;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * Constructor for GhostObject.
     *
     * @param field field on which the object is located
     */
    public GhostObject(char ID, PathField field) {
        this.ID = ID;
        this.field = field;
        this.startField = field;
        if (this.ID == 'A') {
            this.color = colors.get(0);
        } else if (this.ID == 'B') {
            this.color = colors.get(1);
        } else if (this.ID == 'C') {
            this.color = colors.get(2);
        } else if (this.ID == 'D') {
            this.color = colors.get(3);
        } else if (this.ID == 'E') {
            this.color = colors.get(4);
        }

        this.path = new ArrayList<>();
        this.direction = Field.Direction.values()[new Random().nextInt(Field.Direction.values().length)];
    }

    /**
     * Verifies whether it is possible to move the object in the specified
     * direction.
     *
     * @param dir Direction in which the object should be moved.
     * @return True if the move is possible, false otherwise.
     */
    @Override
    public boolean canMove(Field.Direction dir) {
        Field nextField = field.nextField(dir);
        return nextField instanceof PathField;
    }

    /**
     * Moves the object in the specified direction if possible.
     *
     * @param dir Direction in which the object should be moved.
     * @return True if the move was successful, false otherwise.
     */
    @Override
    public boolean move(Field.Direction dir) {
        try {
            lock.writeLock().lock();
            if (!canMove(dir)) {
                lock.writeLock().unlock();
                return false;
            } else {
                PathField nextField = (PathField) field.nextField(dir);
                this.field.remove(this);
                if (nextField.put(this)) {
                    this.field = nextField;
                }
            }
        }finally {
            lock.writeLock().unlock();
        }
        return true;
    }
    
    /**
     * Returns true if the object is a pacman.
     *
     * @return True if the object is a pacman, false otherwise.
     */
    @Override
    public boolean isPacman() {
        return false;
    }


    /**
     * Returns the field on which the object is located.
     *
     * @return Field on which the object is located.
     */
    public Field getField() {
        return this.field;
    }

    /* Return 0 or error? TODO */
    public int getLives() {
        return 0;
    }

    /* Same question as above for getLives() method */
    public int getScore() {
        return 0;
    }

    @Override
    public Field.Direction getDirection() {
        return this.direction;
    }

    @Override
    public void setDirection(Field.Direction dir) {
        notifyObservers();
        this.direction = dir;
    }

    /**
     * Removes object from a current <code>field</code> and moves it to a starting field <code>startField</code>
     */
    @Override
    public void moveToStart() {
        try {
            lock.writeLock().lock();
            this.field.remove(this);
            this.startField.put(this);
            this.field = this.startField;
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void moveFromSave() {
        ListIterator<Field.Direction> it = this.path.listIterator();
        while (it.hasNext()){
            this.move(it.next());
        }
    }

    public void setPath(String line){
        for (char c : line.toCharArray()) {
            switch (c) {
                case 'R' -> this.path.add(Field.Direction.R);
                case 'L' -> this.path.add(Field.Direction.L);
                case 'D' -> this.path.add(Field.Direction.D);
                case 'U' -> this.path.add(Field.Direction.U);
            }
        }
    }

    public char getId() {
        return this.ID;
    }
}
