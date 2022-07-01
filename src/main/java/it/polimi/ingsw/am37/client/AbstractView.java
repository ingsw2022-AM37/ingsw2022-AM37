package it.polimi.ingsw.am37.client;


import it.polimi.ingsw.am37.message.UpdateMessage;
import it.polimi.ingsw.am37.model.Assistant;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * Class with all abstract methods for printing or displaying images
 */
public abstract class AbstractView {
    /**
     * Model in client
     */
    protected final ReducedModel reducedModel;

    /**
     * used to save properties
     */
    protected final Properties messagesConstants;

    /**
     * Default constructor
     */
    public AbstractView() {
        this.reducedModel = new ReducedModel();
        messagesConstants = new Properties();
        try {
            messagesConstants.load(Client.class.getResourceAsStream("/messages.properties"));
        } catch (IOException e) {
            System.err.println("Unable to find messages file");
        }
    }

    /**
     * Method used to ask which assistant player want to use
     *
     * @param client the client to get the nickname of the current player
     * @return The chosen assistant
     */
    public abstract int askAssistant(Client client);

    /**
     * Method used to ask a player which character he wants to play
     *
     * @return the effect of the chosen character
     */
    public abstract Effect askCharacter();

    /**
     * Method used to choose to pick students on cloud
     * @return which cloud player has chosen to take
     */
    public abstract String askCloud();

    /**
     * Method used to ask user to confirm at the provided message
     *
     * @param message a string containing the text to express confirm or negation
     * @return Client's response
     */
    public abstract Boolean askConfirm(String message);

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his
     * parameters
     *
     * @return Client's provided parameters
     */
    public abstract Client.ConnectionParameters askConnectionParameters();

    /**
     *
     * @return if dining or island is selected as next move
     */
    public abstract boolean askDestination();

    /**
     * Method to ask lobby parameters
     * @return chosen lobby parameters
     */
    public abstract Client.LobbyParameters askLobbyParameters();

    /**
     * method to select a specific island
     * @return island id which is selected
     */
    public abstract int askIsland();

    /**
     * Method to ask movement of Mother Nature
     * @param assistant the assistant chosen by the player
     * @return Where mother nature has to go
     */
    public abstract int askMotherNature(Assistant assistant);

    /**
     * Method used to ask which player you want to look at
     *
     * @return chosen player
     */
    public abstract Player askPlayer();

    /**
     * Method used for asking student color to player
     * @param client client who is playing this instruction
     * @return chosen color
     */
    public abstract FactionColor askColor(Client client);

    /**
     * Method used to select students from dining
     * @param client client who is playing this instruction
     * @param num number of students you want to select in dining
     * @return chosen students
     */
    public abstract StudentsContainer askStudentFromDining(Client client, int num);


    /**
     *
     * @param character character played
     * @param num max number of possible students to choose, it depends on the character
     * @param client player who is playing the card
     * @return chosen students
     */
    public abstract StudentsContainer askStudentsFromCharacter(Character character, int num, Client client);

    /**
     * Ask player which students want to move and where
     *
     * @param client the client to get the status of the current player
     * @param num    the num of students to move; if 0 regular behaviour is followed
     * @return HashMap with responses of the player
     */
    public abstract StudentsContainer askStudentsFromEntrance(Client client, int num);

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    public abstract String askNickname();

    /**
     * Method to tell the player the game has begun
     */
    public abstract void gameStarted();

    /**
     * @return the reduced model of the view
     */
    public ReducedModel getReducedModel() {
        return reducedModel;
    }

    /**
     * Show to the view that now is the turn of the provided user's nickname
     *
     * @param nick nickname of player who has to play the current turn
     */
    public abstract void hisTurn(String nick);

    /**
     * Method to notify if client or server has lost the connection
     */
    public void notifyInternetCrash() {
        displayError("Game has lost the connection");
    }

    /**
     * @param nick the winner player
     */
    public abstract void printWinner(String nick);

    /**
     * Show all the character of this match
     */
    public abstract void showCharacters();

    /**
     * Method used to display connection info
     *
     * @param client the client to show info about
     */
    public abstract void showConnection(Client client);

    /**
     * Method used to display the assistant deck of a player
     *
     * @param player the player to show the deck of
     */
    public abstract void showDeck(Player player);

    /**
     * Method used to display the last Assistant played except the client's one
     *
     * @param players      the players to show the last assistant played
     * @param playerToSkip the player to skip
     */
    public abstract void showLastAssistantPlayed(Collection<Player> players, Player playerToSkip);

    /**
     * This function print the view of a player's status: his last assistant and board
     *
     * @param player        the players to show status of
     * @param advancedRules if the advanced rules are enabled or not
     */
    public abstract void showPlayerStatus(Player player, boolean advancedRules);

    /**
     * Method used to show the nickname of all players in this match
     */
    public abstract void showPlayersNicknames();

    /**
     * This function draw the current status of the table: islands and boards
     */
    public abstract void showTable();

    /**
     * Method used to show where mother nature can go
     *
     * @param assistant the assistant to know how many steps can mother nature take
     */
    public abstract void showPossibleIslandDestination(Assistant assistant);

    /**
     * @param client the client to get the status of the current player
     * @return Player's command at any time
     */
    public abstract ActionType takeInput(Client client);

    /**
     * Generic notification of an input error
     */
    public abstract void wrongInsert();

    /**
     * Notify when a number port is expected but another input was given
     */
    public abstract void wrongInsertPort();

    /**
     * Notify when requested server is unreachable
     */
    public void wrongServer() {
        displayError("This server is unreachable");
    }

    /**
     * Tell the player it's his turn
     */
    public abstract void yourTurn();

    /**
     *
     * @param message string needed to be displayed
     */
    public abstract void displayInfo(String message);

    /**
     *
     * @param message message to be displayed
     */
    public abstract void displayImportant(String message);

    /**
     *
     * @param message error to be displayed
     */
    public abstract void displayError(String message);

    /**
     * Method to update the view
     * @param updateMessage message containing new information
     * @param client client who has the message
     */
    public abstract void updateView(UpdateMessage updateMessage, Client client);

    /**
     * Ask player a number among displayed ones
     * @param choices ArrayList with possible numbers for selection
     * @return chosen number
     */
    public abstract int askStudentsNumber(ArrayList<Integer> choices);
}
