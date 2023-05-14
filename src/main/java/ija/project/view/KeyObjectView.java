package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.KeyObject;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Class representing the view of the key. It is an observer of the key it represents. It is a child of {@link FieldView}.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class KeyObjectView extends Pane implements Observable.Observer {
    /**
     * Image representing key
     */
    Image key;
    /**
     * Model of the {@link MazeObject}
     */
    MazeObject model;
    /**
     * Parent {@link FieldView} of this object
     */
    FieldView parent;

    public KeyObjectView(FieldView parent, MazeObject model) {
        this.parent = parent;
        this.model = model;
        this.key = new Image("file:lib/key.png");
        ImageView keyImageView = new ImageView(this.key);
        getChildren().add(new Rectangle(parent.size, parent.size, Color.TRANSPARENT));
        keyImageView.setFitWidth(parent.size * 0.6);
        keyImageView.setPreserveRatio(true);
        keyImageView.setTranslateX((parent.size - keyImageView.getFitWidth()) / 2);
        keyImageView.setTranslateY(parent.size * 0.2);
        getChildren().add(keyImageView);
        paint();
        model.addObserver(this);
    }

    /**
     * Sets the visibility of the view according to the model.
     */
    private void paint() {
        if (((KeyObject) model).collected) {
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
