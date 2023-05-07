package ija.project.game;

/**
 * Class representing target field. Target field is subclass of PathField.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class TargetField extends PathField {

    /**
     * Constructor.
     *
     * @param row row of the field
     * @param col column of the field
     */
    public TargetField(int row, int col) {
        super(row, col);
        point = false;
    }

    public void setOpen() {
        notifyObservers();
    }

    public void setClosed() {
        notifyObservers();
    }
}
