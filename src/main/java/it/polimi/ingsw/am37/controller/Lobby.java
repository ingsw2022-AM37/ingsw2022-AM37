package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.message.Message;
import it.polimi.ingsw.am37.network.MessageReceiver;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.ArrayList;

/**
 * It represents the in game Lobby
 */
public class Lobby implements Runnable, MessageReceiver {

    /**
     * It saves how many Players can enter in the lobby.
     */
    private final int lobbysize;

    /**
     * It represents the client connected in the lobby and therefore the Players.
     */
    private final ArrayList<ClientHandler> players;

    /**
     * Default constructor.
     */
    public Lobby(int lobbysize) {
        this.lobbysize = lobbysize;
        this.players = new ArrayList<>();
    }

    /**
     *
     */
    @Override
    public void run() {
        //TODO: Add more logic
        startGame();
    }

    /**
     *
     */
    private void startGame() {
        //TODO: Start the game, GM is needed
    }

    /**
     * @param UUID the player that is needed to be found.
     * @return true if the player is found, otherwise false.
     */
    public boolean isPlayerInLobby(String UUID) {
        for (ClientHandler client : players) {
            if (client.getClientUUID().equals(UUID))
                return true;
        }
        return false;
    }

    /**
     * @return the list of players connected in the lobby
     */
    public ArrayList<ClientHandler> getPlayers() {
        return players;
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the message received.
     */
    @Override
    public void onMessageReceived(Message message) {
        //TODO: Waiting for Messages class
    }

    /**
     * @param message the message that must be sent
     */
    @Override
    public void sendMessage(Message message) {
        //TODO: Waiting for Messages class
    }
}
