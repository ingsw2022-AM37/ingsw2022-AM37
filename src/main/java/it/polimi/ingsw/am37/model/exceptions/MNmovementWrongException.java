package it.polimi.ingsw.am37.model.exceptions;

/**
 * When you try to move Mother Nature too far according to your last assistant
 */
public class MNmovementWrongException extends RuntimeException {

    public MNmovementWrongException(String message) {

        super(message);
    }
}
