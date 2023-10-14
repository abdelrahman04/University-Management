package Exceptions;

public class CourseHasInstructorException extends TeacherException{
    public CourseHasInstructorException() {
        super("Course already has a Teacher");
    }

    public CourseHasInstructorException(String message) {
        super(message);
    }
}
