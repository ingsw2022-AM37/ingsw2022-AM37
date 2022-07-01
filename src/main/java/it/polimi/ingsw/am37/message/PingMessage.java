package it.polimi.ingsw.am37.message;

/**
 * Simple message to check the connection
 */
public class PingMessage extends Message {
    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the client identifier
     */
    public PingMessage(String UUID) {
        super(UUID, MessageType.PING);
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     */
    public PingMessage() {
        super(MessageType.PING);
    }
}
