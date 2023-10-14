package Account;

import Exceptions.*;
import Material.Course;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public abstract class UserAccount implements Accounts , Serializable {
    static final long serialVersionUID =
            -5849794470654667215L;
    private String email;
    private String name;
    private String password;

    public String getPassword() {
        return password;
    }

    public UserAccount(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    void setName(String name) {
        this.name = name;
    }

    void setPassword(String password) {
        this.password = password;
    }

    public abstract void showMyCourses();
    public abstract boolean registerCourse(@NotNull Course course) throws SemesterRunningException, CourseFullException, CourseAlreadyRegisteredException, NotEnoughCreditsException, MaxCoursesException, CourseHasInstructorException;
    public abstract void registerCourse(String name) throws NonExistentCourseException, MaxCoursesException, CourseHasInstructorException, CourseAlreadyRegisteredException, SemesterRunningException, NotEnoughCreditsException, CourseFullException;
    public UserAccount() {
    }

    public abstract void removeCourse(Course course);

    public boolean compare(String user,String password){
        return this.getName().equals(user)&&password.equals(this.password);
    }

}