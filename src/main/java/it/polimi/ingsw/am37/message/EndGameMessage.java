package it.polimi.ingsw.am37.message;

/**
 * Message sent at the end of a match
 */
public class EndGameMessage extends Message {

    /**
     * The UUID of winner's client
     */
    private final String winnerUID;

    /**
     * The winner's nickname
     */
    private final String winnerNickname;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID           the client identifier
     * @param winnerUUID     the uuid of the winner
     * @param winnerNickname the nickname of the winner
     */
    public EndGameMessage(String UUID, String winnerUUID, String winnerNickname) {
        super(UUID, MessageType.END_GAME);
        this.winnerUID = winnerUUID;
        this.winnerNickname = winnerNickname;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param winnerUUID     the uuid of the winner
     * @param winnerNickname the nickname of the winner
     */
    public EndGameMessage(String winnerUUID, String winnerNickname) {
        super(MessageType.END_GAME);
        this.winnerUID = winnerUUID;
        this.winnerNickname = winnerNickname;
    }

    /**
     * @return the UUID of winner's client
     */
    public String getWinnerUUID() {
        return winnerUID;
    }

    /**
     * @return the winner's nickname
     */
    public String getWinnerNickname() {
        return winnerNickname;
    }
}
