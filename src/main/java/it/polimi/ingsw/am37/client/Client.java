package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.GameManager;
import it.polimi.ingsw.am37.model.Island;
import it.polimi.ingsw.am37.model.Player;
import it.polimi.ingsw.am37.model.character.Character;
import it.polimi.ingsw.am37.model.character.Effect;
import it.polimi.ingsw.am37.model.character.OptionBuilder;
import it.polimi.ingsw.am37.model.student_container.LimitedStudentsContainer;
import it.polimi.ingsw.am37.model.student_container.StudentsContainer;
import it.polimi.ingsw.am37.network.ClientSocket;

import java.io.*;
import java.util.*;

/**
 * This class represent the user interface of the game. Construct it providing initial arguments for connections and
 * graphics mode, its constructor request the correct parameters if invalid ones are provided. Star the game calling the
 * {@link Client#start()} method to start the game logic. Constructor and start method may throw
 * {@link PlayerAbortException} if user decide to abort the current action and close the game.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class Client {

    /**
     * Flag for disabling the resilience logic
     */
    private final static boolean debug_disabledResilience = true;

    /**
     * Static constant that store the path to the saves folder
     */
    private final static String resilienceFilePath =
            System.getProperty("user.home") + File.separator + ".eryantisGame" + File.separator +
                    "resilience.properties";

    /**
     * Key of properties that contains old lobby id value
     */
    private final static String P_LOBBY_KEY = "Lobby";

    /**
     * Key of properties that contains old nickname
     */
    private final static String P_NICKNAME_KEY = "Nickname";

    /**
     * Key of properties that contains old advanced rules setting
     */
    private final static String P_ADVANCEDRULES_KEY = "AdvancedRulesEnable";
    /**
     * Key of properties that contains old number of player setting
     */
    private final static String P_LOBBYSIZE_KEY = "LobbySize";
    /**
     * Key of properties that contains old UUID value
     */
    private final static String P_UUID_KEY = "UUID";

    /**
     * Old configuration settings stored as properties entry
     */
    private final Properties savedProperties;
    /**
     * Properties saving
     */
    private final Properties messagesConstants;
    /**
     * Client identifier
     */
    private final String UUID;
    /**
     * This is the address of the server to connect
     */
    private final String address;
    /**
     * This is the port of the server to connect
     */
    private final int port;
    /**
     * View which can be at real time GUI or CLI, based on the information given by player at the start
     */
    private final AbstractView view;
    /**
     * It's the sum of moved students in this action phase
     */
    private int totalStudentsInTurn = 0;
    /**
     * Client's nickname
     */
    private String nickname = null;
    /**
     * It represents the state of client during a game
     */
    private ClientStatus status;
    /**
     * This is the socket to handle communication with the server
     */
    private ClientSocket socket;
    /**
     * This is used by internal {@link Client#hasReceivedError()} to store the read message for further processing by
     * some other functions.
     */
    private Message lastReadMessage;
    /**
     * The settings of this match
     */
    private LobbyParameters settings;

    /**
     * Construct a fully functional client and connect to the lobby with provided arguments. The lobby is restored from
     * a save files if clients was disconnected before the end of a match; otherwise, the user if prompted to provide
     * new nickname and connection setting.
     */
    public Client(String address, String port, String graphics) throws PlayerAbortException {
        boolean resilienceUsable = false;
        status = ClientStatus.LOGIN;
        savedProperties = new Properties();
        messagesConstants = new Properties();
        try {
            messagesConstants.load(Client.class.getResourceAsStream("/messages.properties"));
        } catch (IOException e) {
            System.err.println("Unable to find messages file");
        }
        if (!debug_disabledResilience) {
            try {
                savedProperties.load(new FileInputStream(resilienceFilePath));
                if (savedProperties.containsKey(P_UUID_KEY) && savedProperties.containsKey(P_LOBBYSIZE_KEY) &&
                        savedProperties.containsKey(P_ADVANCEDRULES_KEY) && savedProperties.containsKey(P_LOBBY_KEY) &&
                        savedProperties.containsKey(P_NICKNAME_KEY)) resilienceUsable = true;
            } catch (NullPointerException | FileNotFoundException e) {
                System.err.println("Persistence disabled because file not found");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (resilienceUsable) UUID = savedProperties.getProperty(P_UUID_KEY);
        else {
            UUID = java.util.UUID.randomUUID().toString();
            savedProperties.setProperty(P_UUID_KEY, UUID);
        }

        view = switch (graphics.toUpperCase()) {
            case "GUI" -> new GuiView();
            case "CLI" -> new CliView();
            default -> {
                System.err.println("Bad graphics mode input, using default: \"CLI\"");
                yield new CliView();
            }
        };
        while (!tryConnection(address, port)) {
            Boolean defaultOptions = view.askConfirm("Do you want to use default options?");
            if (defaultOptions == null) {
                throw new PlayerAbortException();
            } else if (defaultOptions) {
                address = "localhost";
                port = "60000";
            } else {
                ConnectionParameters parameters = view.askConnectionParameters();
                address = parameters.address();
                port = Integer.toString(parameters.port());
            }
        }
        this.address = address;
        this.port = Integer.parseInt(port);
        ActiveLobbiesMessage activeLobbiesMessage;
        try {
            activeLobbiesMessage = (ActiveLobbiesMessage) socket.getResponseBuffer().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (resilienceUsable && activeLobbiesMessage.getLobbyIDs()
                .contains(Integer.parseInt(savedProperties.getProperty(P_LOBBY_KEY, "-1")))) {
            this.nickname = savedProperties.getProperty(P_NICKNAME_KEY);
            if (!Objects.equals(UUID, savedProperties.getProperty(P_UUID_KEY)) || this.nickname == null)
                view.displayError("Impossible to use resilience");
            else {
                sendLoginMessage(UUID, nickname);
                if (!hasReceivedError()) {
                    sendLobbyMessage(UUID, Boolean.parseBoolean(savedProperties.getProperty(P_ADVANCEDRULES_KEY)),
                            Integer.parseInt(savedProperties.getProperty(P_LOBBYSIZE_KEY)));
                    if (!hasReceivedError())
                        settings =
                                new LobbyParameters(Boolean.parseBoolean(savedProperties.getProperty(P_ADVANCEDRULES_KEY)), Integer.parseInt(savedProperties.getProperty(P_LOBBY_KEY)));
                }
            }
        } else {
            status = ClientStatus.CHOOSINGNAME;
            try {
                chooseNickname();
                chooseLobby();
            } catch (PlayerAbortException e) {
                socket.closeGame();
                return;
            }
        }
        view.displayImportant(messagesConstants.getProperty("i.waitingStart"));
        if (!debug_disabledResilience) {
            File file = new File(resilienceFilePath);
            if (file.getParentFile().mkdirs()) {
                try (OutputStream stream = new FileOutputStream(resilienceFilePath)) {
                    savedProperties.store(stream,
                            "This file is for resilience only. DO NOT MODIFY ANY OF THE FOLLOWING " + "LINES\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * @param num students moved
     */
    public void addTotalStudentsInTurn(int num) {
        totalStudentsInTurn = totalStudentsInTurn + num;
    }

    /**
     * This method ask user for nickname and try to set it (and store in config files) if it's available. This method
     * continues to ask until it's a correct and unique nickname is provided and accepted by the server
     */
    private void chooseLobby() throws PlayerAbortException {
        LobbyParameters parameters = view.askLobbyParameters();
        if (parameters == null) throw new PlayerAbortException();
        sendLobbyMessage(UUID, parameters.advancedRulesEnabled(), parameters.lobbySize());
        if (!hasReceivedError()) {
            ConfirmMessage confirmMessage = (ConfirmMessage) lastReadMessage;
            String s = Integer.toString(confirmMessage.getLobbyId());
            savedProperties.setProperty(P_LOBBY_KEY, s);
            savedProperties.setProperty(P_ADVANCEDRULES_KEY, String.valueOf(parameters.advancedRulesEnabled()));
            savedProperties.setProperty(P_LOBBYSIZE_KEY, String.valueOf(parameters.lobbySize()));
            settings = parameters;
        }
    }

    /**
     * This method ask user for nickname and try to set it (and store in config files) if it's available. This method
     * continues to ask until it's a correct and unique nickname is provided and accepted by the server
     */
    private void chooseNickname() throws PlayerAbortException {
        String tempNick;
        while (nickname == null) {
            tempNick = view.askNickname();
            if (tempNick.equals("exit")) throw new PlayerAbortException();
            if (tempNick.isBlank()) {
                view.displayError(messagesConstants.getProperty("e.nicknameBlank"));
            } else {
                sendLoginMessage(UUID, tempNick);
                if (!hasReceivedError()) {
                    if (lastReadMessage.getMessageType() == MessageType.CONFIRM) {
                        this.nickname = tempNick;
                        savedProperties.setProperty(P_NICKNAME_KEY, this.nickname);
                    }
                }
            }
        }
    }

    /**
     * @return if server has an exception for our choice
     */
    private boolean chooseCloud() {
        String cloudId;
        cloudId = view.askCloud();
        if (view.getReducedModel().getClouds().get(cloudId).size() != 0) {
            Message message = new ChooseCloudMessage(UUID, cloudId);
            socket.sendMessage(message);
            return !hasReceivedError();
        } else {
            view.displayError(messagesConstants.getProperty("e.impossibleCloud"));
            return false;
        }
    }

    /**
     * @return the server address this client is connected to
     */
    public String getAddress() {
        return address;
    }

    public String getMessageString(String key) {
        return messagesConstants.getProperty(key);
    }

    /**
     * @return My nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return port of server
     */
    public int getPort() {
        return port;
    }

    /**
     *
     * @return chosen lobby parameters
     */
    public LobbyParameters getSettings() {
        return settings;
    }

    /**
     *
     * @return player's status
     */
    public ClientStatus getStatus() {
        return status;
    }

    /**
     * @param action New status for the client
     */
    public void setStatus(ClientStatus action) {
        status = action;
        ActionType.updateAvailableAction(status, settings.advancedRulesEnabled);
    }

    /**
     * @return total students moved in action phase in a turn
     */
    public int getTotalStudentsInTurn() {
        return totalStudentsInTurn;
    }

    /**
     * @return Player's identifier
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * @return view of the client, used to display some information
     */
    public AbstractView getView() {
        return view;
    }

    /**
     * @return if server has an exception for the movement
     */
    private boolean moveMotherNature() {
        int islandId = view.askMotherNature(view.getReducedModel().getPlayers().get(nickname).getLastAssistantPlayed());
        Message message = new MoveMotherNatureMessage(UUID, islandId);
        socket.sendMessage(message);
        return !hasReceivedError();
    }

    /**
     * Method used to check if the last action performed have been successful or not. More formally returns
     * {@code false} if server responds with an {@link UpdateMessage} or {@code true} if the server responds with an
     * {@link ErrorMessage}
     *
     * @return if last action was accepted by the server or rejected
     */
    private boolean hasReceivedError() {
        Message message;
        boolean isError = false;
        try {
            do {
                message = socket.getResponseBuffer().take();
                if (message.getMessageType() == MessageType.ERROR) {
                    isError = true;
                    view.displayError(((ErrorMessage) message).getMessage());
                }

            } while (socket.getResponseBuffer().size() > 0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        lastReadMessage = message;
        return isError;
    }

    /**
     * Main method to perform student actions
     *
     * @param isToIsland true if the player is moving to an island, false otherwise
     * @return if action has accepted or reject by the server
     * @see ActionType
     */
    private boolean moveStudentsRegular(Boolean isToIsland) {
        StudentsContainer container;
        while (true) {
            container = view.askStudentsFromEntrance(this, 0);
            if (container == null || !view.getReducedModel().getBoards().get(nickname).getEntrance().contains(container) || container.size() == 0)
                return false;
            else {
                break;
            }
        }
        Message message;
        if (isToIsland == null) isToIsland = view.askDestination();

        if (isToIsland) message = new StudentsToIslandMessage(UUID, container, view.askIsland());
        else message = new StudentsToDiningMessage(UUID, container);

        socket.sendMessage(message);
        return !hasReceivedError();

    }

    /**
     * Main method to perform assistant action
     *
     * @return if action has accepted or reject by the server
     * @see ActionType
     */
    private boolean playAssistant() {
        int val = view.askAssistant(this);
        Message message = new PlayAssistantMessage(UUID, val);
        socket.sendMessage(message);
        return !hasReceivedError();
    }

    /**
     * Main method to perform characters action
     *
     * @return if action has accepted or reject by the server
     * @see ActionType
     */
    private boolean playCharacter() {
        Player currentPlayer = view.getReducedModel().getPlayers().get(nickname);
        view.displayImportant("You have " + currentPlayer.getNumberOfCoins() +
                (currentPlayer.getNumberOfCoins() == 1 ? " coin" : " coins"));
        view.showCharacters();
        Effect effect = view.askCharacter();
        if (effect == null) {
            return false;
        }
        Character character = view.getReducedModel()
                .getCharacters()
                .stream()
                .filter(c -> c.getEffectType() == effect)
                .findFirst()
                .get();
        OptionBuilder oBuilder = OptionBuilder.newBuilder(currentPlayer);
        PlayCharacterMessage message;
        if (currentPlayer.getNumberOfCoins() < character.getCurrentPrice()) {
            view.displayError("You don't have enough coins to play this character");
            return false;
        }
        switch (effect) {
            case MONK -> {
                final int MONK_STUDENTS = 1;
                StudentsContainer container = view.askStudentsFromCharacter(character, MONK_STUDENTS, this);
                Island destinationIsland = view.getReducedModel().getIslands().get(view.askIsland());
                oBuilder.primaryContainer((LimitedStudentsContainer) container);
                oBuilder.island(destinationIsland);
                oBuilder.intPar(MONK_STUDENTS);
            }
            case HERALD, GRANDMA, CENTAUR -> {
                int islandId = view.askIsland();
                oBuilder.island(view.getReducedModel()
                        .getIslands()
                        .stream()
                        .filter(i -> i.getIslandId() == islandId)
                        .findFirst()
                        .get());
            }
            case JESTER -> {
                final int JESTER_STUDENTS = 3;
                view.displayImportant("Please select the students you want from the @|bold card|@:");
                LimitedStudentsContainer container1 =
                        (LimitedStudentsContainer) view.askStudentsFromCharacter(character, JESTER_STUDENTS, this);
                LimitedStudentsContainer container2 = (LimitedStudentsContainer) view.askStudentsFromEntrance(this,
                        JESTER_STUDENTS);
                if (container1 == null || container2 == null) return false;
                else {
                    oBuilder.primaryContainer(container1);
                    oBuilder.secondaryContainer(container2);
                }
                oBuilder.intPar(JESTER_STUDENTS);
            }
            case MUSHROOM_MAN, THIEF -> {
                FactionColor color = view.askColor(this);
                if (color == null) return false;
                else oBuilder.color(color);
                if (effect == Effect.THIEF)
                    oBuilder.intPar(3);
            }
            case MINSTREL -> {

                int MINSTREL_STUDENTS = view.askStudentsNumber(new ArrayList<Integer>(Arrays.asList(1,2)));

                if (MINSTREL_STUDENTS == 1 || MINSTREL_STUDENTS == 2) {
                    LimitedStudentsContainer container1 =
                            (LimitedStudentsContainer) view.askStudentsFromEntrance(this, MINSTREL_STUDENTS);
                    LimitedStudentsContainer container2 = (LimitedStudentsContainer) view.askStudentFromDining(this,
                            MINSTREL_STUDENTS);
                    if (container1 == null || container2 == null)
                        return false;
                    else {
                        oBuilder.primaryContainer(container1);
                        oBuilder.secondaryContainer(container2);
                    }
                    oBuilder.intPar(MINSTREL_STUDENTS);
                } else {
                    view.displayError("Invalid number of students");
                    return false;
                }
            }
            case PRINCESS -> {
                final int PRINCESS_STUDENTS = 1;
                LimitedStudentsContainer container =
                        (LimitedStudentsContainer) view.askStudentsFromCharacter(character, PRINCESS_STUDENTS, this);
                oBuilder.primaryContainer(container);
                oBuilder.intPar(PRINCESS_STUDENTS);
            }
            case MAGIC_POSTMAN -> {
                final int MAGIC_POSTMAN_MN_ADDITIONAL_MOVEMENTS = 2;
                if (currentPlayer.getLastAssistantPlayed() == null) {
                    view.displayError("You have to play an assistant before playing this character");
                    return false;
                }
                oBuilder.intPar(MAGIC_POSTMAN_MN_ADDITIONAL_MOVEMENTS);
            }
            case KNIGHT -> {
                final int KNIGHT_ADDITIONAL_INFLUENCE_POINTS = 2;
                oBuilder.intPar(KNIGHT_ADDITIONAL_INFLUENCE_POINTS);
            }

            default -> {
            }
        }
        message = new PlayCharacterMessage(UUID, effect, oBuilder.build());
        socket.sendMessage(message);
        return !hasReceivedError();
    }

    /**
     * This is a utility method to send lobby message
     *
     * @param UUID          UUID of the client
     * @param advancedRules If player wants advanced rules
     * @param numPlayers    num of players in the match
     */
    private void sendLobbyMessage(String UUID, boolean advancedRules, int numPlayers) {
        Message message = new LobbyRequestMessage(UUID, numPlayers, advancedRules);
        socket.sendMessage(message);
    }

    /**
     * This is a utility method to send a login message
     *
     * @param UUID     UUID of the client
     * @param nickname chosen nickname
     */
    private void sendLoginMessage(String UUID, String nickname) {
        Message message = new LoginMessage(UUID, nickname);
        socket.sendMessage(message);
    }

    /**
     * This is the main method that handle game flow
     */
    @SuppressWarnings("BusyWait")
    public void start() throws PlayerAbortException {
        ActionType currentAction;
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        view.gameStarted();

        setStatus(ClientStatus.WAITINGFORTURN);
        while (socket.isConnectedToServer()) {
            currentAction = view.takeInput(this);

            switch (currentAction) {
                case SHOW_MENU -> {
                }
                case SHOW_TABLE -> view.showTable();
                case SHOW_STATUS -> view.showPlayerStatus(view.askPlayer(), settings.advancedRulesEnabled);
                case SHOW_DECK -> view.showDeck(view.getReducedModel().getPlayers().get(this.nickname));
                case SHOW_CONNECTION -> view.showConnection(this);
                case SHOW_PLAYERS -> view.showPlayersNicknames();
                case CLOSE_GAME -> {
                    socket.closeGame();
                    throw new PlayerAbortException();
                }
                case MOVE_STUDENTS_ISLAND -> {
                    boolean actionOk = moveStudentsRegular(true);
                    if (actionOk && totalStudentsInTurn == GameManager.MAX_FOR_MOVEMENTS[settings.lobbySize() % 2]) {
                        setStatus(ClientStatus.MOVINGMOTHERNATURE);
                        totalStudentsInTurn = 0;
                    }
                }
                case MOVE_STUDENTS_DINING -> {
                    boolean actionOk = moveStudentsRegular(false);
                    if (actionOk && totalStudentsInTurn == GameManager.MAX_FOR_MOVEMENTS[settings.lobbySize() % 2]) {
                        setStatus(ClientStatus.MOVINGMOTHERNATURE);
                        totalStudentsInTurn = 0;
                    }
                }
                case MOVE_STUDENTS_UNDEFINED -> {
                    boolean actionOk = moveStudentsRegular(null);
                    if (actionOk && totalStudentsInTurn == GameManager.MAX_FOR_MOVEMENTS[settings.lobbySize() % 2]) {
                        setStatus(ClientStatus.MOVINGMOTHERNATURE);
                        totalStudentsInTurn = 0;
                    }
                }
                case MOVE_MOTHER_NATURE -> {
                    if (moveMotherNature()) setStatus(ClientStatus.CHOOSINGCLOUD);
                }
                case CHOOSE_CLOUD -> {
                    if (chooseCloud()) {
                        view.displayImportant("You have finished your turn");
                        setStatus(ClientStatus.WAITINGFORTURN);
                    }
                }
                case PLAY_ASSISTANT -> {
                    if (playAssistant()) setStatus(ClientStatus.WAITINGFORTURN);
                }
                case PLAY_CHARACTER -> {
                    if (playCharacter()) view.displayImportant("Character played");
                }
                default -> {
                    view.displayError("e.impossibleAction");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    /**
     * @param address It's how we call address in first input in terminal
     * @param port    It's how we call port in first input in terminal
     * @return if initial input was wrong
     */
    private boolean tryConnection(String address, String port) {
        if (address == null || port == null) return false;
        try {
            view.displayInfo("Connecting to server");
            socket = new ClientSocket(address, Integer.parseInt(port), this);
            new Thread(socket, "__client_socket").start();
            view.displayInfo("Connected to server");
            return true;
        } catch (NumberFormatException e) {
            view.wrongInsertPort();
            return false;
        } catch (IOException e) {
            view.wrongServer();
            return false;
        }
    }

    /**
     * Define a simple record to store parameters of connection to be exchange between methods
     *
     * @param address the address of the server
     * @param port    the port of the server
     */
    public record ConnectionParameters(String address, int port) {
    }

    /**
     * Define a simple record to store setting for the lobby to be exchange between methods
     *
     * @param advancedRulesEnabled flag for advanced rules enabling
     * @param lobbySize            size of the lobby
     */
    public record LobbyParameters(boolean advancedRulesEnabled, int lobbySize) {
    }
}
