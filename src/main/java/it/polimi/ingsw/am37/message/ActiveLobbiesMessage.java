package it.polimi.ingsw.am37.message;

import java.util.List;

/**
 * This message is provided to advertise all open lobby to the client when tries to connect. This should let the client
 * understand if the lobby to which it was connected is open or not and act accordingly
 */
public class ActiveLobbiesMessage extends Message {

    /**
     * The list of all running lobbies in the server
     */
    private final List<Integer> lobbyIds;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID        the default constructor
     * @param messageType the type of message
     */
    public ActiveLobbiesMessage(String UUID, MessageType messageType, List<Integer> lobbyIds) {
        super(UUID, messageType);
        this.lobbyIds = lobbyIds;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param messageType the message enum type
     */
    public ActiveLobbiesMessage(MessageType messageType, List<Integer> lobbyIds) {
        super(messageType);
        this.lobbyIds = lobbyIds;
    }

    /**
     * @return the list of all started lobby on the server
     */
    public List<Integer> getLobbyIds() {
        return lobbyIds;
    }
}
