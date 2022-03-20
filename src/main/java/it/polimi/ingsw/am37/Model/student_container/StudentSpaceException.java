package it.polimi.ingsw.am37.Model.student_container;

public class StudentSpaceException extends RuntimeException{
    StudentSpaceExceptionType type;

    private enum StudentSpaceExceptionType {
        TOOMUCH("Too much student to add"),
        TOOFEW("Too much student to remove");

        private String code;
        StudentSpaceExceptionType(String code) {
        }
    }

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
