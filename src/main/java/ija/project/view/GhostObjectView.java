package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.GhostObject;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;


public class GhostObjectView extends Pane implements ComponentView, Observable.Observer {
    private GhostObject model;
    private FieldView parent;

    public GhostObjectView(FieldView parent, MazeObject model){
        this.parent = parent;
        this.model = (GhostObject)  model;
        paint();
        model.addObserver(this);
    }

    /**
     * Paints a ghost as a <code>Circle</code> and adds it to self.
     */
    public void paint() {
        Circle ghost = new Circle(parent.x + parent.size/2, parent.y + parent.size/2,parent.size * 0.35, model.color);
        getChildren().add(ghost);
    }

    @Override
    public void update(Observable var1) {
        paint();
    }
}
