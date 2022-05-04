package it.polimi.ingsw.am37.network;

import it.polimi.ingsw.am37.message.Message;

/**
 * This interface is used by the ClientHandler to perform an action and send a message either to the Lobby or the Server.
 */
public interface MessageReceiver {

    /**
     * When a message is received perform a specific action based on the Message type.
     *
     * @param message the message received.
     */
    void onMessageReceived(Message message);

    /**
     * @param message the message that must be sent
     */
    void sendMessage(Message message);
}
