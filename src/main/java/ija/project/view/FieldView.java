package ija.project.view;

import ija.project.common.Field;
import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Class representing the view of the field. It is an observer of the
 *        field it represents.
 */
public class FieldView extends Pane implements Observable.Observer {
    /**
     * Model of the {@link Field}
     */
    private final Field model;
    /**
     * Represents food on a field
     */
    private Circle Food;
    /**
     * Rectangle representing field
     */
    private Rectangle Border;
    /**
     * List of objects on a field
     */
    private final List<Pane> objects = new ArrayList<>();
    /**
     * Top-left corner x coordinate of a field
     */
    double x;
    /**
     * Top-left corner y coordinate of a field.
     */
    double y;
    /**
     * Width and height of a field
     */
    double size;

    public FieldView(Field model, double cellSize, int row, int col) {
        x = col * cellSize;
        y = row * cellSize;
        size = cellSize;
        this.model = model;
        privateUpdate();
        model.addObserver(this);
    }

    /**
     * Public update method
     */
    @Override
    public void update(Observable var1) {
        privateUpdate();
    }

    /**
     * Generates a border of a field. This border is rectangle with a color
     * depending on the field type.
     */
    public void generateBorder() {
        if (model.canMove()) {
            if (model instanceof TargetField) {
                Border = new Rectangle(x, y, size, size);
                if (model.getMaze().canComplete()) {
                    Border.setFill(Color.web("#84ff9f"));
                } else {
                    Border.setFill(Color.web("#ff8484"));
                }

            } else {
                Border = new Rectangle(x, y, size, size);
                Border.setFill(Color.web("#00022A"));
            }
        } else {
            Border = new Rectangle(x, y, size, size);
            Border.setFill(Color.web("#051D9D"));
            Border.setStroke(Color.WHITE);
        }
    }

    /**
     * Generates and sets visibility of food on a field
     */
    public void generateFood() {
        if (Food == null) {
            Food = new Circle(x + size / 2, y + size / 2, size * 0.1, Color.WHITE);
        }
        Food.setVisible(model.hasPoint() && !(this.model instanceof TargetField));
    }

    /**
     * Updates the view of a field
     * If the field is empty, it clears the view. If the field is not empty, it adds
     * a new Pane to the view. The Pane is either a PacmanObjectView or a
     * GhostObjectView.
     */
    private void privateUpdate() {
        generateBorder();
        generateFood();
        getChildren().setAll(Border, Food);
        if (model.canMove()) {
            objects.clear();
            List<MazeObject> objectsOnField = model.get();
            for (MazeObject o : objectsOnField) {
                if (o instanceof PacmanObject) {
                    objects.add(new PacmanObjectView(this, o));
                } else if (o instanceof GhostObject) {
                    objects.add(new GhostObjectView(this, o));
                } else if (o instanceof KeyObject) {
                    this.Food.setVisible(false);
                    objects.add(new KeyObjectView(this, o));
                }
            }

        }
        getChildren().addAll(objects);
    }
}
