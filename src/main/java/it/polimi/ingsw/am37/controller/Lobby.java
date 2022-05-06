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
    private final ArrayList<ClientHandler> players;

    /**
     * Default constructor.
     */
    public Lobby(int lobbySize, boolean advancedMode) {
        this.lobbySize = lobbySize;
        this.advancedMode = advancedMode;
        this.players = new ArrayList<>();
        this.isGameReady = false;
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
     * Starts the game.
     */
    private void startGame() {
        //TODO: Start the game, GM is needed
    }

    /**
     * @return true if the player is found, otherwise false.
     */
    public boolean isPlayerInLobby(ClientHandler ch) {
        return players.contains(ch);
    }

    /**
     * @return True if the Lobby is full otherwise False
     */
    private boolean isFull() {
        return players.size() == lobbySize;
    }

    /**
     * @return the list of players connected in the lobby
     */
    public ArrayList<ClientHandler> getPlayers() {
        return players;
    }

    /**
     * adds the player in the Lobby and checks if the Lobby is full
     *
     * @param ch the Client to be added.
     */
    public void addPlayerInLobby(ClientHandler ch) {
        players.add(ch);
        isGameReady = isFull();
        //TODO: if(isGameReady) sendMessage(new StartGameMessage());
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
     * @return the flag that states if the Lobby is in advanced mode.
     */
    public boolean isAdvancedMode() {
        return advancedMode;
    }

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     */
    @Override
    public void onMessageReceived(Message message, ClientHandler ch) {
        //TODO: Waiting for Messages class
    }

    /**
     * @param message the Message that must be sent.
     */
    @Override
    public void sendMessage(Message message) {
        //TODO: Waiting for Messages class
    }
}
