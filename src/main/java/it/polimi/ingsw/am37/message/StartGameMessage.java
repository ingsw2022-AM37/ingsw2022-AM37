package it.polimi.ingsw.am37.message;

/**
 * This message is sent by the lobby when is ready to accept players moves. Clients must wait until this message and the
 * following {@link NextTurnMessage} with the userUUID of the first player to move. No response by client is expected
 */
public class StartGameMessage extends Message {

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the client identifier
     */
    public StartGameMessage(String UUID) {
        super(UUID, MessageType.START_GAME);
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     */
    public StartGameMessage() {
        super(MessageType.START_GAME);
    }
}
