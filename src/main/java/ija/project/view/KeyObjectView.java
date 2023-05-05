package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.KeyObject;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class KeyObjectView extends Pane implements Observable.Observer {
    Rectangle key;
    MazeObject model;
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
    private void paint(){
        if(((KeyObject) model).collected){
            setVisible(false);
        }
    }

    @Override
    public void update(Observable var1) {
        paint();
    }
}
