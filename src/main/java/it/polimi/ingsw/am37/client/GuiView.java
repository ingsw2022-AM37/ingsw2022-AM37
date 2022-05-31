package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

public class GuiView extends AbstractView {

    /**
     * Method used to ask which assistant player want to use
     *
     * @param client the client to get the nickname of the current player
     * @return The chosen assistant
     */
    @Override
    public int askAssistant(Client client) {
        return 0;
    }

    /**
     * Method used to ask a player which character he wants to play
     *
     * @return
     */
    @Override
    public Effect askCharacter() {
        return null;
    }

    /**
     * @return which cloud player has chosen to take
     */
    @Override
    public String askCloud() {
        return null;
    }

    /**
     * Method used to ask user to confirm at the provided message
     *
     * @param message a string containing the text to express confirm or negation
     * @return Client's response
     */
    @Override
    public Boolean askConfirm(String message) {
        return null;
    }

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his
     * parameters
     *
     * @return Client's provided parameters
     */
    @Override
    public Client.ConnectionParameters askConnectionParameters() {
        return null;
    }

    @Override
    public Client.LobbyParameters askLobbyParameters() {
        return null;
    }

    @Override
    public int askIsland() {
        return 0;
    }

    /**
     * @return Where mother nature has to go
     */
    @Override
    public int askMotherNature() {
        return 0;
    }

    /**
     * Method used to ask which player you want to look at
     *
     * @return chosen player
     */
    @Override
    public Player askPlayer() {
        return null;
    }

    /**
     * Ask player which students want to move and where
     *
     * @param client the client to get the status of the current player
     * @return HashMap with responses of the player
     */
    @Override
    public StudentsContainer askStudents(Client client) {
        return null;
    }

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    @Override
    public String chooseNickname() {
        return null;
    }

    /**
     * Method to tell the player the game has begun
     */
    @Override
    public void gameStarted() {

    }

    /**
     * Show to the view that now is the turn of the provided user's nickname
     *
     * @param nick nickname of player who has to play the current turn
     */
    @Override
    public void hisTurn(String nick) {

    }

    /**
     * Method to notify if client or server has lost the connection
     */
    @Override
    public void notifyInternetCrash() {

    }

    /**
     * Show the winner of the match
     *
     * @param nick the winner player
     */
    @Override
    public void printWinner(String nick) {

    }

    /**
     * Show all the character of this match
     */
    @Override
    public void showCharacters() {

    }

    /**
     * Method used to display connection info
     *
     * @param client the client to show info about
     */
    @Override
    public void showConnection(Client client) {

    }

    /**
     * Method used to display the assistant deck of a player
     *
     * @param player the player to show the deck of
     */
    @Override
    public void showDeck(Player player) {

    }

    /**
     * This function print the view of a player's status: his last assistant and board
     *
     * @param player the players to show status of
     */
    @Override
    public void showPlayerStatus(Player player) {

    }

    /**
     * Method used to show the nickname of all players in this match
     */
    @Override
    public void showPlayersNicknames() {

    }

    /**
     * This function draw the current status of the table: islands and boards
     */
    @Override
    public void showTable() {

    }

    /**
     * @param client the client to get the status of the current player
     * @return Player's command at any time
     */
    @Override
    public ActionType takeInput(Client client) {
        return null;
    }

    /**
     * Method used to tell the player he is waiting for the match
     */
    @Override
    public void waitingMatch() {

    }

    /**
     * Generic notification of an input error
     */
    @Override
    public void wrongInsert() {

    }

    /**
     * Notify when a string between "cli" or "gui" was expected but another string was given
     */
    @Override
    public void wrongInsertGraphics() {

    }

    /**
     * Notify when a number port is expected but another input was given
     */
    @Override
    public void wrongInsertPort() {

    }

    /**
     * Notify when requested server is unreachable
     */
    @Override
    public void wrongServer() {

    }

    /**
     * Tell the player it's his turn
     */
    @Override
    public void yourTurn() {

    }

    @Override
    public void displayInfo(String message) {

    }

    @Override
    public void displayImportant(String message) {

    }

    @Override
    public void displayError(String message) {

    }
}
