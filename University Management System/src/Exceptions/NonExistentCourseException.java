package Exceptions;

public class NonExistentCourseException extends SystemException{
    public NonExistentCourseException() {
        super("No course with this name");
    }

    public NonExistentCourseException(String message) {
        super(message);
    }
}
