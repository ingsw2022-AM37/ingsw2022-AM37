package it.polimi.ingsw.am37.message;

/**
 * Abstract class for representing the message and its basic functionality.
 */
public abstract class Message {

    /**
     * Identifier of the message sender
     */
    protected String UUID;

    /**
     * Identifier of the message type
     */
    protected MessageType messageType;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID        the default constructor
     * @param messageType the type of message
     */
    protected Message(String UUID, MessageType messageType) {
        this.UUID = UUID;
        this.messageType = messageType;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param messageType the message enum type
     */
    protected Message(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the identifier of the sender
     */
    public String getUUID() {
        return UUID;
    }

    /**
     * @param UUID set the sender identifier of this message
     */
    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    /**
     * @return the message type
     */
    public MessageType getMessageType() {
        return messageType;
    }
}