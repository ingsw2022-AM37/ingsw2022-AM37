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
import java.util.Timer;
import java.util.TimerTask;

/**
 * It represents the Server that manage Players login and game Lobbies.
 */
public class Server implements MessageReceiver {

    /**
     * singleton server
     */
    public static Server server;

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
     * Keeps track of the matchIDs.
     */
    private static int matchIdCounter;

    /**
     * Default Constructor
     */
    public Server() {
        server = this;
        nicknames = new HashMap<>();
        activeLobbies = new ArrayList<>();
        clientHandlerMap = new HashMap<>();
        disconnectedClients = new HashMap<>();
        LOGGER = LogManager.getLogger(Server.class);
        matchIdCounter = 0;
    }

    /**
     * Loads the server with the given port.
     *
     * @param serverPort the given port.
     */
    public void loadServer(int serverPort) {
        LOGGER.printf(Level.OFF, "=====================================================Server Started=====================================================");
        new Thread(() -> {
            Timer timer = new Timer();
            Socket socket = null;
            try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
                LOGGER.info("Awaiting connections...");
                do {
                    try {
                        socket = serverSocket.accept();
                        LOGGER.info("Connection from " + socket + "!");
                    } catch (IOException e) {
                        LOGGER.error("Error encountered while trying to connect");
                        LOGGER.error(e.getMessage());
                    }
                    ClientHandler ch = new ClientHandler(socket);
                    ch.setMessageReceiver(this);
                    new Thread(ch).start();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message response = new ActiveLobbiesMessage(activeLobbies.stream().map(Lobby::getMatchID).toList());
                            ch.sendMessageToClient(response);
                        }
                    }, 500);

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
        LOGGER.info("Created a Lobby with matchID: " + ++matchIdCounter);
        return new Lobby(lobbySize, advancedMode, matchIdCounter);
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
                ch.setUUID(message.getUUID());
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
                Lobby lobbyFound = null;
                boolean lobbyFoundFlag = false;
                if (((LobbyRequestMessage) message).getDesiredSize() > 3) {
                    ch.disconnect();
                }
                for (Lobby lobby : activeLobbies) {
                    //TODO: THIS METHOD IS INVOLVED IN RESILIANCE, MUST BE TESTED
                    if (lobby.isPlayerInLobby(message.getUUID())) {
                        onReconnect(message.getUUID());
                        lobby.onReconnect(message.getUUID());
                    }
                    if (!lobby.isGameReady() && lobby.isAdvancedMode() == ((LobbyRequestMessage) message).isDesiredAdvanceMode() && lobby.getLobbySize() == ((LobbyRequestMessage) message).getDesiredSize()) {
                        lobby.addPlayerInLobby(message.getUUID(), ch, nicknames.get(message.getUUID()));
                        LOGGER.info("Lobby found, " + nicknames.get(message.getUUID()) + " entered lobby " + lobby.getMatchID());
                        lobbyFoundFlag = true;
                        lobbyFound = lobby;
                        ch.setMessageReceiver(lobby);
                        break;
                    }
                }
                if (!lobbyFoundFlag) {
                    lobbyFound = createLobby(((LobbyRequestMessage) message).getDesiredSize(), ((LobbyRequestMessage) message).isDesiredAdvanceMode());
                    new Thread(lobbyFound).start();
                    activeLobbies.add(lobbyFound);
                    lobbyFound.addPlayerInLobby(message.getUUID(), ch, nicknames.get(message.getUUID()));
                    LOGGER.info(nicknames.get(message.getUUID()) + " entered lobby " + lobbyFound.getMatchID());
                    ch.setMessageReceiver(lobbyFound);
                }
                for (Lobby lobby : activeLobbies) {
                    LOGGER.debug("Lobby " + lobby.getMatchID() + " status:\n- LobbySize: " + lobby.getLobbySize() + "\n- IsGameReady: " + lobby.isGameReady() + "\n- Players Connected: " + lobby.getPlayerNicknames().values());
                }
                response = new ConfirmMessage(message.getUUID(), lobbyFound.getMatchID());
                LOGGER.info("RequestLobby Response: Confirm Message");
                sendMessage(response);
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
     * Closes the lobby and deletes the nicknames from the server
     *
     * @param lobby the Lobby to be closed.
     */
    public void closeLobby(Lobby lobby) {
        while (lobby.getPlayers().size() > 0) {
            String uuidToRemove = lobby.getPlayers().keySet().iterator().next();
            clientHandlerMap.get(uuidToRemove).disconnect();
            onDisconnect(uuidToRemove);
        }
        nicknames.keySet().removeAll(lobby.getPlayerNicknames().keySet());
        activeLobbies.remove(lobby);
        LOGGER.info("Lobby " + lobby.getMatchID() + " closed");
    }

    /**
     * Reconnects the client in the Server.
     *
     * @param clientUUID the client that wants to reconnect.
     */
    private void onReconnect(String clientUUID) {
        ClientHandler clientToReconnect = disconnectedClients.get(clientUUID);
        clientHandlerMap.put(clientUUID, clientToReconnect);
        disconnectedClients.remove(clientUUID);

        LOGGER.info("Reconnected " + nicknames.get(clientUUID) + " on the Server");
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
        ClientHandler clientToDisconnect = clientHandlerMap.get(clientUUID);
        disconnectedClients.put(clientUUID, clientToDisconnect);
        clientHandlerMap.remove(clientUUID);

        boolean found = false;
        for (Lobby lobby : activeLobbies) {
            if (lobby.getPlayerNicknames().containsKey(clientUUID) && lobby.getPlayerNicknames().get(clientUUID).equals(nicknames.get(clientUUID))) {
                found = true;
                break;
            }
        }
        if (!found)
            nicknames.remove(clientUUID);

        if (nicknames.get(clientUUID) != null)
            LOGGER.info("Disconnected " + nicknames.get(clientUUID) + " from the Server");
        else
            LOGGER.info("Disconnected " + clientUUID + " from the Server");
    }
}
