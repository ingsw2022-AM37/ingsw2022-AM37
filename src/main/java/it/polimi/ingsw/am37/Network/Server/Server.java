package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.Lobby;
import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.network.MessageReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * It represents the Server that manage Players login and game Lobbies.
 */
public class Server implements MessageReceiver {

    /**
     * It represents the server socket used to accept connections with Clients,
     */
    private static ServerSocket serverSocket;

    /**
     * It represents the server port.
     */
    private static int port;

    /**
     * It represents the server address;
     */
    private static String address;

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
    public Server(String serverAddress, int serverPort) {
        port = serverPort;
        address = serverAddress;

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
        final int expectedArguments = 4;
        List<String> list = Arrays.stream(args).map(String::toLowerCase).toList();
        args = list.toArray(new String[0]);
        final Map<String, String> params = new HashMap<>();
        if (args.length != expectedArguments) {
            System.err.println("Too many or too few arguments, expecting -port and -address ");
            return;
        }
        for (int i = 0; i < args.length; i++) {
            //Checks if the arguments starts with "-" and if there's a value after the argument
            if (args[i].startsWith("-") && i + 1 < args.length) {
                if (args[i].length() < 2) { //Checks if the arguments isn't only "-"
                    System.err.println("Error at argument " + args[i]);
                    return;
                }
                if (args[i + 1].charAt(0) != '-') //Checks if the next String is a value and not and argument.
                    params.put(args[i].substring(1), args[i + 1]);
                else {
                    System.err.println("Illegal parameter usage, enter a value after the argument");
                    return;
                }
                i++;
            } else {
                System.err.println("Illegal parameter usage, expected: \"-argument value\"");
                return;
            }
        }
        try {
            port = Integer.parseInt(params.get("port"));
        } catch (NumberFormatException e) {
            System.err.println("The port entered isn't an Integer");
            return;
        }
        address = params.get("address");
        new Server(address, port);
        //TODO: Si dovrà chiudere la socket? se si quando?
    }

    /**
     * @param client the Client to disconnect.
     */
    private void onDisconnect(ClientHandler client) {
        //TODO: Va rimosso il player e il suo nickname? No se vogliamo fare la Resilienza, semplicemente lo si disattiva somehow
        client.disconnect();
    }

    /**
     * Creates the Lobby.
     *
     * @param lobbySize    the size of the lobby
     * @param advancedMode flag to turn on advanced mode
     */
    private Lobby createLobby(int lobbySize, boolean advancedMode) {
        return new Lobby(lobbySize, advancedMode);
    }


    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the message received.
     */
    @Override
    public void onMessageReceived(Message message, ClientHandler ch) throws IOException {
        Message response;
        switch (message.getMessageType()) {
            case LOGIN:
                if (nicknames.contains(((LoginMessage) message).getNickname())) {
                    clientHandlerMap.put(message.getUUID(), ch);
                    nicknames.add(((LoginMessage) message).getNickname());
                    response = new ConfirmMessage(message.getUUID());
                    sendMessage(response);
                } else {
                    response = new ErrorMessage(message.getUUID(), "Nickname already used", null);
                    sendMessage(response);
                }
                break;
            case LOBBY_REQUEST:
                boolean lobbyFound = false;
                for (Lobby lobby : activeLobbies) {
                    if (!lobby.isGameReady() && lobby.isAdvancedMode() == ((LobbyRequestMessage) message).isDesiredAdvanceMode() && lobby.getLobbySize() == ((LobbyRequestMessage) message).getDesiredSize()) {
                        lobby.addPlayerInLobby(ch);
                        lobbyFound = true;
                        break;
                    }
                }
                if (!lobbyFound) {
                    Lobby lobby = createLobby(((LobbyRequestMessage) message).getDesiredSize(), ((LobbyRequestMessage) message).isDesiredAdvanceMode());
                    new Thread(lobby).start();
                    activeLobbies.add(lobby);
                }
                break;
            default:
                response = new ErrorMessage(message.getUUID(), "You've sent a message that the server can't understand", null);
                sendMessage(response);
                break;
        }
    }

    /**
     * @param message the Message that must be sent.
     */
    @Override
    public void sendMessage(Message message) throws IOException {
        //TODO: Prendi il client id dal messaggio, usa la mappa per poter associarlo al suo CH, chiama sendMessage di CH
        ClientHandler client = clientHandlerMap.get(message.getUUID());
        client.sendMessageToClient(message);
    }
}
