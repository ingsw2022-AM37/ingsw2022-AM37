package it.polimi.ingsw.am37.message;

/**
 * This message is sent by server when a new player have to play. This message is send as broadcast to all the users to
 * notify the change. The player whose current turn was need to suspend everything.
 */
public class NextTurnMessage extends Message {

    /**
     * The UUID of the next player that has to play
     */
    private final String nextPlayerUUID;

    /**
     * The nickname of the next player that has to play
     */
    private final String nextPlayerNickname;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID               the client uuid
     * @param nextPlayerUUID     the next player's UUID
     * @param nextPlayerNickname the next player's nickname
     */
    public NextTurnMessage(String UUID, String nextPlayerUUID, String nextPlayerNickname) {
        super(UUID, MessageType.NEXT_TURN);
        this.nextPlayerUUID = nextPlayerUUID;
        this.nextPlayerNickname = nextPlayerNickname;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param nextPlayerUUID     the next player's UUID
     * @param nextPlayerNickname the next player's nickname
     */
    public NextTurnMessage(String nextPlayerUUID, String nextPlayerNickname) {
        super(MessageType.NEXT_TURN);
        this.nextPlayerUUID = nextPlayerUUID;
        this.nextPlayerNickname = nextPlayerNickname;
    }

    /**
     * @return the UUID of the next player
     */
    public String getNextPlayerUUID() {
        return nextPlayerUUID;
    }

    /**
     * @return the nickname of the next player
     */
    public String getNextPlayerNickname() {
        return nextPlayerNickname;
    }
}