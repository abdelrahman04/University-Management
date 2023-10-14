package Exceptions;

public class CourseAlreadyRegisteredException extends StudentException {
    public CourseAlreadyRegisteredException(String message) {
        super(message);
    }

    public CourseAlreadyRegisteredException() {
        super("You already have this course registered");
    }
}
