package ija.project.game;

import ija.project.common.Field;
import ija.project.common.MazeObject;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class representing ghost object. Ghosts are moving on the field and can be eaten by pacman.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class GhostObject extends AbstractObservable implements MazeObject {
    /**
     * Current field in which the ghost is located.
     */
    private Field field;
    /**
     * Ghost starting field
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
    /**
     * Lock for the ghost object, to ensure that only one thread can access move
     * method at a time.
     */
    ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * List of colors for ghosts. If there are more ghosts than colors, the colors
     * are reused.
     */
    public static List<Color> colors = new ArrayList<>() {
        {
            add(Color.GREENYELLOW);
            add(Color.HONEYDEW);
            add(Color.HOTPINK);
            add(Color.LIGHTBLUE);
            add(Color.LEMONCHIFFON);
        }
    };

    /**
     * Constructor for GhostObject.
     *
     * @param field field on which the object is located
     * @param id    ID of the ghost
     */
    public GhostObject(PathField field, int id) {
        this.id = id;
        this.field = field;
        this.startField = field;
        this.color = colors.get(this.id % colors.size());
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
        } finally {
            lock.writeLock().unlock();
        }
        notifyLogObservers();
        return true;
    }

    /**
     * Moves the object to the specified field if possible.
     *
     * @param field Field to which the object should be moved.
     * @return True if the move was successful, false otherwise.
     * @throws GameException if game is lost or won
     */
    @Override
    public boolean move(Field field) throws GameException {
        try {
            lock.writeLock().lock();
            if (field.canMove()) {
                this.field.remove(this);
                if (((PathField) field).put(this)) {
                    this.field = field;
                }
            } else {
                return false;
            }
        } finally {
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
            notifyLogObservers();
        } finally {
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
     * Returns the direction of the ghost. {@link GhostObject#move(Field.Direction)}
     * should be called with this return value of this method.
     * 
     * @return Direction of the object.
     */
    @Override
    public Field.Direction getDirection() {
        return direction;
    }

    /**
     * Sets the direction of the ghost in which it should move. Direction should be
     * set before calling {@link GhostObject#move(Field.Direction)}.
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
     * Returns start field of a pacman.
     *
     * @return PathField start field.
     */
    public PathField getStartField() {
        return this.startField;
    }

    /**
     * Change the eatable state of the ghost.
     * 
     * @param eatable New eatable state of the ghost.
     */
    public void setEatable(boolean eatable) {
        isEatable = eatable;
    }

    /**
     * Returns the eatable state of the ghost.
     * 
     * @return True if ghost is eatable, false otherwise.
     */
    public boolean isEatable() {
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
     *
     * @return Color of the ghost.
     */
    public Color getColor() {
        return color;
    }
}
