package it.polimi.ingsw.am37.message;

/**
 * This is a message sent by the client when try to log in to the server. This message carry the user desired nickname.
 * The server must reply with a {@link ErrorMessage} when the nickname is already taken or with {@link ConfirmMessage}
 * when authorize login.
 */
public class LoginMessage extends Message {


    /**
     * The nickname desired to log in the server
     */
    private final String nickname;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the client identifier
     */
    public LoginMessage(String UUID, String nickname) {
        super(UUID, MessageType.LOGIN);
        this.nickname = nickname;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param messageType the message enum type
     */
    public LoginMessage(MessageType messageType, String nickname) {
        super(messageType);
        this.nickname = nickname;
    }

    /**
     * @return the desired nickname of this message
     */
    public String getNickname() {
        return nickname;
    }

}