package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.client.gui.GuiApp;
import it.polimi.ingsw.am37.client.gui.SceneController;
import it.polimi.ingsw.am37.client.gui.controller.ConnectionController;
import it.polimi.ingsw.am37.client.gui.controller.EnterInGameController;
import it.polimi.ingsw.am37.client.gui.controller.GameSceneController;
import it.polimi.ingsw.am37.message.UpdateMessage;
import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import javafx.application.Application;
import javafx.application.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GuiView extends AbstractView {
    private final GuiApp app;

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

    public GuiView() {
        new Thread(() -> Application.launch(GuiApp.class)).start();
        app = GuiApp.waitForStartUp();
    }

    /**
     * Method used to ask a player which character he wants to play
     *
     * @return the effect of the select character
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
        return false;
    }

    /**
     * Method used if player decided to don't use default setting for connection, so he will be asked to insert his
     * parameters
     *
     * @return Client's provided parameters
     */
    @Override
    public Client.ConnectionParameters askConnectionParameters() {
        synchronized (SceneController.waitObject) {
            try {
                SceneController.waitObject.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        return ((ConnectionController) SceneController.getActiveController()).getConnectionParameters();
    }

    @Override
    public Client.LobbyParameters askLobbyParameters() {

        return ((EnterInGameController) SceneController.getActiveController()).getLobbyParameters();
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

    @Override
    public FactionColor askColor(Client client) {
        return null;
    }

    @Override
    public StudentsContainer askStudentFromDining(Client client, int num) {
        return null;
    }

    @Override
    public StudentsContainer askStudentsFromCharacter(Character character, int num, Client client) {
        return null;
    }

    /**
     * Ask player which students want to move and where
     *
     * @param client the client to get the status of the current player
     * @param num    the num of students to move; put 0 for normal 3 students movements
     * @return HashMap with responses of the player
     */
    @Override
    public StudentsContainer askStudentsFromEntrance(Client client, int num) {
        return null;
    }

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    @Override
    public String chooseNickname() {
        Platform.runLater(() -> SceneController.switchScreen("/assets/scenes/EnterInGame.fxml"));
        synchronized (SceneController.waitObject) {
            try {
                SceneController.waitObject.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return ((EnterInGameController) SceneController.getActiveController()).getNickname();
    }

    /**
     * Method to tell the player the game has begun
     */
    @Override
    public void gameStarted() {
        Platform.runLater(() -> SceneController.switchScreen("/assets/scenes/GameScene.fxml"));
    }

    /**
     * Show to the view that now is the turn of the provided user's nickname
     *
     * @param nick nickname of player who has to play the current turn
     */
    @Override
    public void hisTurn(String nick) {
        displayImportant("It's " + nick + "'s turn");
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
     * @param player        the players to show status of
     * @param advancedRules if the advanced rules are enabled
     */
    @Override
    public void showPlayerStatus(Player player, boolean advancedRules) {

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
        synchronized (SceneController.waitObject) {
            try {
                SceneController.waitObject.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
        displayError("You haven't written a number as server's port");
    }

    /**
     * Notify when requested server is unreachable
     */
    public void wrongServer() {
        displayError("This server is unreachable");
    }

    /**
     * Tell the player it's his turn
     */
    @Override
    public void yourTurn() {

    }

    @Override
    public void displayInfo(String message) {
        Platform.runLater(() -> SceneController.getActiveController().showInfo(message));
    }

    @Override
    public void displayImportant(String message) {
        Platform.runLater(() -> SceneController.getActiveController().showImportant(message));
    }

    @Override
    public void displayError(String message) {
        Platform.runLater(() -> SceneController.getActiveController().showError(message));
    }

    @Override
    public void updateView(UpdateMessage updateMessage, Client client) {
        reducedModel.update(updateMessage.getUpdatedObjects()
                .values()
                .stream()
                .flatMap(List::stream)
                .toList());
        if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.ISLAND) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.ISLAND).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawIslands(updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.ISLAND)
                    .stream()
                    .map(o -> (Island) o)
                    .toList()));
        }
        if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CLOUD) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CLOUD).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawClouds(updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CLOUD)
                    .stream()
                    .map(o -> (Cloud) o)
                    .toList()));
        }
        if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawPlayedAssistants(updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER)
                    .stream()
                    .map(o -> (Player) o)
                    .toList()));
            if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER)
                    .stream()
                    .anyMatch(p -> Objects.equals(((Player) p).getPlayerId(), client.getNickname()))) {
                Player player = (Player) updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER)
                        .stream()
                        .filter(p -> Objects.equals(((Player) p).getPlayerId(), client.getNickname()))
                        .findFirst()
                        .get();
                List<Assistant> assistants = player.getAssistantsDeck().values().stream().toList();
                Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawDeck(assistants));

                //Start update my board -----------------------------
                HashMap<FactionColor, Integer> entrance = new HashMap<>();
                HashMap<FactionColor, Integer> dining = new HashMap<>();
                boolean[] professors;
                LimitedTowerContainer towers;

                for (FactionColor color : FactionColor.values()) {
                    entrance.put(color, getReducedModel().getBoards().get(client.getNickname()).getEntrance().getByColor(color));
                    dining.put(color, getReducedModel().getBoards().get(client.getNickname()).getDiningRoom().getByColor(color));
                }
                professors = getReducedModel().getBoards().get(client.getNickname()).getProfTable();
                towers = getReducedModel().getBoards().get(client.getNickname()).getTowers();

                Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawBoard(entrance, dining, professors, towers));
                //My board drawn ------------------------------
            }
        }
        if (client.getSettings().advancedRulesEnabled() && updateMessage.getUpdatedObjects(UpdatableObject
                .UpdatableType.CHARACTER) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CHARACTER).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawCharacters
                    (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CHARACTER)
                            .stream()
                            .map(o -> (Character) o)
                            .toList()));
        }
    }
}
