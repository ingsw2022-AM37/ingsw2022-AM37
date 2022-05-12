package it.polimi.ingsw.am37.client;

import java.util.HashMap;

public abstract class AbstractView {

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
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his parameters
     *
     * @param address  It's how address parameter is called
     * @param port     It's how port parameter is called
     * @param graphics It's how graphics parameter is called
     * @param params   It's the HashMap containing for each parameter(key) it's value, so the current address, the current port and the current graphics type
     * @return
     */
    public abstract String insertYourParameters(String address, String port, String graphics, HashMap<String, String> params);

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

}
