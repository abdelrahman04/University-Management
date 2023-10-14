package Exceptions;

public class CourseFullException extends StudentException{
    public CourseFullException(String message) {
        super(message);
    }

    public CourseFullException() {
        super("you have reached max courses to teach");
    }
}
