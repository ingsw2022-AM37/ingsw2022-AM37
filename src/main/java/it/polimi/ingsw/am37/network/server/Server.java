package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.Lobby;
import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.network.MessageReceiver;
import it.polimi.ingsw.am37.network.exceptions.InternetException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * It represents the Server that manage Players login and game Lobbies.
 */
public class Server implements MessageReceiver {

    /**
     * A Logger.
     */
    private static Logger LOGGER;

    /**
     * It represents the server socket used to accept connections with Clients,
     */
    private static ServerSocket serverSocket;

    /**
     * It represents the server port.
     */
    private static int port;

    /**
     * Port's value
     */
    static private String portValue;

    /**
     * Associates the UUID of the Client with the ClientHandler.
     */
    private static HashMap<String, ClientHandler> clientHandlerMap;

    /**
     * List to keep track of active Lobbies.
     */
    private static ArrayList<Lobby> activeLobbies;

    /**
     * List to keep track of the players' nicknames.
     */
    private static ArrayList<String> nicknames;

    /**
     * Default Constructor
     */
    public Server(int serverPort) {
        port = serverPort;
        LOGGER = LogManager.getLogger(Server.class);

        new Thread(() -> {
            //TODO: LOGGER
            Socket socket;
            do {
                try {
                    serverSocket = new ServerSocket(serverPort);
                    System.out.println("ServerSocket awaiting connections...");
                    socket = serverSocket.accept();
                    System.out.println("Connection from " + socket + "!");
                } catch (IOException e) {
                    System.err.println("Error encountered while trying to connect");
                    System.err.println(e.getMessage());
                    return;
                }
                ClientHandler ch = new ClientHandler(socket);
                ch.setMessageReceiver(this);
                new Thread(ch).start();
            } while (!serverSocket.isClosed());
        }).start();
    }

    /**
     * Starts the Server and checks that the arguments are valid.
     *
     * @param args arguments received via command prompt
     */
    public static void main(String[] args) {
        boolean wrongInitialInput;

        wrongInitialInput = tryConnectionWithArgs(args);
        tryConnectionAgain(wrongInitialInput);

        new Server(port);
        //Si dovrà chiudere la socket? se si quando?
    }

    /**
     * @param args It's the array of string created by main class
     * @return if initial input was wrong
     */
    private static boolean tryConnectionWithArgs(String[] args) {
        final int expectedArguments = 2;
        int i = 0;
        boolean wrongInitialInput = false;
        String portString = "port";

        List<String> list = Arrays.stream(args).map(String::toLowerCase).toList();
        args = list.toArray(new String[0]);
        if (args.length != expectedArguments) {
            wrongInsertFewArguments();
            wrongInitialInput = true;
        } else {
            while (i < args.length) {
                if (!(args[i].equals("--" + portString))) {
                    wrongInsert();
                    wrongInitialInput = true;
                    break;
                }
                portValue = args[i + 1];
                i = i + 2;
            }
            try {
                port = Integer.parseInt(portValue);
            } catch (NumberFormatException e) {
                wrongInsertPort();
                wrongInitialInput = true;
            }
        }
        return wrongInitialInput;
    }

    /**
     * @param wrongInitialInput If initial input was wrong
     */
    private static void tryConnectionAgain(boolean wrongInitialInput) {
        final int defaultPort = 60000;
        String response;

        while (wrongInitialInput) {
            response = askDefault();
            if ((response.equals("close game")))
                closeGame();
            else {
                if (response.equals("yes")) {
                    portValue = "";
                    portValue = Integer.toString(defaultPort);
                } else {
                    portValue = "";
                    response = insertYourParameters();
                    if (response.equals("close game"))
                        closeGame();
                }
            }
            wrongInitialInput = false;
        }
    }

