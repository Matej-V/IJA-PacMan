package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.BombObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Class representing the view of the bomb. It is an observer of the bomb it represents.
 * It is a child of the {@link FieldView}.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class BombObjectView extends Pane implements Observable.Observer {
    /**
     * Parent {@link FieldView} of this object
     */
    FieldView parent;
    /**
     * Model of the {@link BombObject}
     */
    BombObject model;
    /**
     * Image of the bomb
     */
    ImageView bomb;
    /**
     * Width of the bomb. It changes as it gets closer to detonation
     */
    double bombSize;

    /**
     * Constructor of the {@link BombObjectView}
     * @param parent parent {@link FieldView} of this object
     * @param model model of the {@link BombObject}
     */
    public BombObjectView(FieldView parent, MazeObject model){
        this.parent = parent;
        this.model = (BombObject) model;
        this.bombSize = parent.size / this.model.getTimeToDetonation();
        bomb = new ImageView(new Image("file:lib/bomb.png"));
        bomb.setFitWidth(bombSize);
        bomb.setFitHeight(bombSize);
        bomb.setPreserveRatio(true);
        bomb.setTranslateX((parent.size - bomb.getFitWidth()) / 2);
        bomb.setTranslateY((parent.size - bomb.getFitHeight()) / 2);
        getChildren().addAll(bomb);
        model.addObserver(this);
    }

    @Override
    public void update(Observable var1) {
        if(model.getTimeToDetonation() > 0){
            bombSize = parent.size / this.model.getTimeToDetonation();
            bomb.setFitWidth(bombSize);
            bomb.setFitHeight(bombSize);
            bomb.setTranslateX((parent.size - bomb.getFitWidth()) / 2);
            bomb.setTranslateY((parent.size - bomb.getFitHeight()) / 2);
        }else{
            model.getField().remove(model);
        }
    }
}
