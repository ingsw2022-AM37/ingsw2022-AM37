package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.client.gui.GuiApp;
import it.polimi.ingsw.am37.client.gui.SceneController;
import it.polimi.ingsw.am37.client.gui.controller.*;
import it.polimi.ingsw.am37.client.gui.observer.GuiObserver;
import it.polimi.ingsw.am37.message.UpdateMessage;
import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.student_container.FixedUnlimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class having methods to update graphical view
 */
public class GuiView extends AbstractView {

    /**
     * needed for graphics
     */
    private final GuiApp app;

    /**
     * Observer used for tracking player choices
     */
    private final GuiObserver observer;

    /**
     * Method used to ask which assistant player want to use
     *
     * @param client the client to get the nickname of the current player
     * @return The chosen assistant
     */
    @Override
    public int askAssistant(Client client) {
        while (observer.getLastClickedObjectType() != GuiObserver.ClickableObjectType.CO_ASSISTANT) {
            System.err.println("Unexpected object clicked: " + observer.getLastClickedObjectType());
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Integer.parseInt(observer.getLastRetrievedObjectId());
    }

    /**
     * Default constructor
     */
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
        while (observer.getLastClickedObjectType() != GuiObserver.ClickableObjectType.CO_CHARACTER) {
            System.err.println("Unexpected object clicked: " + observer.getLastClickedObjectType());
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Effect.valueOf(observer.getLastRetrievedObjectId());
    }

    /**
     * * Method used to choose to pick students on cloud
     * @return which cloud player has chosen to take
     */
    @Override
    public String askCloud() {
        while (observer.getLastClickedObjectType() != GuiObserver.ClickableObjectType.CO_CLOUD) {
            System.err.println("Unexpected object clicked: " + observer.getLastClickedObjectType());
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return observer.getLastRetrievedObjectId();
    }

    /**
     *
     * @return if dining or island is selected as next move
     */
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

    /**
     * Method to ask lobby parameters
     * @return chosen lobby parameters
     */
    @Override
    public Client.LobbyParameters askLobbyParameters() {
        return ((EnterInGameController) SceneController.getActiveController()).getLobbyParameters();
    }

    /**
     * method to select a specific island
     * @return island id which is selected
     */
    @Override
    public int askIsland() {
        while (observer.getLastClickedObjectType() != GuiObserver.ClickableObjectType.CO_ISLAND) {
            try {
                observer.nextClickedObjectType();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
     * Method used for asking student color to player
     * @param client client who is playing this instruction
     * @return chosen color
     */
    @Override
    public FactionColor askColor(Client client) {
        AtomicReference<FactionColor> color = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage colorDialog = new Stage();
            colorDialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/scenes/ChooseColor.fxml"));
            try {
                colorDialog.setScene(new Scene(loader.load()));
                ChooseColorController controller = loader.getController();
                colorDialog.showAndWait();
                color.set(controller.getColor());
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
        return color.get();
    }

    /**
     * Method used to select students from dining
     * @param client client who is playing this instruction
     * @param num number of students you want to select in dining
     * @return chosen students
     */
    @Override
    public StudentsContainer askStudentFromDining(Client client, int num) {
        StudentsContainer container;
        while (true) {
            container = askStudents(num, reducedModel.getBoards().get(client.getNickname()).getDiningRoom());
            if (container == null) return null;
            if (container.size() != num) {
                displayError(client.getMessageString("e.toManyStudents"));
                continue;
            } else break;
        }
        return container;
    }

    /**
     *
     * @param character character played
     * @param num max number of possible students to choose, it depends on the character
     * @param client player who is playing the card
     * @return chosen students
     */
    @Override
    public StudentsContainer askStudentsFromCharacter(Character character, int num, Client client) {
        StudentsContainer container;
        while (true) {
            container = askStudents(num, character.getState().getContainer());
            if (container == null) return null;
            if (container.size() != num) {
                displayError(client.getMessageString("e.toManyStudents"));
                continue;
            } else break;
        }
        return container;
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
            int studentsToMove = (num == 0 ? (GameManager.MAX_FOR_MOVEMENTS[client.getSettings().lobbySize() % 2] -
                    client.getTotalStudentsInTurn()) : num);
            container = askStudents(studentsToMove, reducedModel.getBoards().get(client.getNickname()).getEntrance());
            if (container == null) return null;
            if (num == 0 ? container.size() > studentsToMove : container.size() != num) {
                displayError(client.getMessageString("e.toManyStudents"));
                continue;
            }
            if (num == 0) {
                client.addTotalStudentsInTurn(container.size());
                if (client.getTotalStudentsInTurn() ==
                        GameManager.MAX_FOR_MOVEMENTS[client.getSettings().lobbySize() % 2] ||
                        !askConfirm("Do you want to move more students?")) break;
            } else if (container.size() == num) break;
        }
        return container;
    }

    /**
     * Show all the character of this match
     */
    @Override
    public void showCharacters() {

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

    @Override
    public void printWinner(String nick) {
        displayImportant(nick.toUpperCase() + " has won the game!\n" + "Thank you for playing!\n" +
                "Game is closing in about 30 seconds");
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

    /**
     * Method to update the view
     * @param updateMessage message containing new information
     * @param client client who has the message
     */
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
                Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawOthersBoard(updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER)
                        .stream()
                        .filter(p -> !Objects.equals(((Player) p).getPlayerId(), client.getNickname()))
                        .map(p -> (Player) p)
                        .toList()));
            } else {
                Platform.runLater(() -> ((GameSceneController) SceneController.getActiveController()).drawOthersBoard(updateMessage.getUpdatedObjects(UpdatableObject.UpdatableType.PLAYER)
                        .stream()
                        .map(p -> (Player) p)
                        .toList()));
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

    /**
     *
     * @param message string needed to be displayed
     */
    @Override
    public void displayInfo(String message) {
        Platform.runLater(() -> {
            try {
                SceneController.getActiveController().showInfo(message);
            }catch (NullPointerException e){
                ;
            }
        });
    }
    /**
     *
     * @param message message to be displayed
     */
    @Override
    public void displayImportant(String message) {
        Platform.runLater(() -> SceneController.getActiveController().showImportant(message));
    }

    /**
     *
     * @param message error to be displayed
     */
    @Override
    public void displayError(String message) {
        Platform.runLater(() -> SceneController.getActiveController().showError(message));
    }

    /**
     * This functions display a modal window to let the user input some students with spinner field. The spinners are
     * limited with the student contained in the provided container
     *
     * @param studentsToMove
     */
    private StudentsContainer askStudents(int studentsToMove, StudentsContainer sourceContainer) {
        AtomicReference<StudentsContainer> atomicContainer =
                new AtomicReference<>(new LimitedStudentsContainer(120));
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage studentsDialog = new Stage();
            studentsDialog.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/scenes/ChooseNumStudents.fxml"));
            try {
                studentsDialog.setScene(new Scene(loader.load()));
                ChooseNumStudentsController controller = loader.getController();
                controller.setSourceContainer(sourceContainer, studentsToMove);
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

    /**
     * Show a dialog button to choose a number between multiple possibilities; if user close the dialog a new one reopen
     * until the user click on OK button to correctly close one
     *
     * @param choices the choices available
     * @return the number choose by the user
     */
    @Override
    public int askStudentsNumber(ArrayList<Integer> choices) {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setHeaderText(null);
        dialog.setContentText("How many students would you like to move?");
        do {
            dialog.showAndWait();
        } while (dialog.getSelectedItem() == null);
        return dialog.getSelectedItem();
    }
}
