package it.polimi.ingsw.am37.message;

/**
 * This message is sent by server when a new player have to play. This message is send as broadcast to all the users to
 * notify the change. The player whose current turn was need to suspend everything.
 */
public class NextTurnMessage extends Message {

    /**
     * The UUID of the next player that has to play
     */
    private String nextPlayerUUID;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID        the default constructor
     * @param messageType the type of message
     */
    protected NextTurnMessage(String UUID, MessageType messageType) {
        super(UUID, MessageType.NEXT_TURN);
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param messageType the message enum type
     */
    protected NextTurnMessage(MessageType messageType) {
        super(messageType);
    }

    /**
     * @return the UUID of the next player
     */
    public String getNextPlayerUUID() {
        return nextPlayerUUID;
    }
}