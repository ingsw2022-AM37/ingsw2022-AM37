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
    private final List<Integer> lobbyIDs;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID     the default constructor
     * @param lobbyIDs the active lobbies IDs
     */
    public ActiveLobbiesMessage(String UUID, List<Integer> lobbyIDs) {
        super(UUID, MessageType.ACTIVE_LOBBIES);
        this.lobbyIDs = lobbyIDs;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param lobbyIDs the active lobbies IDs
     */
    public ActiveLobbiesMessage(List<Integer> lobbyIDs) {
        super(MessageType.ACTIVE_LOBBIES);
        this.lobbyIDs = lobbyIDs;
    }

    /**
     * @return the list of all started lobby on the server
     */
    public List<Integer> getLobbyIDs() {
        return lobbyIDs;
    }
}
