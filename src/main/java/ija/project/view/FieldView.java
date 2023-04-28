package ija.project.view;

import ija.project.common.Field;
import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.PathField;
import ija.project.game.TargetField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class FieldView extends Pane implements Observable.Observer {
    private final Field model;
    private Circle Food;
    private Rectangle Border;
    private final List<Pane> objects = new ArrayList<>();
    double x;
    double y;
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
            if (model instanceof PathField){
                Border = new Rectangle(x, y, size, size);
                Border.setFill(Color.web("#00022A"));
            } else if (model instanceof TargetField) {
                Border = new Rectangle(x, y, size, size);
                Border.setFill(Color.web("#ff8484"));
            }
        } else{
            Border = new Rectangle(x, y, size, size);
            Border.setFill(Color.web("#051D9D"));
            Border.setStroke(Color.WHITE);
        }
    }

    /**
     * Generates and set visibility of food on a field
     */
    public void generateFood() {
        if (Food == null) {
            Food = new Circle(x + size / 2, y + size / 2, size * 0.1, Color.WHITE);
        }
        Food.setVisible(model.hasPoint());
    }

    /**
     * Updates the view of a field
     * If the field is empty, it clears the view. If the field is not empty, it adds
     * a new Pane to the view. The Pane is either a PacmanObjectView or a
     * GhostObjectView.
     */
    public void privateUpdate() {
        generateBorder();
        generateFood();
        if (model.canMove()) {
            if (!model.isEmpty()) {
                MazeObject o = model.get();
                Pane v = o.isPacman() ? new PacmanObjectView(this, model.get())
                        : new GhostObjectView(this, model.get());
                objects.add(v);
            } else {
                objects.clear();
            }
        }
        getChildren().setAll(Border, Food);
        getChildren().addAll(objects);
    }
}
