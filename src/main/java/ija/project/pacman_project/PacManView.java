package ija.project.pacman_project;

import ija.project.common.Field;
import ija.project.common.MazeObject;
import ija.project.game.GhostObject;
import ija.project.game.PathField;
import ija.project.game.TargetField;
import ija.project.game.WallField;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PacManView {
    private PacManModel model;
    private double cellWidth;
    private double cellHeight;
    private Group mazeGroup;

    private Circle pacMan;

    private Image heartImage;
    private Image deathImage;

    public PacManView(PacManModel model) {
        this.model = model;
        cellWidth = 500 / this.model.maze.numCols();
        cellHeight = 500 / this.model.maze.numRows();
        this.mazeGroup = new Group();

        try {
            this.heartImage = new Image(PacManApp.class.getResource("img/heart.png").openStream());
            this.deathImage = new Image(PacManApp.class.getResource("img/death.png").openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Generates maze representation
     *
     * @return Group that represents Maze
     */
    public Group drawMaze() throws IOException {
        this.mazeGroup.getChildren().clear();

        for (int row = 0; row < this.model.maze.numRows(); row++) {
            for (int column = 0; column < this.model.maze.numCols(); column++) {
                Field currentField = this.model.maze.getField(row, column);
                Rectangle field = new Rectangle(column * cellWidth, row * cellHeight, cellWidth, cellHeight);
                if (currentField instanceof WallField) {
                    for (Field.Direction dir : ija.project.common.Field.Direction.values()) {
                        if (!(currentField.nextField(dir) instanceof WallField)) {
                            Line line = new Line();
                            // set the start and end points of the line
                            switch (dir) {
                                case U:
                                    line.setStartX(field.getX());
                                    line.setStartY(field.getY());
                                    line.setEndX(field.getX() + field.getWidth());
                                    line.setEndY(field.getY());
                                    break;
                                case D:
                                    line.setStartX(field.getX());
                                    line.setStartY(field.getY() + field.getHeight());
                                    line.setEndX(field.getX() + field.getWidth());
                                    line.setEndY(field.getY() + field.getHeight());
                                    break;
                                case L:
                                    line.setStartX(field.getX());
                                    line.setStartY(field.getY());
                                    line.setEndX(field.getX());
                                    line.setEndY(field.getY() + field.getHeight());
                                    break;
                                case R:
                                    line.setStartX(field.getX() + field.getWidth());
                                    line.setStartY(field.getY());
                                    line.setEndX(field.getX() + field.getWidth());
                                    line.setEndY(field.getY() + field.getHeight());
                                    break;
                            }
                            line.setStroke(Color.web("#051D9D"));
                            line.setStrokeWidth(5);
                            line.setStrokeLineCap(StrokeLineCap.BUTT);
                            this.mazeGroup.getChildren().add(line);
                        }

                    }
                    this.mazeGroup.getChildren().add(field);
                } else if (currentField instanceof TargetField) {
                    Circle target = new Circle(field.getWidth() * 0.3, Color.web("#B02E0C"));
                    target.setCenterX(field.getX() + field.getWidth() / 2);
                    target.setCenterY(field.getY() + field.getHeight() / 2);
                    this.mazeGroup.getChildren().addAll(target);
                } else if (currentField instanceof PathField) {
                        Circle point = new Circle(3, Color.web("#CED6EE"));
                        if (currentField.isEmpty()){
                            if (((PathField) currentField).point){
//                                System.out.println("Score: " + this.model.getScore());
                                point.setCenterX(field.getX() + field.getWidth()/2);
                                point.setCenterY(field.getY() + field.getHeight()/2);
                                this.mazeGroup.getChildren().addAll(point);
                            }
                        }
                    if (currentField.contains(this.model.pacman)) {
                        this.mazeGroup.getChildren().remove(this.pacMan);
                        this.pacMan = new Circle(field.getWidth() * 0.35, Color.web("#FFF901"));
                        this.pacMan.setCenterX(field.getX() + field.getWidth() / 2);
                        this.pacMan.setCenterY(field.getY() + field.getHeight() / 2);
                        this.mazeGroup.getChildren().addAll(pacMan);
                    }
                }
                for (MazeObject object : this.model.maze.ghosts()) {
                    if (currentField.contains(object)) {
                        Circle ghost = new Circle(field.getWidth() * 0.25, ((GhostObject) object).color);
                        ghost.setCenterX(field.getX() + field.getWidth() / 2);
                        ghost.setCenterY(field.getY() + field.getHeight() / 2);
                        ghost.setStrokeWidth(field.getWidth() * 0.1);
                        ghost.setStroke(Color.web("#B02E0C"));
                        this.mazeGroup.getChildren().addAll(ghost);
                    }
                }
            }
        }
        ImageView imageView1 = new ImageView(this.heartImage);
        ImageView imageView2 = new ImageView(this.heartImage);
        imageView2.setX(40);
        ImageView imageView3 = new ImageView(this.heartImage);
        imageView3.setX(80);

        Group healthBar;

        if (this.model.pacman.getLives() == 3){
            healthBar = new Group(imageView1, imageView2, imageView3);
        } else if(this.model.pacman.getLives() == 2){
            healthBar = new Group(imageView1, imageView2);
        } else if(this.model.pacman.getLives() == 1){
            healthBar = new Group(imageView1);
        } else {
            ImageView death = new ImageView(this.deathImage);
            healthBar = new Group(death);
        }

        this.mazeGroup.getChildren().add(healthBar);

        return this.mazeGroup;
    }

    public VBox generateGame() throws IOException {
        // Create Menu
        Menu menu = new Menu("Menu");
        MenuItem mapChange = new MenuItem("Change Map");

        //Create MenuBar
        MenuBar menuBar = new MenuBar(menu);
        menu.getItems().addAll(mapChange);
        mapChange.setOnAction(e -> {
            System.out.println("Changing map");
        });

        // Create healthbar
//        Image image = new Image(PacManApp.class.getResource("img/heart.png").openStream());
//        ImageView imageView1 = new ImageView(image);
//        ImageView imageView2 = new ImageView(image);
//        imageView2.setX(40);
//        ImageView imageView3 = new ImageView(image);
//        imageView3.setX(80);

//        Group healthBar = new Group(imageView1, imageView2, imageView3);


        VBox gameBox = new VBox(menuBar, drawMaze());
        gameBox.setStyle("-fx-background-color: #00022A");
        return gameBox;
    }
}