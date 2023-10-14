package Account;

import Engine.UniversityManager;
import Exceptions.*;
import Material.Course;
import Material.Requests;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaAccount extends UserAccount implements Requests , Serializable {
    static final long serialVersionUID =
            -5849794470654667211L;
    private String specialization;
    private ArrayList<Course> courses=new ArrayList<>();

    public TaAccount(String email, String name, String password, String specialization) {
        super(email, name, password);
        this.specialization = specialization;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public ArrayList<Course> getCourses() {
        return courses;
    }
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public TaAccount() {
    }

    public void showCourses(){
        Stream<Course> stream= UniversityManager.getInstance().courses.stream();
        List x=stream.filter((c)->c.toString().contains(specialization)).collect(Collectors.toList());
        x.forEach(o -> System.out.println(o.toString()));
    }
    public void registerCourse(String name) throws NonExistentCourseException, MaxCoursesException, CourseHasInstructorException, SemesterRunningException {
        AtomicBoolean found=new AtomicBoolean(false);
        AtomicReference<Course> Course=new AtomicReference<>();
        UniversityManager.getInstance().courses.forEach(course->{
            if(course.getName().equals(name)){
                Course.set(course);
                found.set(true);
            }
        });
        if(found.get()){
            registerCourse(Course.get());
        }
        if(!found.get()){
            throw new NonExistentCourseException();
        }
    }
    public boolean registerCourse(@NotNull Course course) throws MaxCoursesException, CourseHasInstructorException, SemesterRunningException {
        if(UniversityManager.getInstance().semesterRunning){
            throw new SemesterRunningException();
        }
        if(course.getInstructor()!=null){
            throw new CourseHasInstructorException();
        }
        if(this.getCourses().size()==3){
            throw new MaxCoursesException();
        }
        course.setInstructor(this);
        this.courses.add(course);
        System.out.println("Registered course successfully");
        return true;
    }
    public void removeCourse(String name) throws CourseUnavailableException {
        AtomicBoolean found=new AtomicBoolean(false);
        this.getCourses().forEach(course->{
            if(course.getName().equals(name)){
                removeCourse(course);
                found.set(true);
            }
        });
        if(!found.get()){
            throw new CourseUnavailableException();
        }
    }

    public void removeCourse(Course course){
        ArrayList<StudentAccount> Student_List = course.getStudents_enrolled();
        Student_List.forEach(student-> student.removeCourse(course));
        UniversityManager.getInstance().courses.remove(course);
        courses.remove(course);
    }

    public void showMyCourses(){
        courses.forEach(course->System.out.println(course.toString()));
    }

    public void View_My_Students(){
        for(int i=0;i<courses.size();++i){
            System.out.println("student for the "+(i+1)+" course\n"+courses.get(i));
            var students=courses.get(i).getStudents_enrolled();
            students.forEach(student->System.out.println(student.toString()));
        }
    }

    @Override
    public String printRequest() {
        return String.format("Teacher Name: %s\nSpecialization: %s",getName(),getSpecialization());
    }

    public static class TeacherBuilder {
        public TaAccount teacher;
        public TeacherBuilder(){

        }

        public TeacherBuilder createTeacher(){
            teacher=new TaAccount();
            return this;
        }
        public TaAccount getTeacher(){
            return teacher;
        }
        public TeacherBuilder setName(){
            String name=UniversityManager.getInstance().getField("Name");
            teacher.setName(name);
            return this;
        }

        public TeacherBuilder setPassword(){
            String password=UniversityManager.getInstance().getField("password");
            teacher.setPassword(password);
            return this;
        }
        public TeacherBuilder setEmail(){
            String email=UniversityManager.getInstance().getField("email");
            teacher.setEmail(email);
            return this;
        }
        public TeacherBuilder setSpecialization(){
            String specialization=UniversityManager.getInstance().getField("specialization");
            teacher.setSpecialization(specialization);
            return this;
        }



    }
}
