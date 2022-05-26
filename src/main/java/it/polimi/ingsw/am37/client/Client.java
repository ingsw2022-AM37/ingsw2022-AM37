package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.model.FactionColor;
import it.polimi.ingsw.am37.model.student_container.UnlimitedStudentsContainer;
import it.polimi.ingsw.am37.network.ClientSocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.Properties;

/**
 * This class represent the user interface of the game. Construct it providing initial arguments for connections and
 * graphics mode, its constructor request the correct parameters if invalid ones are provided. Star the game calling the
 * {@link Client#start()} method to start the game logic. Constructor and start method may throw
 * {@link PlayerAbortException} if user decide to abort the current action and close the game.
 */
public class Client {
    /**
     * Key of properties that contains old lobby id value
     */
    final static String P_LOBBY_KEY = "Lobby";
    /**
     * Key of properties that contains old nickname
     */
    final static String P_NICKNAME_KEY = "Nickname";
    /**
     * Key of properties that contains old advanced rules setting
     */
    final static String P_ADVANCEDRULES_KEY = "AdvancedRulesEnable";
    /**
     * Key of properties that contains old number of player setting
     */
    final static String P_LOBBYSIZE_KEY = "LobbySize";
    /**
     * Key of properties that contains old UUID value
     */
    private final static String P_UUID_KEY = "UUID";
    /**
     * Old configuration settings stored as properties entry
     */
    final Properties savedProperties;
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
     * Flag used to start the game after joining lobby
     */
    private boolean inGame = false;
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

    private Message lastReadMessage;

    private Thread userInputThread;

