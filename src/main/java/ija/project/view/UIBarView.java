package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.game.PacmanObject;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Class representing UI bar view, that constains lives and score of the pacman. It is displayed at the top of the screen. It is an observer of the {@link MazeObject} it represents.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class UIBarView extends Group implements Observable.Observer {
    /**
     * Model of the {@link MazeObject}
     */
    private final MazeObject model;
    /**
     * Image of the heart
     */
    private Image heartImage;
    /**
     * Image of the bomb
     */
    private Image bombImage;

    public UIBarView(MazeObject model) {
        this.model = model;
        this.heartImage = new Image("file:lib/heart.png");
        this.bombImage = new Image("file:lib/bomb.png");
        paint();
        model.addObserver(this);
    }

    public void paint() {
        getChildren().clear();
        HBox hboxLives = new HBox(2);
        for (int i = 0; i < model.getLives(); i++){
            ImageView heartView = new ImageView(heartImage);
            heartView.setPreserveRatio(true);
            heartView.setFitHeight(40);
            hboxLives.getChildren().add(heartView);
        }
        HBox hboxBombs = new HBox(2);
        for (int i = 0; i < ((PacmanObject)model).getAvailableBombs(); i++){
            ImageView bombView = new ImageView(bombImage);
            bombView.setPreserveRatio(true);
            bombView.setFitHeight(40);
            hboxLives.getChildren().add(bombView);

        }
        Label score = new Label("SCORE: " + this.model.getScore());
        score.setTranslateX(400);
        score.setStyle("-fx-text-fill: #00022A; -fx-font-size: 20px; -fx-font-weight: bold ");
        getChildren().addAll(hboxLives, hboxBombs, score);

    }

    public void update(Observable var1) {
        paint();
    }
}