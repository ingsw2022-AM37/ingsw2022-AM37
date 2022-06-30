package it.polimi.ingsw.am37.model.exceptions;

/**
 * Exception thrown when there an action couldn't be performed due to size limitation in containers;
 */
public class StudentSpaceException extends RuntimeException{
    /**
     * Represent the type of exception
     * @see StudentSpaceExceptionType
     */
    StudentSpaceExceptionType type;

    /**
     * Enum to define possible type of Exception:
     * - tooMany means num is too high for available space;
     * - tooFew means have been tried to remove too many students
     */
    private enum StudentSpaceExceptionType {
        TOOMANY("Too many students to add"),
        TOOFEW("Too many students to remove");

        private String code;

        StudentSpaceExceptionType(String code) {
        }
    }

    /**
     * Override with type printing
     */
    @Override
    public String getMessage() {
        return super.getMessage()+"type: "+type.code;
    }

    public StudentSpaceException(boolean tooMany) {
        super();
        type = tooMany ? StudentSpaceExceptionType.TOOMANY : StudentSpaceExceptionType.TOOFEW;
    }

    public StudentSpaceException(String message, boolean tooMany) {
        super(message);
        type = tooMany ? StudentSpaceExceptionType.TOOMANY : StudentSpaceExceptionType.TOOFEW;
    }

    public StudentSpaceException(String message, Throwable cause, boolean tooMany) {
        super(message, cause);
        type = tooMany ? StudentSpaceExceptionType.TOOMANY : StudentSpaceExceptionType.TOOFEW;
    }

    public StudentSpaceException(Throwable cause, boolean tooMany) {

        super(cause);

        type = tooMany ? StudentSpaceExceptionType.TOOMANY : StudentSpaceExceptionType.TOOFEW;
    }
}
