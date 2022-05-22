package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.Lobby;
import it.polimi.ingsw.am37.message.*;
import it.polimi.ingsw.am37.network.MessageReceiver;
import it.polimi.ingsw.am37.network.exceptions.InternetException;
import org.apache.logging.log4j.Level;
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
        LOGGER.printf(Level.OFF, "=====================================================Server Started=====================================================");
        new Thread(() -> {
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
        int matchID = new Random().nextInt(1024);
        do {
            for (Lobby lobby : activeLobbies) {
                if (lobby.getMatchID() == matchID) {
                    matchID = new Random().nextInt(1024);
                    generated = true;
                }
            }
        } while (generated);
        LOGGER.info("Created a Lobby with matchID: " + matchID);
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
            case LOGIN -> {
                LOGGER.info("Received LoginMessage");
                if (!nicknames.containsValue(((LoginMessage) message).getNickname())) {
                    clientHandlerMap.put(message.getUUID(), ch);
                    nicknames.put(message.getUUID(), ((LoginMessage) message).getNickname());
                    response = new ConfirmMessage(message.getUUID());
                    LOGGER.info("LoginMessage Response: Confirm Message");
                    sendMessage(response);
                } else {
                    response = new ErrorMessage(message.getUUID(), "Nickname already used");
                    LOGGER.error("LoginMessage Response: Nickname already used");
                    ch.sendMessageToClient(response);
                }
            }
            case LOBBY_REQUEST -> {
                LOGGER.info("Received LobbyRequestMessage");
                if (((LobbyRequestMessage) message).getDesiredSize() > 3)
                    ch.disconnect();
                boolean lobbyFound = false;
                for (Lobby lobby : activeLobbies) {
                    if (!lobby.isGameReady() && lobby.isAdvancedMode() == ((LobbyRequestMessage) message).isDesiredAdvanceMode() && lobby.getLobbySize() == ((LobbyRequestMessage) message).getDesiredSize()) {
                        lobby.addPlayerInLobby(message.getUUID(), ch, nicknames.get(message.getUUID()));
                        LOGGER.info("Lobby found, " + nicknames.get(message.getUUID()) + " entered lobby " + lobby.getMatchID());
                        lobbyFound = true;
                        ch.setMessageReceiver(lobby);
                        break;
                    }
                }
                if (!lobbyFound) {
                    Lobby lobby = createLobby(((LobbyRequestMessage) message).getDesiredSize(), ((LobbyRequestMessage) message).isDesiredAdvanceMode());
                    new Thread(lobby).start();
                    activeLobbies.add(lobby);
                    lobby.addPlayerInLobby(message.getUUID(), ch, nicknames.get(message.getUUID()));
                    LOGGER.info(nicknames.get(message.getUUID()) + "entered lobby " + lobby.getMatchID());
                    ch.setMessageReceiver(lobby);
                }
                for (Lobby lobby : activeLobbies) {
                    LOGGER.debug("Lobby " + lobby.getMatchID() + " status:\n- LobbySize: " + lobby.getLobbySize() + "\n- IsGameReady: " + lobby.isGameReady() + "\n- Players Connected: " + lobby.getPlayerNicknames().values());
                }
            }
            default -> {
                response = new ErrorMessage(message.getUUID(), "You've sent a message that the server can't " +
                        "understand");
                LOGGER.error("A message has been sent that the server can't understand: " + message.getMessageType());
                ch.sendMessageToClient(response);
            }
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

        // TODO: Se rimane attivo un solo giocatore, il gioco viene sospeso fino a che non si ricollega almeno un altro
        // giocatore oppure scade un timeout

        ClientHandler clientToDisconnect = clientHandlerMap.get(clientUUID);
        disconnectedClients.put(clientUUID, clientToDisconnect);
        clientHandlerMap.remove(clientUUID);

        //TODO: I nickname non vanno gestiti, li elimino solo a fine partita; solo se non sono ancora in una lobby e si disconnette allora posso eliminare il nickname
        //nicknames.remove(clientUUID);

        Lobby lobbyContainingClient;
        for (Lobby lobby : activeLobbies) {
            if (lobby.isPlayerInLobby(clientUUID)) {
                lobbyContainingClient = lobby;
                lobbyContainingClient.onDisconnect(clientUUID);
                break;
            } else
                System.err.println("Unable to find the Client in any Lobby");
        }

        //TODO: onReconnect forzare l'aggiunta del player che si è riconnesso in orderPlayed (o assistantPlayed) nel GM, altrimenti si rompe.
        clientToDisconnect.disconnect();
    }
}
