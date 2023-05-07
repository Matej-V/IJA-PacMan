package ija.project.pacman_project;

import ija.project.view.FieldView;
import ija.project.view.UIBarView;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * PacManView class is responsible for generating all the views in the game
 */
public class PacManView extends AbstractObservableView{
    /**
     * Controller to be used for setting button actions
     */
    private PacManController controller;
    /**
     * Current scene that is displayed
     */
    public Pane currentScene;
    /**
     * Width of the screen
     */
    private final double widthOfScreen;
    /**
     * Height of the screen
     */
    private final double heightOfScreen;
    /**
     * Represents UI + maze part of currentScreen
     */
    public StackPane gameBox;

    /**
     * PacManView constructor
     * 
     * @param width  of the screen
     * @param height of the screen
     */
    public PacManView( double width, double height) {
        this.widthOfScreen = width;
        this.heightOfScreen = height;
    }

    /**
     * Sets controller that is used for setting button actions and accessing model
     * @param controller to be set
     */
    public void setController(PacManController controller) {
        this.controller = controller;
    }

    /**
     * Generates maze representation using FieldView class objects
     * @return Group od FieldView class objects that represents maze
     */
    public Group drawMaze() {
        Group mazeGroup = new Group();
        for (int row = 0; row < controller.maze.numRows(); row++) {
            for (int column = 0; column < controller.maze.numCols(); column++) {
                FieldView fieldView = new FieldView(controller.maze.getField(row, column), (Math.min(widthOfScreen, heightOfScreen) - 100) / controller.maze.numCols(), row, column);
                mazeGroup.getChildren().add(fieldView);
            }
        }
        return mazeGroup;
    }

    /**
     * 
     * @return Score and healt bar representation
     */
    public Group drawUI() {
        UIBarView UIBar = new UIBarView(controller.maze.getPacMan());
        return new Group(UIBar);
    }

    /**
     * Generates Main screen that is displayed when game is started
     */
    public void generateMainScreen() {
        StackPane pane = new StackPane(drawBackgroundImage("./src/main/resources/ija/project/pacman_project/img/title.jpg"), drawButton("START THE GAME"));
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
    }

    /**
     * generate game that consists of maze and UI
     */
    public void generateGame() {
        // Create Menu
        Menu menu = new Menu("Menu");
        MenuItem newGame = new MenuItem("New Game");
        MenuItem pauseGame = new MenuItem("Pause Game");

        // Create MenuBar
        MenuBar menuBar = new MenuBar(menu);
        newGame.setOnAction(e -> {
            System.out.println("New Game starting");
            controller.newGame();
        });
        pauseGame.setOnAction(e -> {
            System.out.println("Game paused");
            controller.changeGameState(PacManController.GameState.PAUSE);
        });
        menu.getItems().addAll(newGame, pauseGame);

        VBox uiMazeBox = new VBox();
        HBox mazeHolder = new HBox(drawMaze());
        mazeHolder.alignmentProperty().set(Pos.CENTER);
        HBox statsHolder = new HBox(drawUI());
        statsHolder.alignmentProperty().set(Pos.CENTER);
        statsHolder.setStyle("-fx-background-color: #FFFFFF;");
        uiMazeBox.getChildren().addAll(statsHolder, mazeHolder);
        //Stack Pane to let us add pause screen over game screen
        StackPane temp = new StackPane(uiMazeBox);
        temp.setAlignment(Pos.CENTER);
        this.gameBox = temp;
        // Set background image
        StackPane pane = new StackPane(drawBackgroundImage("./src/main/resources/ija/project/pacman_project/img/title.jpg"), new VBox(menuBar, gameBox));
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
        notifyObservers();
    }

    /**
     * Generate screen that player sees when game is lost
     */
    public void generateEndScreen() {
        System.out.println("Generating end screen");
        //Score
        Text score = new Text("Total score: " + controller.maze.getPacMan().getScore());
        score.setStyle("-fx-font-size: 20px; -fx-fill: #FFFFFF");
        score.setTranslateY(-100);
        StackPane pane = new StackPane(drawBackgroundImage("./src/main/resources/ija/project/pacman_project/img/game-over.jpg"), drawButton("PLAY AGAIN"), score);
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
        notifyObservers();
    }

    /**
     * Generates screen thath player sees when game is won
     */
    public void generateSuccessScreen(){
        System.out.println("Generating success screen");
        //Score
        Text score = new Text("Total score: " + controller.maze.getPacMan().getScore());
        score.setStyle("-fx-font-size: 20px; -fx-fill: #FFFFFF");
        score.setTranslateY(-50);
        StackPane pane = new StackPane(drawBackgroundImage("./src/main/resources/ija/project/pacman_project/img/title.jpg"), drawButton("PLAY AGAIN"), score);
        // add win text
        Text text = new Text("YOU WON");
        text.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-fill: white;");
        text.setTranslateY(-150);
        pane.getChildren().add(text);
        // Load button
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
        notifyObservers();
    }

    /**
     * Method that generates background image according to given url
     * @param url url of image
     * @return ImageView object that represents background image
     */
    private ImageView drawBackgroundImage(String url){
        ImageView backgroundImage = new ImageView();
        try {
            backgroundImage.setImage(new Image(new FileInputStream(url)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        backgroundImage.setX(0);
        backgroundImage.setY(0);
        backgroundImage.setFitWidth(widthOfScreen);
        backgroundImage.setFitHeight(heightOfScreen);
        Blend blend = new Blend();
        blend.setMode(BlendMode.MULTIPLY);
        backgroundImage.setEffect(blend);
        return backgroundImage;
    }

    /**
     * Method that generates button with given text
     * @param text text that will be displayed on button
     * @return Button object
     */
    private Button drawButton(String text){
        Button button = new Button();
        button.setText(text);
        button.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #00022A; -fx-border-radius: 20%; -fx-font-size: 16px");
        button.setOnAction(e -> {
            System.out.println(text + " button clicked");
            controller.newGame();
        });
        button.requestFocus();
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                button.fire();      // Simulate button action
                event.consume();    // Consume the event to prevent further processing
            }
        });
        return button;
    }
}