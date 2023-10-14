package Exceptions;

public class MaxCoursesException extends TeacherException{
    public MaxCoursesException() {
        super("max enrolled students reached");
    }

    public MaxCoursesException(String message) {
        super(message);
    }
}
