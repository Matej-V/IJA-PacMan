package ija.project.game;

/**
 * @authors Matej Vadovič(xvadov01), Alina Vinogradova(xvinog00)
 * @brief Class representing game exception. Exception is thrown when the game is lost or won.
 */
public class GameException extends Exception{
    public TypeOfException type;
    public enum TypeOfException {
        CompletedGame,
        LostGame,
        Other
    }

    /**
     * Constructor for GameException
     * @param type type of exception
     */
    public GameException(TypeOfException type){
        super();
        this.type = type;
    }
}
