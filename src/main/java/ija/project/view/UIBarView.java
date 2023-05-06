package ija.project.view;

import ija.project.common.MazeObject;
import ija.project.common.Observable;
import ija.project.pacman_project.PacManApp;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


import java.io.IOException;

public class UIBarView extends Group implements Observable.Observer {
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
        HBox hbox = new HBox(2);
        switch (this.model.getLives()) {
            case 1 -> hbox.getChildren().addAll(new ImageView(this.heartImage));
            case 2 -> hbox.getChildren().addAll(new ImageView(this.heartImage), new ImageView(this.heartImage));
            case 3 -> hbox.getChildren().addAll(new ImageView(this.heartImage), new ImageView(this.heartImage), new ImageView(this.heartImage));
        }

        Label score = new Label("SCORE: " + this.model.getScore());
        score.setTranslateX(400);
        score.setStyle("-fx-text-fill: #00022A; -fx-font-size: 20px; -fx-font-weight: bold ");
        getChildren().addAll(hbox, score);

    }

    public void update(Observable var1){
        paint();
    }
}