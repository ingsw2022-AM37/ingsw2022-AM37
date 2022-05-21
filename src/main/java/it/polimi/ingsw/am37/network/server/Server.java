package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.Lobby;
import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.network.MessageReceiver;
import it.polimi.ingsw.am37.network.exceptions.InternetException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * It represents the Server that manage Players login and game Lobbies.
 */
public class Server implements MessageReceiver {

    /**
     * A Logger.
     */
    private static Logger LOGGER;

    /**
     * Associates the UUID of the Client with the ClientHandler.
     */
    private static HashMap<String, ClientHandler> clientHandlerMap;

    /**
     * List to keep track of active Lobbies.
     */
    private static ArrayList<Lobby> activeLobbies;

    /**
     * Map to keep track of the players' nicknames via UUID.
     */
    private static HashMap<String, String> nicknames;

    /**
     * Keeps track of the disconnected clients
     */
    private static HashMap<String, ClientHandler> disconnectedClients;

    /**
     * Default Constructor
     */
    public Server() {
        nicknames = new HashMap<>();
        activeLobbies = new ArrayList<>();
        clientHandlerMap = new HashMap<>();
        LOGGER = LogManager.getLogger(Server.class);
    }

    /**
     * Loads the server with the given port.
     *
     * @param serverPort the given port.
     */
    public void loadServer(int serverPort) {
        new Thread(() -> {
            //TODO: LOGGER
            Socket socket = null;
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                do {
                    try {
                        LOGGER.info("Awaiting connections...");
                        socket = serverSocket.accept();
                        LOGGER.info("Connection from " + socket + "!");
                    } catch (IOException e) {
                        LOGGER.error("Error encountered while trying to connect");
                        LOGGER.error(e.getMessage());
                    }
                    ClientHandler ch = new ClientHandler(socket);
                    ch.setMessageReceiver(this);
                    new Thread(ch).start();
                } while (!serverSocket.isClosed());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Creates the Lobby.
     *
     * @param lobbySize    the size of the lobby
     * @param advancedMode flag to turn on advanced mode
     */
    private Lobby createLobby(int lobbySize, boolean advancedMode) {
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
                if (!nicknames.containsValue(((LoginMessage) message).getNickname())) {
                    clientHandlerMap.put(message.getUUID(), ch);
                    nicknames.put(message.getUUID(), ((LoginMessage) message).getNickname());
                    response = new ConfirmMessage(message.getUUID());
                    sendMessage(response);
                } else {
                    response = new ErrorMessage(message.getUUID(), "Nickname already used");
                    ch.sendMessageToClient(response);
                }
                break;
            case LOBBY_REQUEST:
                if (((LobbyRequestMessage) message).getDesiredSize() > 3)
                    ch.disconnect();
                boolean lobbyFound = false;
                for (Lobby lobby : activeLobbies) {
                    if (!lobby.isGameReady() && lobby.isAdvancedMode() == ((LobbyRequestMessage) message).isDesiredAdvanceMode() && lobby.getLobbySize() == ((LobbyRequestMessage) message).getDesiredSize()) {
                        lobby.addPlayerInLobby(message.getUUID(), ch, nicknames.get(message.getUUID()));
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
                    lobby.addPlayerInLobby(message.getUUID(), ch, nicknames.get(message.getUUID()));
                    ch.setMessageReceiver(lobby);
                }
                for (Lobby lobby:activeLobbies) {
                    System.out.println("LobbyId: " + lobby.getMatchID() + " LobbySize: " + lobby.getLobbySize() + "is game ready: " + lobby.isGameReady());
                    for (String s : lobby.getPlayers().keySet()) {
                        System.out.println("nickname: " + nicknames.get(s));
                    }
                }
                break;
            default:
                response = new ErrorMessage(message.getUUID(), "You've sent a message that the server can't " +
                        "understand");
                ch.sendMessageToClient(response);
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

    /**
     * Perform actions when client wants to disconnect
     *
     * @param clientUUID the UUID of the client to disconnect.
     */
    @Override
    public void onDisconnect(String clientUUID) {
        //TODO: Va rimosso il player e il suo nickname? No se vogliamo fare la Resilienza, semplicemente lo si disattiva somehow
        // gestire poi anche cosa fare con le sue informazioni nel server.
        // Se si vuole gestire la resilienza le sue informazioni non andranno eliminate lato server.
        // ma sarà necessario toglierlo dal model
        ClientHandler clientToDisconnect = clientHandlerMap.get(clientUUID);
        disconnectedClients.put(clientUUID, clientToDisconnect);
        clientHandlerMap.remove(clientUUID);
        //TODO: Timer per gestire che se il client non si riconnette allora il nickname può essere liberato
        // Nickname da togliere anche dalla lobby
        nicknames.remove(clientUUID);
        Lobby lobbyContainingClient;
        for (Lobby lobby : activeLobbies) {
            if(lobby.isPlayerInLobby(clientUUID)){
                lobbyContainingClient = lobby;
                lobbyContainingClient.onDisconnect(clientUUID);
                break;
            }
            else
                System.err.println("Unable to find the Client in any Lobby");
        }
        clientToDisconnect.disconnect();
    }
}
