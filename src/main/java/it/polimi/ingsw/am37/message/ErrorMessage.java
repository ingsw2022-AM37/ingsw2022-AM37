package it.polimi.ingsw.am37.message;


/**
 * This message is used to comunicate that an error occur when last request have been performed. See the
 * {@link ErrorMessage#getCause()} to catch the exception or {@link ErrorMessage#getMessage()} to have additional
 * information over the error occurred
 */
public class ErrorMessage extends Message {

    /**
     * This is a small text description of the error, useful to display a human-readable cause of the error.
     */
    private final String message;

    /**
     * This is the exception that caused the error, if any have been raised.
     */
    private final Throwable cause;

    /**
     * The fromJSON receiver side constructor where all data are accessible
     *
     * @param UUID the default constructor
     */
    public ErrorMessage(String UUID, String message, Throwable cause) {
        super(UUID, MessageType.ERROR);
        this.message = message;
        this.cause = cause;
    }

    /**
     * The default sender side constructor for message preparing. UUID must be set using {@link Message#setUUID(String)}
     * before sending it
     *
     * @param message the message that explain the error
     * @param cause   the exception thrown when performing requested action (maybe null if any have been thrown)
     */
    public ErrorMessage(String message, Throwable cause) {
        super(MessageType.ERROR);
        this.message = message;
        this.cause = cause;
    }

    /**
     * @return human-readable explanation of the error cause
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return exception thrown when request action have been performed
     */
    public Throwable getCause() {
        return this.cause;
    }

}