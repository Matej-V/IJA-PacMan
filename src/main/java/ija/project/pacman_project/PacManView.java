package ija.project.pacman_project;

import ija.project.game.AbstractObservable;
import ija.project.view.FieldView;
import ija.project.view.UIBarView;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.util.*;

/**
 * Class representing game view. Contains implementation for generating all the views in the game.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class PacManView extends AbstractObservable {
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
    public PacManView(double width, double height) {
        this.widthOfScreen = width;
        this.heightOfScreen = height;
    }

    /**
     * Sets controller that is used for setting button actions and accessing model
     * 
     * @param controller to be set
     */
    public void setController(PacManController controller) {
        this.controller = controller;
    }

    /**
     * Generates maze representation using FieldView class objects
     * 
     * @return Group od FieldView class objects that represents maze
     */
    public Group drawMaze() {
        Group mazeGroup = new Group();
        for (int row = 0; row < controller.maze.numRows(); row++) {
            for (int column = 0; column < controller.maze.numCols(); column++) {
                FieldView fieldView = new FieldView(controller.maze.getField(row, column),
                        (Math.min(widthOfScreen, heightOfScreen) - 100) / controller.maze.numCols(), row, column);
                fieldView.setOnMouseClicked(event -> controller.setPacmanPathOnClick(fieldView));
                mazeGroup.getChildren().add(fieldView);
            }
        }
        return mazeGroup;
    }

    /**
     * Draws score, lives and bombs count of player
     * @return Score and health bar representation
     */
    public Group drawUI() {
        UIBarView UIBar = new UIBarView(controller.maze.getPacMan());
        return new Group(UIBar);
    }

    /**
     * Generates Main screen that is displayed when game is started
     */
    public void generateMainScreen() {
        StackPane pane = new StackPane(
                drawBackgroundImage("file:lib/title.jpg"),
                drawButton("START THE GAME"));
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
    }

    /**
     * generate game that consists of maze and UI
     */
    public void generateGame() {
        // Create Menu
        Menu menuOptions = new Menu("Menu");
        Menu replayOptions = new Menu("Replay");
        Menu leadersOption = new Menu("Leaderboard");
        Menu helpOption = new Menu("Help");
        MenuItem newGameMenuItem = new MenuItem("New Game");
        MenuItem pauseGameMenuItem = new MenuItem("Pause Game");
        MenuItem replayGameMenuItem = new MenuItem("Replay Game");
        MenuItem reverRelayGameMenuItem = new MenuItem("Replay Game in Reverse Mode");
        MenuItem leadersMenuItem = new MenuItem("Show Leaderboard");
        // Tooltip button
        MenuItem helpMenuItem = new MenuItem("Help");

        DialogPane dialogPane = new DialogPane();
        dialogPane.setContentText("Controls:\tW, A, S, D keys or Arrow keys\nP\tPause Game\nR\tReplay Game\nB\tBackwards replay\nE\tPlace bomb");
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Help");
        dialog.setDialogPane(dialogPane);
        Button closeButton = new Button("Close");
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        dialogPane.lookupButton(ButtonType.CLOSE).addEventFilter(ActionEvent.ACTION, event -> dialog.close());
        helpMenuItem.setOnAction(event -> dialog.showAndWait());
        replayGameMenuItem.setOnAction(event -> controller.changeGameState(PacManController.GameState.REPLAY));
        reverRelayGameMenuItem.setOnAction(event -> controller.changeGameState(PacManController.GameState.REPLAY_REVERSE));
        newGameMenuItem.setOnAction(e -> controller.newGame());
        pauseGameMenuItem.setOnAction(e -> controller.changeGameState(PacManController.GameState.PAUSE));

        leadersMenuItem.setOnAction(e -> this.drawLeaderBoard());

        menuOptions.getItems().addAll(newGameMenuItem, pauseGameMenuItem);
        replayOptions.getItems().addAll(replayGameMenuItem, reverRelayGameMenuItem);
        leadersOption.getItems().add(leadersMenuItem);
        helpOption.getItems().addAll(helpMenuItem);
        MenuBar menuBar = new MenuBar(menuOptions, replayOptions, helpOption);

        VBox uiMazeBox = new VBox();
        HBox mazeHolder = new HBox(drawMaze());
        mazeHolder.alignmentProperty().set(Pos.CENTER);
        HBox statsHolder = new HBox(drawUI());
        statsHolder.setMaxHeight(60);
        statsHolder.alignmentProperty().set(Pos.CENTER);
        statsHolder.setStyle("-fx-background-color: #FFFFFF;");
        uiMazeBox.getChildren().addAll(statsHolder, mazeHolder);
        // Stack Pane to let us add pause screen over game screen
        StackPane temp = new StackPane(uiMazeBox);
        temp.setAlignment(Pos.CENTER);
        this.gameBox = temp;

        // Set background image
        StackPane pane = new StackPane(
                drawBackgroundImage("file:lib/title.jpg"),
                new VBox(menuBar, gameBox));
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
        notifyObservers();
    }

    /**
     * Generate screen that player sees when game is lost
     */
    public void generateEndScreen() {
        System.out.println("Generating end screen");
        // Score
        Text score = new Text("Total score: " + controller.maze.getPacMan().getScore());
        score.setStyle("-fx-font-size: 20px; -fx-fill: #FFFFFF");
        score.setTranslateY(-100);
        StackPane pane = new StackPane(
                drawBackgroundImage("file:lib/game-over.jpg"),
                drawButton("PLAY AGAIN"), score);
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
        notifyObservers();
    }

    /**
     * Generates screen thath player sees when game is won
     */
    public void generateSuccessScreen() {
        System.out.println("Generating success screen");
        // Score
        Text score = new Text("Total score: " + controller.maze.getPacMan().getScore());
        score.setStyle("-fx-font-size: 20px; -fx-fill: #FFFFFF");
        score.setTranslateY(-50);
        StackPane pane = new StackPane();
        // add win text
        Text text = new Text("YOU WON");
        text.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-fill: white;");
        text.setTranslateY(-250);

        TextField textField = new TextField();

        HBox hb = new HBox();
        hb.setTranslateY(-150);
        textField.setPrefSize(120, 40);
        textField.setPromptText("Your name");
        textField.setStyle("-fx-font-size: 20px;");
        Button save = drawButton("SAVE");
        save.setOnAction(e -> {
            if (!textField.getText().isEmpty()) {
                controller.setCurrentUser(textField.getText());
                controller.writeToLeaderboard();
                save.setDisable(true);
            }

        });
        Button leaderboardButton = drawButton("Open Leaderboard");
        leaderboardButton.setTranslateY(-100);
        leaderboardButton.setOnAction(e -> this.drawLeaderBoard());
        hb.getChildren().addAll(textField, save);
        textField.getParent().requestFocus();
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);

        pane.getChildren().addAll(drawBackgroundImage("file:lib/title.jpg"), text, hb, leaderboardButton,
                drawButton("PLAY AGAIN"), score);
        // Load button
        pane.setAlignment(Pos.CENTER);
        this.currentScene = pane;
        notifyObservers();
    }

    /**
     * Generates leaderboard as a dialog
     */
    private void drawLeaderBoard() {
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().add(new Label("Leaderboard:"));
        vb.getChildren().add(new Label("Name → Score"));
        vb.setPrefWidth(200);
        //write first 3 players
        int entries = Math.min(3, controller.getLeaders().size());
        for (Map.Entry<String, Integer> entry : controller.leaders.entrySet()) {
            if (entries-- == 0)break;
            vb.getChildren().add(new Label(entry.getKey() + " → " + entry.getValue()));
        }
        DialogPane lead = new DialogPane();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Leaders");
        dialog.setDialogPane(lead);
        Button closeButton = new Button("Close");
        lead.getButtonTypes().add(ButtonType.CLOSE);
        lead.lookupButton(ButtonType.CLOSE).addEventFilter(ActionEvent.ACTION, event -> dialog.close());

        lead.setContent(vb);
        dialog.showAndWait();
    }

    /**
     * Method that generates background image according to given url
     * 
     * @param url url of image
     * @return ImageView object that represents background image
     */
    private ImageView drawBackgroundImage(String url) {
        ImageView backgroundImage = new ImageView();
        backgroundImage.setImage(new Image(url));
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
     * 
     * @param text text that will be displayed on button
     * @return Button object
     */
    private Button drawButton(String text) {
        Button button = new Button();
        button.setText(text);
        button.setStyle(
                "-fx-background-color: #FFFFFF; -fx-text-fill: #00022A; -fx-border-radius: 20%; -fx-font-size: 16px");
        button.setOnAction(e -> {
            System.out.println(text + " button clicked");
            controller.newGame();
        });

        button.requestFocus();
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                button.fire(); // Simulate button action
                event.consume(); // Consume the event to prevent further processing
            }
        });
        return button;
    }
}