package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.model.*;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Option;
import it.polimi.ingsw.am37.model.exceptions.AssistantImpossibleToPlay;
import it.polimi.ingsw.am37.model.exceptions.CharacterImpossibleToPlay;
import it.polimi.ingsw.am37.model.exceptions.MNmovementWrongException;
import it.polimi.ingsw.am37.model.exceptions.StudentSpaceException;
import it.polimi.ingsw.am37.network.MessageReceiver;
import it.polimi.ingsw.am37.network.exceptions.InternetException;
import it.polimi.ingsw.am37.network.server.ClientHandler;
import it.polimi.ingsw.am37.network.server.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * It represents the in game Lobby
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class Lobby implements Runnable, MessageReceiver {
    /**
     * A Logger.
     */
    private static Logger LOGGER;

    /**
     * The unique ID of the match.
     */
    private final int matchID;

    /**
     * It saves how many Players can enter the lobby.
     */
    private final int lobbySize;

    /**
     * It represents if the lobby is set to advanced mode or not.
     */
    private final boolean advancedMode;

    /**
     * If the lobby is full the game is ready.
     */
    private boolean isGameReady;

    /**
     * It represents the client connected in the lobby and therefore the Players.
     */
    private final HashMap<String, ClientHandler> players;

    /**
     * Keeps track of the disconnected clients
     */
    private final HashMap<String, ClientHandler> disconnectedPlayers;

    /**
     * exposed model
     */
    private final GameManager gameManager;

    /**
     * A controller that observe what is changed in the model and stores it.
     */
    private final UpdateController updateController;

    /**
     * It represents the clientHandlers and theirs nickname
     */
    private final HashMap<String, String> playerNicknames;

    /**
     * Keeps track of the number of Students moved.
     */
    private int numberOfStudentsMoved;

    /**
     * It represents the timer that starts when there's only one Player in the Lobby.
     */
    private final Timer endGameTimer;


    /**
     * Default constructor.
     */
    public Lobby(int lobbySize, boolean advancedMode, int matchID) {
        LOGGER = LogManager.getLogger(Lobby.class);
        this.lobbySize = lobbySize;
        this.advancedMode = advancedMode;
        this.players = new HashMap<>();
        this.playerNicknames = new HashMap<>();
        this.isGameReady = false;
        this.gameManager = new GameManager(lobbySize, advancedMode);
        this.matchID = matchID;
        this.updateController = new UpdateController();
        this.disconnectedPlayers = new HashMap<>();
        this.endGameTimer = new Timer();
        numberOfStudentsMoved = 0;
    }

    /**
     * @return true if the player is found, otherwise false.
     */
    public boolean isPlayerInLobby(String UUID) {
        return players.containsKey(UUID) || disconnectedPlayers.containsKey(UUID);
    }

    /**
     * @return True if the Lobby is full otherwise False
     */
    private boolean isFull() {
        return players.size() == lobbySize;
    }

    /**
     * @return the players connected in the lobby
     */
    public HashMap<String, ClientHandler> getPlayers() {
        return players;
    }

    /**
     * @return the players nickname in the lobby
     */
    public HashMap<String, String> getPlayerNicknames() {
        return playerNicknames;
    }

    /**
     * @return the flag that saves if the Lobby is ready to start the game.
     */
    public boolean isGameReady() {
        return isGameReady;
    }

    /**
     * @return the Lobby size.
     */
    public int getLobbySize() {
        return lobbySize;
    }

    /**
     * @return the unique id of the match.
     */
    public int getMatchID() {
        return matchID;
    }

    /**
     * @return the flag that states if the Lobby is in advanced mode.
     */
    public boolean isAdvancedMode() {
        return advancedMode;
    }

    /**
     * resets the variables needed
     */
    private void reset() {
        if (advancedMode)
            for (int i = 0; i < gameManager.getCharacters().length - 1; i++) {
                gameManager.getCharacters()[i].setPlayedInThisTurn(false);
            }
        numberOfStudentsMoved = 0;
    }

    /**
     * Find the UUID associated to the given username in the current registered uid - username map
     *
     * @param username the username to find the UUID
     * @return the UUID associated
     */
    private String findUUIDByUsername(String username) {
        return playerNicknames.entrySet().stream().filter(entry -> Objects.equals(entry.getValue(), username)).findFirst().get().getKey();
    }

    /**
     * Runs the thread.
     */
    @Override
    public synchronized void run() {
        while (true) {
            if (isGameReady()) {
                startGame();
                break;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Starts the game and notifies the client
     */
    private void startGame() {
        LOGGER.info("[Lobby " + matchID + "] Everything is ready, game is about to start");
        Timer timer = new Timer();
        gameManager.prepareGame();
        reset();
        gameManager.registerListener(updateController);
        int i = 0;
        for (String nickname : playerNicknames.values()) {
            gameManager.getTurnManager().getPlayers().get(i).setPlayerId(nickname);
            i++;
        }
        sendMessage(new StartGameMessage());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new UpdateMessage(updateController.getUpdatedObjects(), MessageType.START_GAME,
                        "StartGame"));
            }
        }, 100);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(new PlanningPhaseMessage(findUUIDByUsername(gameManager.getTurnManager()
                        .getCurrentPlayer()
                        .getPlayerId())));
            }
        }, 300);

    }

    /**
     * adds the player in the Lobby and checks if the Lobby is full
     *
     * @param ch the Client to be added.
     */
    public synchronized void addPlayerInLobby(String UUID, ClientHandler ch, String nickname) {
        players.put(UUID, ch);
        isGameReady = isFull();
        playerNicknames.put(UUID, nickname);
        LOGGER.info("[Lobby " + matchID + "] " + nickname + " joined the lobby");
        notifyAll();
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     */
    private void playAssistantCase(Message message) {
        Message response;
        //Refill clouds if is the first player of the round
        if (Objects.equals(findUUIDByUsername(gameManager.getTurnManager().getOrderPlayed().get(0).getPlayerId()), message.getUUID())) {
            for (Cloud c : gameManager.getClouds()) {
                c.addStudents(gameManager.getBag()
                        .extractStudents(c.getIsFor2()
                                ? c.getStudentsPerCloud2Players()
                                : c.getStudentsPerCloud3Players()));
            }
            LOGGER.debug("[Lobby " + matchID + "] Clouds Refilled! (he's the first player of the round)");
        }
        HashMap<Integer, Assistant> deck = gameManager.getTurnManager().getCurrentPlayer().getAssistantsDeck();
        try {
            gameManager.playAssistant(deck.get(((PlayAssistantMessage) message).getCardValue()));
            response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(), message.getMessageType().getClassName());
            sendMessage(response);
            if (isLastPlayerInOrder(message.getUUID())) {
                gameManager.nextTurn();
                response = new NextTurnMessage(findUUIDByUsername(gameManager.getTurnManager().getCurrentPlayer().getPlayerId()), gameManager.getTurnManager().getCurrentPlayer().getPlayerId());
            } else {
                response = new PlanningPhaseMessage(findUUIDByUsername(gameManager.getTurnManager().getCurrentPlayer().getPlayerId()));
            }
            sendMessage(response);
        } catch (AssistantImpossibleToPlay | IllegalArgumentException e) {
            LOGGER.error("[Lobby " + matchID + "] Assistant impossible to play");
            LOGGER.error("[Lobby " + matchID + "]\n" + e.getMessage());
            response = new ErrorMessage(message.getUUID(), e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
            LOGGER.error(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
            sendMessage(response);
            response = new PlanningPhaseMessage(findUUIDByUsername(gameManager.getTurnManager().getCurrentPlayer().getPlayerId()));
            sendMessage(response);
        }
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that called the method.
     */
    private void studentsToDiningCase(Message message, ClientHandler ch) {
        Message response;
        int students;
        int movableStudents = 3;
        students = ((StudentsToDiningMessage) message).getContainer().size();
        if (numberOfStudentsMoved + students <= movableStudents) {
            try {
                gameManager.moveStudentsToDining(((StudentsToDiningMessage) message).getContainer());
            } catch (IllegalArgumentException e) {
                response = new ErrorMessage(message.getUUID(), e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                LOGGER.error(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                sendMessage(response);
            }
            numberOfStudentsMoved += students;
            response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(), message.getMessageType().getClassName());
            sendMessage(response);
        } else
            ch.disconnect();
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that called the method.
     */
    private void studentsToIslandCase(Message message, ClientHandler ch) {
        Message response;
        int students;
        int movableStudents = 3;
        students = ((StudentsToIslandMessage) message).getContainer().size();
        if (numberOfStudentsMoved + students <= movableStudents) {
            try {
                gameManager.moveStudentsToIsland(((StudentsToIslandMessage) message).getContainer(),
                        ((StudentsToIslandMessage) message).getIslandId());
            } catch (IllegalArgumentException e) {
                response = new ErrorMessage(message.getUUID(), e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                LOGGER.error(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                sendMessage(response);
            }
            numberOfStudentsMoved += students;
            response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(), message.getMessageType().getClassName());
            sendMessage(response);
        } else
            ch.disconnect();
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that called the method.
     */
    private void moveMotherNatureCase(Message message, ClientHandler ch) {
        Message response;
        boolean exists = false;
        for (Island island : gameManager.getIslandsManager().getIslands()) {
            if (((MoveMotherNatureMessage) message).getIslandId() == island.getIslandId()) {
                exists = true;
                break;
            }
        }
        if (exists) {
            try {
                gameManager.moveMotherNature(((MoveMotherNatureMessage) message).getIslandId());
                response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(), message.getMessageType().getClassName());
                sendMessage(response);
            } catch (MNmovementWrongException e) {
                response = new ErrorMessage(message.getUUID(), e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                LOGGER.error(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                sendMessage(response);
            }
        } else
            ch.disconnect();
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that called the method.
     */
    private void playCharacterCase(Message message, ClientHandler ch) {
        Message response;
        boolean characterPlayable = false;
        int characterIndex;
        for (characterIndex = 0; characterIndex < gameManager.getCharacters().length; characterIndex++) {
            if (gameManager.getCharacters()[characterIndex].getEffectType() == ((PlayCharacterMessage) message).getChosenCharacter()) {
                if (!gameManager.getCharacters()[characterIndex].isPlayedInThisTurn()) {
                    characterPlayable = true;
                    break;
                }
            }
        }
        if (characterPlayable) {
            Character characterToBePlayed = Arrays.stream(gameManager.getCharacters())
                    .filter(c -> c.getEffectType() == ((PlayCharacterMessage) message).getChosenCharacter())
                    .findFirst()
                    .orElse(null);
            Option optionNeeded = ((PlayCharacterMessage) message).getOption(gameManager);
            try {
                if (optionNeeded == null || characterToBePlayed == null) throw new IllegalStateException();
                gameManager.playCharacter(characterToBePlayed, optionNeeded);
            } catch (CharacterImpossibleToPlay e) {
                response = new ErrorMessage(message.getUUID(), e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                LOGGER.error(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
                sendMessage(response);
            } catch (IllegalStateException e) {
                ch.disconnect();
                onDisconnect(ch.getUUID());
            }
            response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(),
                    message.getMessageType()
                            .getClassName());
            sendMessage(response);
            gameManager.getCharacters()[characterIndex].setPlayedInThisTurn(true);
        } else {
            response = new ErrorMessage(message.getUUID(), "Character not playable, already played in this turn");
            LOGGER.error("Character not playable, already played in this turn");
            sendMessage(response);
        }
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that called the method.
     */
    private void chooseCloudCase(Message message, ClientHandler ch) {
        Message response;
        try {
            gameManager.chooseCloud(((ChooseCloudMessage) message).getCloudId());
            reset();
            if (isLastPlayerInOrder(message.getUUID())) {
                gameManager.nextTurn();
                //Resets the last assistant played when a turn ends.
                gameManager.getTurnManager().getPlayers().forEach(player -> player.setLastAssistantPlayed(null));
                response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(), message.getMessageType().getClassName());
            } else {
                gameManager.getTurnManager().nextPlayer();
                response = new UpdateMessage(updateController.getUpdatedObjects(), message.getMessageType(), message.getMessageType().getClassName());
            }
            sendMessage(response);
        } catch (IllegalArgumentException | StudentSpaceException e) {
            response = new ErrorMessage(message.getUUID(), e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
            LOGGER.error(e.getMessage().substring(e.getMessage().indexOf(" ") + 1));
            sendMessage(response);
        }

        if (!ch.isConnectedToClient()) {
            ClientHandler newCh;
            do {
                onDisconnect(message.getUUID());
                gameManager.nextTurn();
                newCh = players.get(findUUIDByUsername(gameManager.getTurnManager().getCurrentPlayer().getPlayerId()));
            } while (!newCh.isConnectedToClient());
            ch = newCh;
        }
        if (ch.isConnectedToClient()) {
            if (!isLastPlayerInOrder(message.getUUID())) {
                response = new NextTurnMessage(findUUIDByUsername(gameManager.getTurnManager().getCurrentPlayer().getPlayerId()), gameManager.getTurnManager().getCurrentPlayer().getPlayerId());
            } else {
                response = new PlanningPhaseMessage(findUUIDByUsername(gameManager.getTurnManager().getCurrentPlayer().getPlayerId()));
                gameManager.getTurnManager().getAssistantPlayed().clear();
            }
        }
        sendMessage(response);
    }

    /**
     * @param UUID player id
     * @return true if the player is in the last that has to play
     */
    private boolean isLastPlayerInOrder(String UUID) {
        return Objects.equals(findUUIDByUsername(gameManager.getTurnManager().getOrderPlayed().get(gameManager.getTurnManager().getOrderPlayed().size() - 1).getPlayerId()), UUID);
    }

    /**
     * Reconnects the client in the lobby if it exists
     *
     * @param clientUUID the Client that wants to reconnect.
     */
    public void onReconnect(String clientUUID) {
        //Forces the addition of the player who has reconnected to the list orderPlayed in GM
        Message message;
        Player playerToAdd = gameManager.getTurnManager()
                .getPlayers()
                .stream()
                .filter(player -> findUUIDByUsername(player.getPlayerId()).equals(clientUUID))
                .findFirst()
                .orElseThrow();
        //TODO catch exception
        gameManager.getTurnManager().getOrderPlayed().add(playerToAdd);

        ClientHandler clientToReconnect = disconnectedPlayers.get(clientUUID);
        players.put(clientUUID, clientToReconnect);
        disconnectedPlayers.remove(clientUUID);
        endGameTimer.cancel();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss z");
        LocalDateTime date = LocalDateTime.now().plusMinutes(5);
        String expiring = date.format(formatter);
        message = new ResilienceMessage(false, playerNicknames.get(clientUUID), expiring);
        sendMessage(message);

        LOGGER.info("[Lobby " + matchID + "] " + playerNicknames.get(clientUUID) + " reconnected in the lobby");
        LOGGER.debug("[Lobby " + matchID + "] The Players in the lobby now are: " + players.values());
        LOGGER.debug("[Lobby " + matchID + "] Timer canceled");
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that called the method.
     */
    @Override
    public void onMessageReceived(Message message, ClientHandler ch) throws InternetException {
        switch (message.getMessageType()) {
            case PLAY_ASSISTANT -> {
                LOGGER.info("[Lobby " + matchID + "] PlayAssistant Message received from: " + playerNicknames.get(message.getUUID()));
                playAssistantCase(message);
            }
            case STUDENTS_TO_DINING -> {
                LOGGER.info("[Lobby " + matchID + "] StudentsToDining Message received from: " + playerNicknames.get(message.getUUID()));
                studentsToDiningCase(message, ch);
            }
            case STUDENTS_TO_ISLAND -> {
                LOGGER.info("[Lobby " + matchID + "] StudentsToIsland Message received from: " + playerNicknames.get(message.getUUID()));
                studentsToIslandCase(message, ch);
            }
            case MOVE_MOTHER_NATURE -> {
                LOGGER.info("[Lobby " + matchID + "] MoveMotherNature Message received from: " + playerNicknames.get(message.getUUID()));
                moveMotherNatureCase(message, ch);
            }
            case PLAY_CHARACTER -> {
                LOGGER.info("[Lobby " + matchID + "] PlayCharacter Message received from: " + playerNicknames.get(message.getUUID()));
                playCharacterCase(message, ch);
            }
            case CHOOSE_CLOUD -> {
                LOGGER.info("[Lobby " + matchID + "] ChooseCloud Message received from: " + playerNicknames.get(message.getUUID()));
                chooseCloudCase(message, ch);
            }
            default -> {
                LOGGER.error("[Lobby " + matchID + "] Unexpected value: " + message.getMessageType());
                throw new IllegalStateException("Unexpected value: " + message.getMessageType());
            }
        }
    }

    /**
     * @param message the Message that must be sent.
     */
    @Override
    public void sendMessage(Message message) throws InternetException {
        switch (message.getMessageType()) {
            case NEXT_TURN, START_GAME, RESILIENCE, UPDATE -> {
                for (ClientHandler ch : players.values()) {
                    ch.sendMessageToClient(message);
                    if (message.getMessageType() == MessageType.NEXT_TURN)
                        LOGGER.info("[Lobby " + matchID + "] Sent " + message.getMessageType().getClassName() + "[nextPlayer: " + ((NextTurnMessage) message).getNextPlayerNickname() + "] to " + playerNicknames.get(ch.getUUID()));
                    else
                        LOGGER.info("[Lobby " + matchID + "] Sent " + message.getMessageType().getClassName() + " to " + playerNicknames.get(ch.getUUID()));
                }
            }
            case END_GAME -> {
                Timer timer = new Timer();
                Lobby thisLobby = this;
                for (ClientHandler ch : players.values()) {
                    ch.sendMessageToClient(message);
                    LOGGER.info("[Lobby " + matchID + "] Sent " + message.getMessageType().getClassName() + " to " + playerNicknames.get(ch.getUUID()));
                }
                LOGGER.info("[Lobby " + matchID + "] closing in 10 seconds...");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Server.server.closeLobby(thisLobby);
                    }
                }, 10000);

            }
            case ERROR, PLANNING_PHASE -> {
                ClientHandler ch = players.get(message.getUUID());
                ch.sendMessageToClient(message);
                LOGGER.info("[Lobby " + matchID + "] Sent " + message.getMessageType().getClassName() + " to " + playerNicknames.get(ch.getUUID()));
            }
            default ->
                    LOGGER.error("[Lobby " + matchID + "] The lobby doesn't know where to send this message: " + message.getMessageType().getClassName());
        }
    }

    /**
     * Perform actions when client wants to disconnect
     *
     * @param clientUUID the UUID of the client to disconnect.
     */
    @Override
    public void onDisconnect(String clientUUID) {
        ClientHandler clientToDisconnect = players.get(clientUUID);
        disconnectedPlayers.put(clientUUID, clientToDisconnect);
        players.remove(clientUUID);
        LOGGER.info("[Lobby " + matchID + "] Disconnected " + playerNicknames.get(clientUUID) + " from the lobby");
        LOGGER.debug("[Lobby " + matchID + "] Remaining players in the lobby are: " + playerNicknames.keySet().stream().filter(el -> !disconnectedPlayers.containsKey(el)).map(playerNicknames::get).toList());
        Server.server.onDisconnect(clientUUID);
        Lobby lobby = this;
        if (players.size() == 0) {
            Server.server.closeLobby(lobby);
            LOGGER.debug("[Lobby " + matchID + "] The game is over because there aren't any players in the lobby");
        }
        if (players.size() == 1) {
            endGameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss z");
                    ZonedDateTime date = ZonedDateTime.now().plusMinutes(5);
                    String expiring = date.format(formatter);
                    Message message = new ResilienceMessage(false, playerNicknames.get(clientUUID), expiring);
                    sendMessage(message);
                }
            }, 500);
            LOGGER.debug("[Lobby " + matchID + "] The 10-minutes timer has started");
            endGameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (players.size() == 0) {
                        Server.server.closeLobby(lobby);
                        LOGGER.debug("[Lobby " + matchID + "] The game is over because there aren't any players in the lobby");
                    } else {
                        Message message = new EndGameMessage(players.keySet().stream().toList().get(0), playerNicknames.get(players.keySet().stream().toList().get(0)));
                        sendMessage(message);
                        Server.server.closeLobby(lobby);
                        LOGGER.debug("[Lobby " + matchID + "] The game is over because the timer has expired");
                    }
                }
            }, 600500);
        }
    }
}