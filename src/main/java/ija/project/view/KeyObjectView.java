package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.KeyObject;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class KeyObjectView extends Pane implements Observable.Observer {
    /**
     * Rectangle representing key
     */
    Rectangle key;
    /**
     * Model of the {@link MazeObject}
     */
    MazeObject model;
    /**
     * Parent {@link FieldView} of this object
     */
    FieldView parent;

    public KeyObjectView(FieldView parent, MazeObject model){
        this.parent = parent;
        this.model = model;
        key = new Rectangle(parent.x + parent.size * 0.3, parent.y + parent.size * 0.3, parent.size * 0.4, parent.size * 0.4);
        key.setFill(Color.YELLOW);
        getChildren().add(key);
        paint();
        model.addObserver(this);
    }
    /**
     * Paints a key as a rectangle and adds it to own Observable list
     */
    private void paint(){
        if(((KeyObject) model).collected){
            setVisible(false);
        }
    }

    /**
     * Updates the view when notified by the model.
     */
    @Override
    public void update(Observable var1) {
        paint();
    }
}
