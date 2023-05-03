package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.pacman_project.PacManApp;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class UIBarView extends Group implements ComponentView, Observable.Observer {
    private final MazeObject model;
    private Image heartImage;
    private Image deathImage;
    private Group smallBar = new Group();

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
        ImageView h = new ImageView(this.heartImage);
        ImageView h1 = new ImageView(this.heartImage);
        h1.setX(40);
        ImageView h2 = new ImageView(this.heartImage);
        h2.setX(80);
        switch (this.model.getLives()) {
            case 1 -> {
                this.smallBar.getChildren().clear();
                this.smallBar.getChildren().addAll(h);
            }
            case 2 -> {
                this.smallBar.getChildren().clear();
                this.smallBar.getChildren().addAll(h, h1);
            }
            case 3 -> {
                this.smallBar.getChildren().clear();
                this.smallBar.getChildren().addAll(h, h1, h2);
            }
        }
        Label score = new Label("SCORE: " + this.model.getScore());
        score.setTranslateX(400);
        score.setStyle("-fx-text-fill: #cfd0e6; -fx-font-size: 19px; -fx-font-weight: bold;");
        getChildren().addAll(smallBar, score);
    }

    public void update(Observable var1){
        paint();
    }
}
