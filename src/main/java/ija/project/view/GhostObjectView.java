package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.GhostObject;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class representing the view of the ghost. It is an observer of the ghost it represents. It is a child of {@link FieldView}.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class GhostObjectView extends Pane implements Observable.Observer {
    /**
     * Model of the {@link GhostObject}
     */
    private final GhostObject model;
    /**
     * Parent {@link FieldView} of this object
     */
    private final FieldView parent;
    /**
     * Circle representing ghost
     */
    private final Circle ghost;

    public GhostObjectView(FieldView parent, MazeObject model) {
        this.parent = parent;
        this.model = (GhostObject) model;
        this.ghost = new Circle(parent.size / 2, parent.size / 2, parent.size * 0.35,
                this.model.getColor());
        getChildren().add(ghost);
        paint();
        model.addObserver(this);
    }

    /**
     * Paints a ghost as a circle and adds it to own Observable list
     */
    private void paint() {
        if (model.isEatable()) {
            ghost.setFill(Color.web("#051D9D"));
        }
        // create eyes of ghost
        Circle eye1 = new Circle(parent.size / 2 - parent.size * 0.1,
                parent.size / 2 - parent.size * 0.1, parent.size * 0.1, Color.WHITE);
        Circle eye2 = new Circle(parent.size / 2 + parent.size * 0.1,
                parent.size / 2 - parent.size * 0.1, parent.size * 0.1, Color.WHITE);
        Circle eye1pupil = new Circle(parent.size / 2 - parent.size * 0.1,
                parent.size / 2 - parent.size * 0.1, parent.size * 0.05, Color.BLACK);
        Circle eye2pupil = new Circle(parent.size / 2 + parent.size * 0.1,
                parent.size / 2 - parent.size * 0.1, parent.size * 0.05, Color.BLACK);
        switch (model.getDirection()) {
            case U -> {
                eye1.setCenterX(parent.size / 2 - parent.size * 0.1);
                eye1.setCenterY(parent.size / 2 - parent.size * 0.1 - parent.size * 0.1);
                eye2.setCenterX(parent.size / 2 + parent.size * 0.1);
                eye2.setCenterY(parent.size / 2 - parent.size * 0.1 - parent.size * 0.1);
                eye1pupil.setCenterX(parent.size / 2 - parent.size * 0.1);
                eye1pupil.setCenterY(parent.size / 2 - parent.size * 0.1 - parent.size * 0.1);
                eye2pupil.setCenterX(parent.size / 2 + parent.size * 0.1);
                eye2pupil.setCenterY(parent.size / 2 - parent.size * 0.1 - parent.size * 0.1);
            }
            case D -> {
                eye1.setCenterX(parent.size / 2 - parent.size * 0.1);
                eye1.setCenterY(parent.size / 2 - parent.size * 0.1 + parent.size * 0.1);
                eye2.setCenterX(parent.size / 2 + parent.size * 0.1);
                eye2.setCenterY(parent.size / 2 - parent.size * 0.1 + parent.size * 0.1);
                eye1pupil.setCenterX(parent.size / 2 - parent.size * 0.1);
                eye1pupil.setCenterY(parent.size / 2 - parent.size * 0.1 + parent.size * 0.1);
                eye2pupil.setCenterX(parent.size / 2 + parent.size * 0.1);
                eye2pupil.setCenterY(parent.size / 2 - parent.size * 0.1 + parent.size * 0.1);
            }
            case L -> {
                eye1.setCenterX(parent.size / 2 - parent.size * 0.1 - parent.size * 0.1);
                eye1.setCenterY(parent.size / 2 - parent.size * 0.1);
                eye2.setCenterX(parent.size / 2 + parent.size * 0.1 - parent.size * 0.1);
                eye2.setCenterY(parent.size / 2 - parent.size * 0.1);
                eye1pupil.setCenterX(parent.size / 2 - parent.size * 0.1 - parent.size * 0.1);
                eye1pupil.setCenterY(parent.size / 2 - parent.size * 0.1);
                eye2pupil.setCenterX(parent.size / 2 + parent.size * 0.1 - parent.size * 0.1);
                eye2pupil.setCenterY(parent.size / 2 - parent.size * 0.1);
            }
            case R -> {
                eye1.setCenterX(parent.size / 2 - parent.size * 0.1 + parent.size * 0.1);
                eye1.setCenterY(parent.size / 2 - parent.size * 0.1);
                eye2.setCenterX(parent.size / 2 + parent.size * 0.1 + parent.size * 0.1);
                eye2.setCenterY(parent.size / 2 - parent.size * 0.1);
                eye1pupil.setCenterX(parent.size / 2 - parent.size * 0.1 + parent.size * 0.1);
                eye1pupil.setCenterY(parent.size / 2 - parent.size * 0.1);
                eye2pupil.setCenterX(parent.size / 2 + parent.size * 0.1 + parent.size * 0.1);
                eye2pupil.setCenterY(parent.size / 2 - parent.size * 0.1);
            }
        }
        // add eyes to ghost
        getChildren().add(eye1);
        getChildren().add(eye2);
        getChildren().add(eye1pupil);
        getChildren().add(eye2pupil);
    }

    /**
     * Updates the view when notified by the model.
     */
    @Override
    public void update(Observable var1) {
        paint();
    }
}
