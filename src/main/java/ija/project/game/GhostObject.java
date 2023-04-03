package ija.project.game;

import ija.project.common.Field;
import ija.project.common.MazeObject;

public class GhostObject implements MazeObject {
    private PathField field;

    /**
     * Constructor for GhostObject.
     *
     * @param field field on which the object is located
     */
    public GhostObject(PathField field) {
        this.field = field;
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
        if (!canMove(dir)) {
            return false;
        } else {
            PathField nextField = (PathField) field.nextField(dir);
            field.remove(this);
            try {
                nextField.put(this);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            field = nextField;
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
}
