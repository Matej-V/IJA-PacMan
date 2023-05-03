package ija.project.game;

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
}
