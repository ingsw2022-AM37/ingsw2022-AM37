package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.client.gui.GuiApp;
import it.polimi.ingsw.am37.client.gui.SceneController;
import it.polimi.ingsw.am37.client.gui.controller.ChooseNumStudentsController;
import it.polimi.ingsw.am37.client.gui.controller.ConnectionController;
import it.polimi.ingsw.am37.client.gui.controller.EnterInGameController;
import it.polimi.ingsw.am37.client.gui.controller.GameSceneController;
import it.polimi.ingsw.am37.client.gui.observer.GuiObserver;
import it.polimi.ingsw.am37.message.UpdateMessage;
import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("ALL")
public class GuiView extends AbstractView {
    private final GuiApp app;

    private final GuiObserver observer;

    /**
     * Method used to ask which assistant player want to use
     *
     * @param client the client to get the nickname of the current player
     * @return The chosen assistant
     */
    @Override
    public int askAssistant(Client client) {
        while (observer.getLastClickedObject() != GuiObserver.ClickableObjectType.CO_ASSISTANT) {
            System.err.println("Unexpected object clicked: " + observer.getLastClickedObject());
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Integer.parseInt(observer.getLastRetrievedObjectId());
    }

    public GuiView() {
        observer = new GuiObserver();
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
        while (observer.getLastClickedObject() != GuiObserver.ClickableObjectType.CO_CHARACTER) {
            System.err.println("Unexpected object clicked: " + observer.getLastClickedObject());
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Effect.valueOf(observer.getLastRetrievedObjectId());
    }

    /**
     * @return which cloud player has chosen to take
     */
    @Override
    public String askCloud() {
        while (observer.getLastClickedObject() != GuiObserver.ClickableObjectType.CO_CLOUD) {
            System.err.println("Unexpected object clicked: " + observer.getLastClickedObject());
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return observer.getLastRetrievedObjectId();
    }

    @Override
    public boolean askDestination() {
        GuiObserver.ClickableObjectType objectType = null;
        try {
            objectType = observer.nextClickedObjectType();
            while (objectType != GuiObserver.ClickableObjectType.CO_ISLAND &&
                    objectType != GuiObserver.ClickableObjectType.CO_DINING) {
                objectType = observer.nextClickedObjectType();
            }
            if (objectType == GuiObserver.ClickableObjectType.CO_ISLAND) return true;
            else if (objectType == GuiObserver.ClickableObjectType.CO_DINING) return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Should not reach this point");
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

    /**
     * @param assistant the assistant chosen by the players
     * @return Where mother nature has to go
     */
    @Override
    public int askMotherNature(Assistant assistant) {
        while (true) {
            try {
                if (observer.nextClickedObjectType() == GuiObserver.ClickableObjectType.CO_ISLAND) break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Integer.parseInt(observer.getLastRetrievedObjectId());
    }

    @Override
    public Client.LobbyParameters askLobbyParameters() {
        return ((EnterInGameController) SceneController.getActiveController()).getLobbyParameters();
    }

    @Override
    public int askIsland() {
        return Integer.parseInt(observer.getLastRetrievedObjectId());
    }

    /**
     * Method used to ask which player you want to look at
     *
     * @return chosen player
     */
    @Override
    public Player askPlayer() {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * Show all the character of this match
     */
    @Override
    public void showCharacters() {
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
        StudentsContainer container;
        while (true) {
            int studentsToMove = (num == 0 ? (GameManager.MAX_FOR_MOVEMENTS - client.getTotalStudentsInTurn()) : num);
            container = askStudents(studentsToMove, reducedModel.getBoards().get(client.getNickname()).getEntrance());
            if (num == 0 ? container.size() > studentsToMove : container.size() != num) {
                displayError(client.getMessageString("e.toManyStudents"));
                continue;
            }
            if (num == 0) {
                client.addTotalStudentsInTurn(container.size());
                if (client.getTotalStudentsInTurn() == GameManager.MAX_FOR_MOVEMENTS ||
                        !askConfirm("Do you want to move more students?")) break;
            } else if (container.size() == num) break;
        }
        return container;
    }

    /**
     * @param client the client to get the status of the current player
     * @return Player's command at any time
     */
    @Override
    public ActionType takeInput(Client client) {
        List<ActionType> availableActions = ActionType.getActions();
        ActionType value;
        while (true) {
            try {
                GuiObserver.ClickableObjectType objectType = observer.nextClickedObjectType();
                value = switch (objectType) {
                    case CO_ENTRANCE -> ActionType.MOVE_STUDENTS_UNDEFINED;
                    case CO_ASSISTANT -> ActionType.PLAY_ASSISTANT;
                    case CO_MOTHER_NATURE -> ActionType.MOVE_MOTHER_NATURE;
                    case CO_CHARACTER -> ActionType.PLAY_CHARACTER;
                    case CO_CLOUD -> ActionType.CHOOSE_CLOUD;
                    default -> null;
                };
                if (value != null && availableActions.contains(value)) break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }

    /**
     * Method used to ask a nickname
     *
     * @return The chosen nickname
     */
    @Override
    public String askNickname() {
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
        Platform.runLater(() -> {
            SceneController.switchScreen("/assets/scenes/GameScene.fxml");
            ((GameSceneController) SceneController.getActiveController()).registerListener(observer);
        });
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
     * Method used to display connection info
     *
     * @param client the client to show info about
     */
    @Override
    public void showConnection(Client client) {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * Method used to display the assistant deck of a player
     *
     * @param player the player to show the deck of
     */
    @Override
    public void showDeck(Player player) {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * Method used to display the last Assistant played except the client's one
     *
     * @param players      the players to show the last assistant played
     * @param playerToSkip the player to skip
     */
    @Override
    public void showLastAssistantPlayed(Collection<Player> players, Player playerToSkip) {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * This function print the view of a player's status: his last assistant and board
     *
     * @param player        the players to show status of
     * @param advancedRules if the advanced rules are enabled
     */
    @Override
    public void showPlayerStatus(Player player, boolean advancedRules) {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * Method used to show the nickname of all players in this match
     */
    @Override
    public void showPlayersNicknames() {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * This function draw the current status of the table: islands and boards
     */
    @Override
    public void showTable() {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * Method used to show where mother nature can go
     *
     * @param assistant the assistant to know how many steps can mother nature take
     */
    @Override
    public void showPossibleIslandDestination(Assistant assistant) {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    @Override
    public void updateView(UpdateMessage updateMessage, Client client) {
        reducedModel.update(updateMessage.getUpdatedObjects().values().stream().flatMap(List::stream).toList());
        if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.ISLAND) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.ISLAND).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawIslands(reducedModel.getIslands()));
        }
        if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CLOUD) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CLOUD).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawClouds(reducedModel.getClouds()
                    .values()
                    .stream()
                    .toList()));
        }
        if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawPlayedAssistants(reducedModel.getPlayers()
                    .values()
                    .stream()
                    .toList()));
            if (updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER)
                    .stream()
                    .anyMatch(p -> Objects.equals(((Player) p).getPlayerId(), client.getNickname()))) {
                Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawDeck(reducedModel.getPlayers()
                        .get(client.getNickname())
                        .getAssistantsDeck()
                        .values()
                        .stream()
                        .toList()));

                //Write number of coins
                if (client.getSettings().advancedRulesEnabled())
                    Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).changeCoins(reducedModel.getPlayers()
                            .get(client.getNickname())
                            .getNumberOfCoins()));

                //Start update my board -----------------------------
                HashMap<FactionColor, Integer> entrance = new HashMap<>();
                HashMap<FactionColor, Integer> dining = new HashMap<>();
                boolean[] professors;
                LimitedTowerContainer towers;

                for (FactionColor color : FactionColor.values()) {
                    entrance.put(color, reducedModel.getBoards()
                            .get(client.getNickname())
                            .getEntrance()
                            .getByColor(color));
                    dining.put(color, reducedModel.getBoards()
                            .get(client.getNickname())
                            .getDiningRoom()
                            .getByColor(color));
                }
                professors = reducedModel.getBoards().get(client.getNickname()).getProfTable();
                towers = reducedModel.getBoards().get(client.getNickname()).getTowers();

                Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawBoard(entrance, dining, professors, towers));
                //My board drawn ------------------------------
            }
        }
        if (client.getSettings().advancedRulesEnabled() &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CHARACTER) != null &&
                updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CHARACTER).size() != 0) {
            Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawCharacters(updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.CHARACTER)
                    .stream()
                    .map(o -> (Character) o)
                    .toList()));
        }
    }

    /**
     * Generic notification of an input error
     */
    @Override
    public void wrongInsert() {
        throw new IllegalStateException("Only CLI method called in GUI");
    }

    /**
     * Notify when a number port is expected but another input was given
     */
    @Override
    public void wrongInsertPort() {
        displayError("You haven't written a number as server's port");
    }

    /**
     * Tell the player it's his turn
     */
    @Override
    public void yourTurn() {
        displayImportant("It's your turn");
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

    /**
     * This function pop up a container
     *
     * @param container
     * @param studentsToMove
     */
    private StudentsContainer askStudents(int studentsToMove, StudentsContainer sourceContainer) {
        AtomicReference<StudentsContainer> atomicContainer =
                new AtomicReference<>(new FixedUnlimitedStudentsContainer());
        displayInfo("You have to move " + studentsToMove + (studentsToMove == 1 ? " student" : " students") +
                " in this turn");
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage studentsDialog = new Stage();
            studentsDialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/scenes/ChooseNumStudents.fxml"));
            try {
                studentsDialog.setScene(new Scene(loader.load()));
                ChooseNumStudentsController controller = loader.getController();
                controller.setSourceContainer(sourceContainer);
                studentsDialog.showAndWait();
                atomicContainer.set(controller.getStudents());
                latch.countDown();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return atomicContainer.get();
    }
}