    /**
     * Method used if server manager decided to don't use default setting for connection, so he will be asked to insert his parameters
     *
     * @return Server's decision
     */
    private static String insertYourParameters() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Write server's port or \"close game\":");
            String s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("close game"))
                return s;
            try {
                int num = Integer.parseInt(s);
                portValue = Integer.toString(num);
            } catch (NumberFormatException e) {
                wrongInsertPort();
                continue;
            }
            return "true";
        }
    }

    /**
     * Notify when a number port is expected but another input was given
     */
    private static void wrongInsertPort() {
        System.out.println("You haven't written a number as server's port");
    }

    /**
     * Notify if a player has inserted fewer parameters than expected during opening of the terminal
     */
    private static void wrongInsertFewArguments() {
        System.out.println("You have written too few arguments");
    }

    /**
     * Generic notification of an input error
     */
    private static void wrongInsert() {
        System.out.println("You have written wrong parameters");
    }

    /**
     * Method used to ask if server manager wants to use default parameters for connection
     *
     * @return Response
     */
    private static String askDefault() {
        String s;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Do you want to use default options? Please write \"yes\" or \"no\" or \"close game\":");
            s = scanner.nextLine().toLowerCase().trim().replaceAll(" +", " ");
            if (s.equals("yes") || s.equals("no") || s.equals("close game"))
                return s;
            wrongInsert();
        }
    }

    private static void closeGame() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Runtime.getRuntime().halt(0);
            }
        }, 3000);
        System.exit(0);
        timer.cancel();
    }

    /**
     * @param client the Client to disconnect.
     */
    private static void onDisconnect(ClientHandler client) {
        //TODO: Va rimosso il player e il suo nickname? No se vogliamo fare la Resilienza, semplicemente lo si disattiva somehow
        client.disconnect();
    }

    /**
     * Creates the Lobby.
     *
     * @param lobbySize    the size of the lobby
     * @param advancedMode flag to turn on advanced mode
     */
    private static Lobby createLobby(int lobbySize, boolean advancedMode) {
        boolean generated = false;
        int matchID = new Random().nextInt() & Integer.MAX_VALUE;
        do {
            for (Lobby lobby : activeLobbies) {
                if (lobby.getMatchID() == matchID) {
                    matchID = new Random().nextInt() & Integer.MAX_VALUE;
                    generated = true;
                }
            }
        } while (generated);
        return new Lobby(lobbySize, advancedMode, matchID);
    }


    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the message received.
     */
    @Override
    public void onMessageReceived(Message message, ClientHandler ch) throws InternetException {
        Message response;
        switch (message.getMessageType()) {
            case LOGIN:
                if (nicknames.contains(((LoginMessage) message).getNickname())) {
                    clientHandlerMap.put(message.getUUID(), ch);
                    nicknames.add(((LoginMessage) message).getNickname());
                    response = new ConfirmMessage(message.getUUID());
                    sendMessage(response);
                } else {
                    response = new ErrorMessage(message.getUUID(), "Nickname already used");
                    sendMessage(response);
                }
                break;
            case LOBBY_REQUEST:
                boolean lobbyFound = false;
                for (Lobby lobby : activeLobbies) {
                    if (!lobby.isGameReady() && lobby.isAdvancedMode() == ((LobbyRequestMessage) message).isDesiredAdvanceMode() && lobby.getLobbySize() == ((LobbyRequestMessage) message).getDesiredSize()) {
                        lobby.addPlayerInLobby(message.getUUID(), ch);
                        ch.setMessageReceiver(lobby);
                        lobbyFound = true;
                        break;
                    }
                }
                if (!lobbyFound) {
                    Lobby lobby = createLobby(((LobbyRequestMessage) message).getDesiredSize(),
                            ((LobbyRequestMessage) message).isDesiredAdvanceMode());
                    new Thread(lobby).start();
                    activeLobbies.add(lobby);
                    lobby.addPlayerInLobby(message.getUUID(), ch);
                    ch.setMessageReceiver(lobby);
                }
                break;
            default:
                response = new ErrorMessage(message.getUUID(), "You've sent a message that the server can't " +
                        "understand");
                sendMessage(response);
                break;
        }
    }

    /**
     * @param message the Message that must be sent.
     */
    @Override
    public void sendMessage(Message message) throws InternetException {
        ClientHandler client = clientHandlerMap.get(message.getUUID());
        client.sendMessageToClient(message);
    }
}
