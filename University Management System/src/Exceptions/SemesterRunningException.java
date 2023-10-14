package Exceptions;

public class SemesterRunningException extends SystemException{
    public SemesterRunningException() {
        super("Semester is Running");
    }

    public SemesterRunningException(String message) {
        super(message);
    }
}
