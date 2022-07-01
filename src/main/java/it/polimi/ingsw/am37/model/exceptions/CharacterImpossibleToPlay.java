package it.polimi.ingsw.am37.model.exceptions;

/**
 * When you can't play a certain character because of the price or because you've already played one in your turn
 */
public class CharacterImpossibleToPlay extends RuntimeException {

    /**
     * Constructs a new runtime assistant impossible to play exception with the specified cause and a detail message of
     * {@code (cause==null ? null : cause.toString())} (which typically contains the class and detail message of {@code
     * cause}).
     *
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A {@code null }
     *              value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public CharacterImpossibleToPlay(Throwable cause) {
        super(cause);
    }

    public CharacterImpossibleToPlay(String message) {
        super(message);
    }
}
