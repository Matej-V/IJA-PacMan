package ija.project.pacman_project;


import ija.project.game.PacmanObject;
import ija.project.view.FieldView;
import ija.project.view.UIBarView;
import javafx.scene.Group;

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
    private Group mazeGroup;
    private Group UIGroup;
    private Image heartImage;

    public PacManView(PacManModel model) {
        this.model = model;
        cellWidth = 500 / this.model.maze.numCols();
        this.mazeGroup = new Group();
        this.UIGroup = new Group();
    }

    /**
     * Generates maze representation
     *
     * @return Group that represents Maze
     */
    public Group drawMaze() throws IOException {
        this.mazeGroup.getChildren().clear();

       /* Now all the fields are generated here as FieldView objects and added into mazeGroup */  
        for (int row = 0; row < this.model.maze.numRows(); row++) {
            for (int column = 0; column < this.model.maze.numCols(); column++) {
                FieldView fieldView = new FieldView(this.model.maze.getField(row, column), cellWidth, row, column);
                this.mazeGroup.getChildren().add(fieldView);
            }
        }

        return this.mazeGroup;
    }

    public Group drawUI() throws IOException {
        /* TODO - implement observer logic with these UI elements*/
//        Rectangle healthBar = new Rectangle(500, 40);
        System.out.println(this.model.pacman);
        UIBarView UIBar = new UIBarView(this.model.pacman);
        this.UIGroup.getChildren().add(UIBar);
        return this.UIGroup;
    }

    public VBox generateGame() throws IOException {
        // Create Menu
        Menu menu = new Menu("Game");
        MenuItem mapChange = new MenuItem("Change Map");
        MenuItem saveGame = new MenuItem("Save Game");
        MenuItem loadGame = new MenuItem("Load Game");

        //Create MenuBar
        MenuBar menuBar = new MenuBar(menu);
        menu.getItems().addAll(mapChange, saveGame, loadGame);
        mapChange.setOnAction(e -> {
            System.out.println("Changing map");
        });

        saveGame.setOnAction(e -> {
            // TODO: save data from model to .save file
            System.out.println("Saving the game");
        });

        loadGame.setOnAction(e -> {
            // TODO: load data from .save file to the model
            //       + move all the objects to their saved positions
            //         according to their paths
            this.model.loadGame();
            this.model.pacman.moveFromSave();
            System.out.println("Loading the game");
        });

        VBox gameBox = new VBox(menuBar, drawUI(), drawMaze());
        gameBox.setStyle("-fx-background-color: #00022A");
        return gameBox;
    }
}