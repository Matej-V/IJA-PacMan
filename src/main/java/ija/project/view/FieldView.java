package ija.project.view;

import ija.project.common.Field;
import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.*;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the view of the field. It is an observer of the field it represents.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class FieldView extends Pane implements Observable.Observer {
    /**
     * Model of the {@link Field}
     */
    private Field model;
    /**
     * Represents food on a field
     */
    private Circle Food;
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
        setMinWidth(cellSize);
        setMinHeight(cellSize);
        setLayoutY(0);
        setLayoutX(0);
        setTranslateX(x);
        setTranslateY(y);
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
    public void generateFieldBackground() {
        setBorder(null);
        if (model.canMove()) {
            if (model instanceof TargetField) {
                if (model.getMaze().canComplete()) {
                    setBackground(new Background(new BackgroundFill(Color.web("#84ff9f"), CornerRadii.EMPTY, Insets.EMPTY)));
                } else {
                    setBackground(new Background(new BackgroundFill(Color.web("#ff8484"), CornerRadii.EMPTY, Insets.EMPTY)));
                }

            } else {
                setBackground(new Background(new BackgroundFill(Color.web("#00022A"), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        } else {
            setBackground(new Background(new BackgroundFill(Color.web("#051D9D"), CornerRadii.EMPTY, Insets.EMPTY)));
            setBorder(new Border(new BorderStroke(Color.WHITE,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    /**
     * Generates and sets visibility of food on a field
     */
    public void generateFood() {
        if (Food == null) {
            Food = new Circle(size / 2, size / 2, size * 0.1, Color.WHITE);
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
        generateFieldBackground();
        generateFood();
        getChildren().setAll(Food);
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
                }else if(o instanceof BombObject){
                    objects.add(new BombObjectView(this, o));
                }
            }

        }
        getChildren().addAll(objects);
    }

    public Field getModel(){
        return this.model;
    }

    public void setModel(Field newModel){
        this.model = newModel;
        privateUpdate();
    }

}
