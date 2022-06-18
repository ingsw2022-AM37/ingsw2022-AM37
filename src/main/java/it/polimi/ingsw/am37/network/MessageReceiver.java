package it.polimi.ingsw.am37.network;

import it.polimi.ingsw.am37.message.Message;
import it.polimi.ingsw.am37.network.exceptions.InternetException;
import it.polimi.ingsw.am37.network.server.ClientHandler;

/**
 * This interface is used by the ClientHandler to perform an action and send a message either to the Lobby or the Server.
 */
public interface MessageReceiver {

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the Message received.
     * @param ch      the ClientHandler that calls the method.
     */
    void onMessageReceived(Message message, ClientHandler ch) throws InternetException;

    /**
     * @param message the message that must be sent
     */
    void sendMessage(Message message) throws InternetException;

    /**
     * Perform actions when client wants to disconnect
     *
     * @param UUID the client uuid to disconnect.
     */
    void onDisconnect(String UUID);
}
