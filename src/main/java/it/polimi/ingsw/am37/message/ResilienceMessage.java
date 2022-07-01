package it.polimi.ingsw.am37.message;

/**
 * Message used for notifying disconnection or re-connection
 */
public class ResilienceMessage extends Message {

    /**
     * A boolean to know if the client is disconnecting or reconnecting
     */
    private final boolean isDisconnecting;

    /**
     * The client's nickname
     */
    private final String nickname;

    /**
     * the time when the timer will expire
     */
    private final String expiring;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID            the client identifier
     * @param isDisconnecting A boolean to know if the client is disconnecting or reconnecting
     * @param nickname        The client's nickname
     * @param expiring        the time when the timer will expire
     */
    public ResilienceMessage(String UUID, boolean isDisconnecting, String nickname, String expiring) {
        super(UUID, MessageType.RESILIENCE);
        this.nickname = nickname;
        this.isDisconnecting = isDisconnecting;
        this.expiring = expiring;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param isDisconnecting A boolean to know if the client is disconnecting or reconnecting
     * @param nickname        The client's nickname
     * @param expiring        the time when the timer will expire
     */
    public ResilienceMessage(boolean isDisconnecting, String nickname, String expiring) {
        super(MessageType.RESILIENCE);
        this.nickname = nickname;
        this.isDisconnecting = isDisconnecting;
        this.expiring = expiring;
    }

    public boolean isDisconnecting() {
        return isDisconnecting;
    }

    public String getNickname() {
        return nickname;
    }

    public String getExpiring() {
        return expiring;
    }
}
