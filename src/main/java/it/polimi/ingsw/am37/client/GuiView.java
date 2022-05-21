package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.model.Player;

import java.util.HashMap;
import java.util.Scanner;

public class GuiView extends AbstractView {

    /**
     * This method notifies if address is unknown
     *
     * @param address Address written by the player during connection to server
     */
    public void ifNonLocalhostAddress(String address) {
        if (!address.equals("localhost"))
            System.out.println(" You have put an address different from \"localhost\", if this doesn't exists it will be considered \"localhost\" \n");
    }

    /**
     * Method to notify if client or server has lost the connection
     */
    public void notifyInternetCrash() { //TODO

    }

    /**
     * Notify if a player has inserted fewer parameters than expected during opening of the terminal
     */
    public void wrongInsertFewArguments() {
        System.out.println(" You have written too few arguments \n");
    }

    /**
     * Generic notification of an input error
     */
    public void wrongInsert() {
        System.out.println(" You have written wrong parameters \n");
    }

    /**
     * Notify when a number port is expected but another input was given
     */
    public void wrongInsertPort() {
        System.out.println(" You haven't written a number as server's port \n");
    }

    /**
     * Notify when a string between "cli" or "gui" was expected but another string was given
     */
    public void wrongInsertGraphics() {
        System.out.println(" You had to choose between \"cli\" or \"gui\" \n");
    }

    /**
     * Notify when requested server is unreachable
     */
    public void wrongServer() {
        System.out.println(" This server is unreachable \n");
    }

    /**
     * Method used to ask if player wants to use default parameters for connection
     *
     * @return Client's response
     */
    public String askDefault() {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(" Do you want to use default options? Please write \"yes\" or \"no\" or \"close game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("yes") || s.equals("no") || s.equals("close game"))
                return s;
            wrongInsert();
        }
    }

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his parameters
     *
     * @param address  It's how address parameter is called
     * @param port     It's how port parameter is called
     * @param graphics It's how graphics parameter is called
     * @return Client's decision
     */
    public String insertYourParameters(String address, String port, String graphics) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(" Write server's address or \"close game\": \n");
            String s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            ifNonLocalhostAddress(s);
            Client.getParams().put(address, s);
            System.out.println(" Write server's port or \"close game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            try {
                int num = Integer.parseInt(s);
                Client.getParams().put(port, Integer.toString(num));
            } catch (NumberFormatException e) {
                wrongInsertPort();
                continue;
            }
            System.out.println(" Write \"cli\" or \"gui\" or \"close game\": \n");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("cli"))
                Client.getParams().put(graphics, "cli");
            else if (s.equals("gui"))
                Client.getParams().put(graphics, "gui");
            else if (s.equals("close game"))
                return s;
            else {
                wrongInsertGraphics();
                continue;
            }

            return "true";
        }
    }

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    public String chooseNickname() { //TODO
        return null;
    }

    /**
     * Method used to ask player if he wants to use advanced rules or not
     *
     * @return Player's choice
     */
    public String requestAdvancedRules() { //TODO
        return null;
    }

    /**
     * Method used to ask player the total players of the game he wants to join in
     *
     * @return Player's choice
     */
    public String requestNumPlayers() { //TODO
        return null;
    }

    /**
     * Method used to ask which assistant player want to use
     *
     * @return The chosen assistant
     */
    @Override
    public int askAssistant() {
        return 0;
    }

    /**
     * Method used to tell player possible commands
     */
    @Override
    public void possibleChoices() {

    }

    /**
     * @return Player's command at any time
     */
    @Override
    public String takeInput() {
        return null;
    }

    /**
     * Tell player this input isn't ok for now
     */
    @Override
    public void impossibleInputForNow() {

    }

    /**
     * Ask player which students want to move and where
     *
     * @return HashMap with responses of the player
     */
    @Override
    public HashMap<String, String> askStudents() {
        return null;
    }

    /**
     * @return Where mother nature has to go
     */
    @Override
    public int askMotherNature() {
        return 0;
    }

    /**
     * @return which cloud player has chosen to take
     */
    @Override
    public String askCloud() {
        return null;
    }

    /**
     * Tell the player it's his turn
     */
    @Override
    public void yourTurn() {

    }

    /**
     * @param nick nickname of player who has to play the current turn
     */
    @Override
    public void hisTurn(String nick) {

    }

    /**
     * Method used to tell a player he has to play the assistant card
     */
    @Override
    public void mustPlayAssistant() {

    }

    /**
     * Method used to tell the player he is waiting for the match
     */
    @Override
    public void waitingMatch() {

    }

    /**
     * Method to tell the player the game has begun
     */
    @Override
    public void gameStarted() {

    }

    /**
     * @param nick the winner player
     */
    @Override
    public void printWinner(String nick) {

    }

    /**
     * Method used to ask a player which character he wants to play
     */
    @Override
    public void askCharacter() {

    }

    /**
     * Method used when an error message come from server
     */
    @Override
    public void impossibleAssistant() {

    }

    /**
     * Method used when an error message come from server
     */
    @Override
    public void impossibleStudents() {

    }

    /**
     * Method used when an error message come from server
     */
    @Override
    public void impossibleMotherNature() {

    }

    /**
     * Method used when an error message come from server
     */
    @Override
    public void impossibleCloud() {

    }

    /**
     * Method used when an error message come from server
     */
    @Override
    public void impossibleCharacter() {

    }

    /**
     * This function draw the current status of the table: islands and boards
     */
    @Override
    public void showTable() {

    }

    /**
     * This function print the view of a player's status: his last assistant and board
     *
     * @param player the players to show status of
     */
    @Override
    public void showPlayerStatus(Player player) {

    }

    @Override
    public void showDeck(Player player) {

    }
}
