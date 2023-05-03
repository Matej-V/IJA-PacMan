package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.pacman_project.PacManApp;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;


import java.io.IOException;

public class UIBarView extends Group implements ComponentView, Observable.Observer {
    private final MazeObject model;
    private Image heartImage;
    private Image deathImage;

    public UIBarView(MazeObject model) {
        this.model = model;

        try {
            this.heartImage = new Image(PacManApp.class.getResource("img/heart.png").openStream());
            this.deathImage = new Image(PacManApp.class.getResource("img/death.png").openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        paint();
        model.addObserver(this);
    }

    public void paint() {
        getChildren().clear();
//        ImageView heart = new ImageView(this.heartImage);
//        ImageView heart2 = new ImageView(this.heartImage);
//        ImageView heart3 = new ImageView(this.heartImage);
        Label hearts = new Label("Health: " + this.model.getLives());
        Label score = new Label("Score: " + this.model.getScore());
        score.setTranslateX(400);
        hearts.setTranslateX(800);
        score.setStyle("-fx-text-fill: #00022A; -fx-font-size: 16px; ");
        hearts.setStyle("-fx-text-fill: #00022A; -fx-font-size: 16px; ");
        getChildren().addAll(hearts, score);

    }

    public void update(Observable var1){
        paint();
    }
}