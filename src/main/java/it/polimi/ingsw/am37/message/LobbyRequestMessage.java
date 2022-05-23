package it.polimi.ingsw.am37.message;

/**
 * This message is used to request to join a lobby providing desired settings, such as advanced rules enabled or the
 * number of players. This message should be answered with a {@link ConfirmMessage} with the lobby id of joined one, or
 * in case of errors answer with a {@link ErrorMessage}.
 */
public class LobbyRequestMessage extends Message {

    /**
     * The desired size for the lobby
     */
    private final int desiredSize;

    /**
     * The desired settings for the game. Use {@code true} for advanced mode or otherwise {@code false} (the default)
     * for basic rule
     */
    private final boolean desiredAdvanceMode;


    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the client identifier
     */
    public LobbyRequestMessage(String UUID, int desiredSize, boolean desiredAdvanceMode) {
        super(UUID, MessageType.LOBBY_REQUEST);
        this.desiredSize = desiredSize;
        this.desiredAdvanceMode = desiredAdvanceMode;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param desiredSize        the desired size
     * @param desiredAdvanceMode flag for the desired advance mode
     */
    public LobbyRequestMessage(int desiredSize, boolean desiredAdvanceMode) {
        super(MessageType.LOBBY_REQUEST);
        this.desiredSize = desiredSize;
        this.desiredAdvanceMode = desiredAdvanceMode;
    }

    /**
     * @return the desired lobby size
     */
    public int getDesiredSize() {
        return desiredSize;
    }

    /**
     * @return the flag for enable or disable advance mode
     */
    public boolean isDesiredAdvanceMode() {
        return desiredAdvanceMode;
    }
}