package ija.project.game;

import ija.project.common.AbstractObservableObject;
import ija.project.common.Field;
import ija.project.common.MazeObject;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GhostObject extends AbstractObservableObject implements MazeObject {
    /**
     * Current field on which the ghost is located.
     */
    private Field field;
    /**
     * Starting field of the ghost.
     */
    private final PathField startField;
    /**
     * ID of the ghost.
     */
    private final int id;
    /**
     * Color of the ghost.
     */
    private final Color color;
    /**
     * Direction in which the ghost is moving.
     */
    private Field.Direction direction;
    /**
     * Boolean value indicating whether the ghost is eatable.
     */
    private boolean isEatable = false;
    // TODO
    private final List<Field.Direction> path;
    /**
     * Lock for the ghost object, to ensure that only one thread can access move method at a time.
     */
    ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * List of colors for ghosts. If there are more ghosts than colors, the colors are reused. 
     */
    public static List<Color> colors = new ArrayList<>(){{
        add(Color.GREENYELLOW);
        add(Color.HONEYDEW);
        add(Color.HOTPINK);
        add(Color.LIGHTBLUE);
        add(Color.LEMONCHIFFON);
     }};

    /**
     * Constructor for GhostObject.
     *
     * @param field field on which the object is located
     * @param id ID of the ghost
     */
    public GhostObject(PathField field, int id) {
        this.id = id;
        this.field = field;
        this.startField = field;
        this.color = colors.get(this.id % colors.size());
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
     *
     * @throws GameException if game is lost or won
     */
    @Override
    public boolean move(Field.Direction dir) throws GameException {
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
        notifyLogObservers(this);
        return true;
    }

    /**
     * Moves the object to the specified field if possible.
     *
     * @param field Field to which the object should be moved.
     * @return True if the move was successful, false otherwise.
     *
     * @throws GameException if game is lost or won
     */
    @Override
    public boolean move(Field field) throws GameException {
        try {
            lock.writeLock().lock();
            if(field.canMove()){
                this.field.remove(this);
                if (((PathField)field).put(this)) {
                    this.field = field;
                }
            }else{
                return false;
            }
        }finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    /**
     * Moves the object to the start field. Used when the ghost is eaten by pacman.
     */
    @Override
    public void moveToStart() throws GameException {
        try {
            lock.writeLock().lock();
            this.field.remove(this);
            this.startField.put(this);
            this.field = this.startField;
            notifyObservers();
            notifyLogObservers(this);
        }finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Returns the field on which the object is located.
     *
     * @return Field on which the object is located.
     */
    public Field getField() {
        return field;
    }

    /**
     * Throws UnsupportedOperationException because ghosts do not have lives.
     */
    @Override
    public int getLives() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    /**
     * Throws UnsupportedOperationException because ghosts do not have score points.
     */
    @Override
    public int getScore() {
        throw new UnsupportedOperationException("Unsupported operation.");
    }


    /**
     * Returns the direction of the ghost. {@link GhostObject#move(Field.Direction)} should be called with this return value of this method.
     * 
     * @return Direction of the object.
     */
    @Override
    public Field.Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the ghost in which it should move. Direction should be set before calling {@link GhostObject#move(Field.Direction)}.
     * 
     * @param dir Direction in which object should move
     */
    @Override
    public void setDirection(Field.Direction dir) {
        direction = dir;
        notifyObservers();
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
     * Change the eatable state of the ghost.
     * 
     * @param eatable New eatable state of the ghost.
     */
    public void setEatable(boolean eatable){
        isEatable = eatable;
    }

    /**
     * Returns the eatable state of the ghost.
     * 
     * @return Eatable state of the ghost.
     */
    public boolean isEatable(){
        return isEatable;
    }

    /**
     * Returns the ID of the ghost.
     * 
     * @return ID of the ghost.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Returns the color of the ghost.
     */
    public Color getColor() {
        return color;
    }

    
    // TODO
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

    
}
