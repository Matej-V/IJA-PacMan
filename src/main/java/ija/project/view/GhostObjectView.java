package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.GhostObject;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class GhostObjectView extends Pane implements Observable.Observer {
    private GhostObject model;
    private FieldView parent;
    private Circle ghost;

    public GhostObjectView(FieldView parent, MazeObject model){
        this.parent = parent;
        this.model = (GhostObject)  model;
        this. ghost = new Circle(parent.x + parent.size/2, parent.y + parent.size/2,parent.size * 0.35, this.model.getColor());
        getChildren().add(ghost);
        paint();
        model.addObserver(this);
    }

    /**
     * Paints a ghost as a <code>Circle</code> and adds it to self.
     */
    private void paint() {
        if (model.isEatable()){
            ghost.setFill(Color.web("#051D9D"));
        }
        // create eyes of ghost
        Circle eye1 = new Circle(parent.x + parent.size/2 - parent.size * 0.1, parent.y + parent.size/2 - parent.size * 0.1, parent.size * 0.1, Color.WHITE);
        Circle eye2 = new Circle(parent.x + parent.size/2 + parent.size * 0.1, parent.y + parent.size/2 - parent.size * 0.1, parent.size * 0.1, Color.WHITE);
        Circle eye1pupil = new Circle(parent.x + parent.size/2 - parent.size * 0.1, parent.y + parent.size/2 - parent.size * 0.1, parent.size * 0.05, Color.BLACK);
        Circle eye2pupil = new Circle(parent.x + parent.size/2 + parent.size * 0.1, parent.y + parent.size/2 - parent.size * 0.1, parent.size * 0.05, Color.BLACK);
        switch(model.getDirection()){
            case U:
                eye1.setCenterX(parent.x + parent.size/2 - parent.size * 0.1);
                eye1.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 - parent.size * 0.1);
                eye2.setCenterX(parent.x + parent.size/2 + parent.size * 0.1);
                eye2.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 - parent.size * 0.1);
                eye1pupil.setCenterX(parent.x + parent.size/2 - parent.size * 0.1);
                eye1pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 - parent.size * 0.1);
                eye2pupil.setCenterX(parent.x + parent.size/2 + parent.size * 0.1);
                eye2pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 - parent.size * 0.1);
                break;
            case D:
                eye1.setCenterX(parent.x + parent.size/2 - parent.size * 0.1);
                eye1.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 + parent.size * 0.1);
                eye2.setCenterX(parent.x + parent.size/2 + parent.size * 0.1);
                eye2.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 + parent.size * 0.1);
                eye1pupil.setCenterX(parent.x + parent.size/2 - parent.size * 0.1);
                eye1pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 + parent.size * 0.1);
                eye2pupil.setCenterX(parent.x + parent.size/2 + parent.size * 0.1);
                eye2pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1 + parent.size * 0.1);
                break;
            case L:
                eye1.setCenterX(parent.x + parent.size/2 - parent.size * 0.1 - parent.size * 0.1);
                eye1.setCenterY(parent.y + parent.size/2 - parent.size* 0.1);
                eye2.setCenterX(parent.x + parent.size/2 + parent.size * 0.1 - parent.size * 0.1);
                eye2.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                eye1pupil.setCenterX(parent.x + parent.size/2 - parent.size * 0.1 - parent.size * 0.1);
                eye1pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                eye2pupil.setCenterX(parent.x + parent.size/2 + parent.size * 0.1 - parent.size * 0.1);
                eye2pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                break;
            case R:
                eye1.setCenterX(parent.x + parent.size/2 - parent.size * 0.1 + parent.size * 0.1);
                eye1.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                eye2.setCenterX(parent.x + parent.size/2 + parent.size * 0.1 + parent.size * 0.1);
                eye2.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                eye1pupil.setCenterX(parent.x + parent.size/2 - parent.size * 0.1 + parent.size * 0.1);
                eye1pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                eye2pupil.setCenterX(parent.x + parent.size/2 + parent.size * 0.1 + parent.size * 0.1);
                eye2pupil.setCenterY(parent.y + parent.size/2 - parent.size * 0.1);
                break;
        }
        // add eyes to ghost
        getChildren().add(eye1);
        getChildren().add(eye2);
        getChildren().add(eye1pupil);
        getChildren().add(eye2pupil);
    }

    @Override
    public void update(Observable var1) {
        paint();
    }
}
