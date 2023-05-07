package ija.project.game;


/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Class representing target field. Target field is subclass of PathField.
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
}