    /**
     * Construct a fully functional client and connect to the lobby with provided argumetns. The lobby is restored from
     * a save files if clients was disconnected before the end of a match; otherwise, the user if prompted to provide
     * new nickname and connection setting.
     */
    public Client(String address, String port, String graphics) throws PlayerAbortException {
        boolean resilienceUsable = false;
        status = ClientStatus.LOGIN;
        savedProperties = new Properties();
        try {
            savedProperties.load(Client.class.getResourceAsStream("config.properties"));
            if (savedProperties.containsKey(P_UUID_KEY) && savedProperties.containsKey(P_LOBBYSIZE_KEY) &&
                    savedProperties.containsKey(P_ADVANCEDRULES_KEY) && savedProperties.containsKey(P_LOBBY_KEY) &&
                    savedProperties.containsKey(P_NICKNAME_KEY))
                resilienceUsable = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Persistence disabled because file not found");
        }
        UUID = (String) savedProperties.getOrDefault(P_UUID_KEY, java.util.UUID.randomUUID().toString());

        view = switch (graphics.toUpperCase()) {
            case "GUI" -> new GuiView();
            case "CLI" -> new CliView();
            default -> {
                System.err.println("Bad graphics mode input, using default: \"CLI\"");
                yield new CliView();
            }
        };
        while (!tryConnection(address, port)) {
            Boolean defaultOptions = view.askConfirm(
                    "Do you want to use default options? Please write \"yes\" or \"no\" or \"close " + "game\":");

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
        ActiveLobbiesMessage activeLobbiesMessage = null;
        try {
            activeLobbiesMessage = (ActiveLobbiesMessage) socket.getMessageBuffer().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (activeLobbiesMessage.getLobbyIDs()
                .contains(Integer.parseInt(savedProperties.getProperty(P_LOBBY_KEY, "-1")))) {
            this.nickname = savedProperties.getProperty(P_NICKNAME_KEY);
            if (!Objects.equals(UUID, savedProperties.getProperty(P_UUID_KEY)) || this.nickname == null)
                System.err.println("Impossible to use resilience");
            else {
                sendLoginMessage(UUID, nickname);
                if (onMessage()) {
                    sendLobbyMessage(UUID, Boolean.parseBoolean(savedProperties.getProperty(P_ADVANCEDRULES_KEY)),
                            Integer.parseInt(savedProperties.getProperty(P_LOBBYSIZE_KEY)));
                }
            }
        } else {
            status = ClientStatus.CHOOSINGNAME;
            chooseNickname();
            chooseLobby();
        }
        view.waitingMatch();
    }

    /**
     * @param num students moved
     */
    void addTotalStudentsInTurn(int num) {
        totalStudentsInTurn = totalStudentsInTurn + num;
    }

    /**
     * Method used to set beginGame
     */
    public void beginGame() {
        inGame = true;
    }

    /**
     * @return if server has an exception for our choice
     */
    private boolean chooseCloud() {

        String cloudId;

        cloudId = view.askCloud();

        Message message = new ChooseCloudMessage(UUID, cloudId);

        socket.sendMessage(message);

        return onMessage();
    }

    /**
     * This method ask user for nickname and try to set it (and store in config files) if it's available. This method
     * continues to ask until it's a correct and unique nickname is provided and accepted by the server
     */
    private void chooseLobby() throws PlayerAbortException {
        LobbyParameters parameters = view.askLobbyParameters();
        if (parameters == null) throw new PlayerAbortException();
        sendLobbyMessage(UUID, parameters.advancedRulesEnabled(), parameters.lobbySize());
        if (onMessage()) {
            ConfirmMessage confirmMessage = (ConfirmMessage) lastReadMessage;
            String s = Integer.toString(confirmMessage.getLobbyId());
            savedProperties.setProperty(P_LOBBY_KEY, s);
            savedProperties.setProperty(P_ADVANCEDRULES_KEY, String.valueOf(parameters.advancedRulesEnabled()));
            savedProperties.setProperty(P_LOBBYSIZE_KEY, String.valueOf(parameters.lobbySize()));
        }
    }

    /**
     * This method ask user for nickname and try to set it (and store in config files) if it's available. This method
     * continues to ask until it's a correct and unique nickname is provided and accepted by the server
     */
    private void chooseNickname() throws PlayerAbortException {
        String tempNick;
        while (nickname == null) {
            tempNick = view.chooseNickname();
            if (tempNick.equals("close game")) throw new PlayerAbortException();
            sendLoginMessage(UUID, tempNick);
            if (!onMessage()) {
                ErrorMessage mes = (ErrorMessage) lastReadMessage;
                System.out.println("\n" + mes.getMessage() + "\n");
            } else if (lastReadMessage.getMessageType() == MessageType.CONFIRM) {
                this.nickname = tempNick;
                savedProperties.setProperty(P_NICKNAME_KEY, this.nickname);
            }
        }
    }

    public String getAddress() {
        return address;
    }

    /**
     * @return My nickname
     */
    public String getNickname() {
        return nickname;
    }

    public int getPort() {
        return port;
    }

    public ClientStatus getStatus() {
        return status;
    }

    /**
     * @param action New status for the client
     */
    public void setStatus(ClientStatus action) {
        status = action;
    }

    /**
     * @return total students moved in action phase in a turn
     */
    int getTotalStudentsInTurn() {
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
        int islandId = view.askMotherNature();
        Message message = new MoveMotherNatureMessage(UUID, islandId);
        socket.sendMessage(message);
        return onMessage();
    }

    /**
     * Main method to perform student actions
     *
     * @return if action has accepted or reject by the server
     * @see ActionType
     */
    private boolean moveStudents() {
        HashMap<String, String> response;
        Message message;
        UnlimitedStudentsContainer container = new UnlimitedStudentsContainer();
        FactionColor color = null;

        response = view.askStudents(this);
        for (FactionColor temp : FactionColor.values())
            if (response.get("color").equals(temp.toString())) color = temp;
        container.addStudents(Integer.parseInt(response.get("number")), color);

        if (response.get("destination").equals("d")) {
            message = new StudentsToDiningMessage(UUID, container);
        } else {
            message = new StudentsToIslandMessage(UUID, container, Integer.parseInt(response.get("islandDest")));
        }
        socket.sendMessage(message);
        return onMessage();
    }

    /**
     * Method used to check if the last action performed have been successful or not. More formally returns {@code true}
     * if server responds with an {@link UpdateMessage} or {@code false} if the server responds with an
     * {@link ErrorMessage}
     *
     * @return if last action was accepted by the server or rejected
     */
    private boolean onMessage() {
        Message message = null;
        try {
            message = socket.getMessageBuffer().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        lastReadMessage = message;
        return message.getMessageType() != MessageType.ERROR;
    }

    /**
     * Main method to perform assistant action
     *
     * @return if action has accepted or reject by the server
     * @see ActionType
     */
    private boolean playAssistant() {
        int val;

        val = view.askAssistant(this);
        Message message = new PlayAssistantMessage(UUID, val);

        socket.sendMessage(message);

        return onMessage();
    }

    /**
     * Main method to perform characters action
     *
     * @return if action has accepted or reject by the server
     * @see ActionType
     */
    private boolean playCharacter() {

        //TODO DA FARE IL METODO CHE CHIEDE VIEW.ASKCHARACTER E POI CREA UN MESSAGGIO E LO MANDA CON IL PERSONAGGIO
        // SCELTO

        return false;
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


        status = ClientStatus.WAITINGFORTURN;
        while (socket.isConnectedToServer()) {

            currentAction = view.takeInput(this);

            switch (currentAction) {
                case SHOW_MENU -> {
                }
                case SHOW_TABLE -> view.showTable();
                case SHOW_STATUS -> view.showPlayerStatus(view.askPlayer());
                case SHOW_DECK -> view.showDeck(view.getReducedModel().getPlayers().get(this.nickname));
                case SHOW_CONNECTION -> view.showConnection(this);
                case SHOW_PLAYERS -> view.showPlayersNicknames();
                case CLOSE_GAME -> {
                    socket.closeGame();
                    throw new PlayerAbortException();
                }
                case MOVE_STUDENTS_ISLAND, MOVE_STUDENTS_DINING -> {
                    boolean actionOk = moveStudents();
                    if (actionOk && totalStudentsInTurn == 3) {
                        status = ClientStatus.MOVINGMOTHERNATURE;
                        totalStudentsInTurn = 0;
                    } else if (!actionOk) view.impossibleStudents();
                }
                case MOVE_MOTHER_NATURE -> {
                    if (moveMotherNature()) status = ClientStatus.CHOOSINGCLOUD;
                    else view.impossibleMotherNature();
                }
                case CHOOSE_CLOUD -> {
                    if (chooseCloud()) {
                        System.out.println("You have finished your turn");
                        status = ClientStatus.WAITINGFORTURN;
                    } else view.impossibleCloud();
                }
                case PLAY_ASSISTANT -> {
                    if (playAssistant()) status = ClientStatus.WAITINGFORTURN;
                    else view.impossibleAssistant();
                }
                case PLAY_CHARACTER -> //TODO implements logic and remove exception
                        view.impossibleInputForNow();
                default -> {
                    view.impossibleInputForNow();
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
            socket = new ClientSocket(address, Integer.parseInt(port), this);
            new Thread(socket, "__client_socket").start();
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
    protected record ConnectionParameters(String address, int port) {
    }

    /**
     * Define a simple record to store setting for the lobby to be exchange between methods
     *
     * @param advancedRulesEnabled flag for advanced rules enabling
     * @param lobbySize            size of the lobby
     */
    protected record LobbyParameters(boolean advancedRulesEnabled, int lobbySize) {
    }


}
