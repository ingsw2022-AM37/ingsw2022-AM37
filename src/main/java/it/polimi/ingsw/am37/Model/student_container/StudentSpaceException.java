package it.polimi.ingsw.am37.Model.student_container;

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
     * - tooMuch means num is too high for available space;
     * - tooFew means have been tried to remove too many students
     */
    private enum StudentSpaceExceptionType {
        TOOMUCH("Too much student to add"),
        TOOFEW("Too much student to remove");

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

    public StudentSpaceException(boolean tooMuch) {
        super();
        type = tooMuch ? StudentSpaceExceptionType.TOOMUCH : StudentSpaceExceptionType.TOOFEW;
    }

    public StudentSpaceException(String message, boolean tooMuch) {
        super(message);
        type = tooMuch ? StudentSpaceExceptionType.TOOMUCH : StudentSpaceExceptionType.TOOFEW;
    }

    public StudentSpaceException(String message, Throwable cause, boolean tooMuch) {
        super(message, cause);
        type = tooMuch ? StudentSpaceExceptionType.TOOMUCH : StudentSpaceExceptionType.TOOFEW;
    }

    public StudentSpaceException(Throwable cause, boolean tooMuch) {

        super(cause);

        type = tooMuch ? StudentSpaceExceptionType.TOOMUCH : StudentSpaceExceptionType.TOOFEW;
    }
}
