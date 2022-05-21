package it.polimi.ingsw.am37.client;


import it.polimi.ingsw.am37.model.Player;

import java.util.HashMap;

public abstract class AbstractView {

    /**
     * Model in client
     */
    protected final ReducedModel reducedModel;

    /**
     * Default constructor
     */
    public AbstractView() {
        this.reducedModel = new ReducedModel();
    }

    /**
     * @return reduced model for the client
     */
    public ReducedModel getReducedModel() {
        return reducedModel;
    }

    /**
     * This method notifies if address is unknown
     *
     * @param address Address written by the player during connection to server
     */
    public abstract void ifNonLocalhostAddress(String address);

    /**
     * Method to notify if client or server has lost the connection
     */
    public abstract void notifyInternetCrash();

    /**
     * Notify if a player has inserted fewer parameters than expected during opening of the terminal
     */
    public abstract void wrongInsertFewArguments();

    /**
     * Generic notification of an input error
     */
    public abstract void wrongInsert();

    /**
     * Notify when a number port is expected but another input was given
     */
    public abstract void wrongInsertPort();

    /**
     * Notify when a string between "cli" or "gui" was expected but another string was given
     */
    public abstract void wrongInsertGraphics();

    /**
     * Notify when requested server is unreachable
     */
    public abstract void wrongServer();

    /**
     * Method used to ask if player wants to use default parameters for connection
     *
     * @return Client's response
     */
    public abstract String askDefault();

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his
     * parameters
     *
     * @param address  It's how address parameter is called
     * @param port     It's how port parameter is called
     * @param graphics It's how graphics parameter is called
     * @return Client's decision
     */
    public abstract String insertYourParameters(String address, String port, String graphics);

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    public abstract String chooseNickname();

    /**
     * Method used to ask player if he wants to use advanced rules or not
     *
     * @return Player's choice
     */
    public abstract String requestAdvancedRules();

    /**
     * Method used to ask player the total players of the game he wants to join in
     *
     * @return Player's choice
     */
    public abstract String requestNumPlayers();

    /**
     * Method used to ask which assistant player want to use
     *
     * @return The chosen assistant
     */
    public abstract int askAssistant();

    /**
     * Method used to tell player possible commands
     */
    public abstract void possibleChoices();

    /**
     * @return Player's command at any time
     */
    public abstract String takeInput();

    /**
     * Tell player this input isn't ok for now
     */
    public abstract void impossibleInputForNow();

    /**
     * Ask player which students want to move and where
     *
     * @return HashMap with responses of the player
     */
    public abstract HashMap<String, String> askStudents();

    /**
     * @return Where mother nature has to go
     */
    public abstract int askMotherNature();

    /**
     * @return which cloud player has chosen to take
     */
    public abstract String askCloud();

    /**
     * Tell the player it's his turn
     */
    public abstract void yourTurn();

    /**
     * @param nick nickname of player who has to play the current turn
     */
    public abstract void hisTurn(String nick);

    /**
     * Method used to tell a player he has to play the assistant card
     */
    public abstract void mustPlayAssistant();

    /**
     * Method used to tell the player he is waiting for the match
     */
    public abstract void waitingMatch();

    /**
     * Method to tell the player the game has begun
     */
    public abstract void gameStarted();

    /**
     * @param nick the winner player
     */
    public abstract void printWinner(String nick);

    /**
     * Method used to ask a player which character he wants to play
     */
    public abstract void askCharacter();

    /**
     * Method used when an error message come from server
     */
    public abstract void impossibleAssistant();

    /**
     * Method used when an error message come from server
     */
    public abstract void impossibleStudents();

    /**
     * Method used when an error message come from server
     */
    public abstract void impossibleMotherNature();

    /**
     * Method used when an error message come from server
     */
    public abstract void impossibleCloud();

    /**
     * Method used when an error message come from server
     */
    public abstract void impossibleCharacter();


    /**
     * This function draw the current status of the table: islands and boards
     */
    public abstract void showTable();

    /**
     * This function print the view of a player's status: his last assistant and board
     * @param player the players to show status of
     */
    public abstract void showPlayerStatus(Player player);

    public abstract void showDeck(Player player);
}
