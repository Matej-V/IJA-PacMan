package ija.project.game;

/**
 * Class representing game exception. Exception is thrown when the game is lost or won.
 * @author Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 */
public class GameException extends Exception {
    public TypeOfException type;

    public enum TypeOfException {
        CompletedGame,
        LostGame,
        Other
    }

    /**
     * Constructor for GameException
     * 
     * @param type type of exception
     */
    public GameException(TypeOfException type) {
        super();
        this.type = type;
    }
}
