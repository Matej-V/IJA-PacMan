package ija.project.game;

/**
 * Exception to handle game scenarios such as completed game or lost game.
 */
public class GameException extends Exception{
    public TypeOfException type;
    public enum TypeOfException {
        CompletedGame,
        LostGame,
        Other
    }

    public GameException(TypeOfException type){
        super();
        this.type = type;
    }
}
