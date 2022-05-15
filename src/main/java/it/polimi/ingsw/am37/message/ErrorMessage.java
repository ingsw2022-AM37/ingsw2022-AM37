package it.polimi.ingsw.am37.message;


/**
 * This message is used to comunicate that an error occur when last request have been performed. See the
 * {@link ErrorMessage#getMessage()} to have additional information over the error occurred
 */
public class ErrorMessage extends Message {

    /**
     * This is a small text description of the error, useful to display a human-readable cause of the error.
     */
    private final String message;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the client identifier
     */
    public ErrorMessage(String UUID, String message) {
        super(UUID, MessageType.ERROR);
        this.message = message;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param message the message that explain the error
     */
    public ErrorMessage(String message) {
        super(MessageType.ERROR);
        this.message = message;
    }

    /**
     * @return human-readable explanation of the error cause
     */
    public String getMessage() {
        return this.message;
    }

}