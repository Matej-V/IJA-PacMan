package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.KeyObject;
import ija.project.pacman_project.PacManApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.Objects;

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
        try {
            this.key = new Image(
                    Objects.requireNonNull(PacManApp.class.getResource("/ija/project/img/key.png")).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageView keyImageView = new ImageView(this.key);
        getChildren().add(new Rectangle(parent.size, parent.size, Color.TRANSPARENT));
        setTranslateX(parent.x);
        setTranslateY(parent.y);
        keyImageView.setFitWidth(parent.size * 0.6);
        keyImageView.setPreserveRatio(true);
        keyImageView.setTranslateX((parent.size - keyImageView.getFitWidth()) / 2);
        keyImageView.setTranslateY(parent.size * 0.2);
        getChildren().add(keyImageView);
        paint();
        model.addObserver(this);
    }

    /**
     * Paints a key as a rectangle and adds it to own Observable list
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
