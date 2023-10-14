package Account;

import Engine.UniversityManager;
import Exceptions.*;
import Material.Course;
import Material.Grade;
import Material.Requests;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StudentAccount extends UserAccount implements Requests, Serializable {
    static final long serialVersionUID =
            -5849794470654667210L;
    private String id;
    private double balance;
    private ArrayList<Course> courses=new ArrayList<>();
    private boolean financialAided =false;
    private HashMap<Course, Grade> grades=new HashMap<>();
    private ArrayList<HashMap<Course, Grade>> allGrades =new ArrayList<>();
    private int creditHours =0;

    public StudentAccount() {
        this.id=UUID.randomUUID().toString().replace("-", "");
    }

    public StudentAccount(String email, String name, String password, double balance) {
        super(email, name, password);
        this.balance = balance;
        this.id=UUID.randomUUID().toString().replace("-", "");
    }
    public boolean isFinancialAided() {
        return financialAided;
    }
    public void setFinancialAided(boolean financialAided) {
        this.financialAided = financialAided;
    }
    public String getId() {
        return id;
    }
    /*public void setId(String id) {
        this.id = id;
    }*/
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public ArrayList<Course> getCourses() {
        return courses;
    }
    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
    public HashMap<Course, Grade> getGrades() {
        return grades;
    }
    public void setGrades(HashMap<Course, Grade> grades) {
        this.grades = grades;
    }

    public ArrayList<HashMap<Course, Grade>> getAllGrades() {
        return allGrades;
    }

    public void setAllGrades(ArrayList<HashMap<Course, Grade>> allGrades) {
        this.allGrades = allGrades;
    }

    public int getCreditHours() {
        return creditHours;
    }
    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    public void registerCourse(String name) throws CourseAlreadyRegisteredException, SemesterRunningException, NotEnoughCreditsException, CourseFullException, NonExistentCourseException {
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
    public boolean registerCourse(@NotNull Course course) throws SemesterRunningException, CourseFullException, CourseAlreadyRegisteredException, NotEnoughCreditsException {

        if(UniversityManager.getInstance().semesterRunning){
            throw new SemesterRunningException();
        }
        if(course.getStudents_enrolled().size()>=course.getMax_students()){
            throw new CourseFullException();
        }

        if(course.getCredit_hours()*1000>this.getBalance()&&!financialAided) {
            throw new NotEnoughCreditsException();
        }
        if(grades.containsKey(course)) {
            throw new CourseAlreadyRegisteredException();
        }
        AtomicBoolean Found=new AtomicBoolean(false);
        allGrades.forEach(Grade->{
            if(Grade.containsKey(course)&&Grade.get(course)!=Material.Grade.F)
                Found.set(true);
        });
        if(Found.get()) {
            throw new CourseAlreadyRegisteredException();
        }
        course.getStudents_enrolled().add(this);
        this.courses.add(course);
        grades.put(course, Grade.NULL);
        if(!financialAided)
        this.setBalance(this.balance-course.getCredit_hours()*100);
        this.setCreditHours(getCreditHours()+course.getCredit_hours());
        System.out.println("registered course successfully");
        return true;
    }

    public void removeCourse(Course course){
        this.courses.remove(course);
        grades.remove(course);
        if(!financialAided)
            this.setBalance(this.balance+course.getCredit_hours()*100);
        this.setCreditHours(getCreditHours()-course.getCredit_hours());
    }

    public void showMyCourses(){
        for (int i = 0; i < getCourses().size(); ++i) {
            System.out.println(courses.get(i).toString());
        }
    }
    public void show_grades(){
        grades.forEach((c,g)->{
            System.out.println(c.toString()+"Material.Grade: "+g+"\n");
        });
    }

    @Override
    public String toString() {
        return "Student Name: "+getName()+"\nId: "+id;
    }

    @Override
    public String printRequest() {
        return String.format("Student Name: %s\nId: %s\nBalance: %f",getName(),getId(),getBalance());
    }
    public static class StudentBuilder {
        public StudentAccount student;
        public StudentBuilder(){
            student=new StudentAccount();
        }


        public StudentAccount getStudent(){
            return student;
        }
        public StudentBuilder setName(){
            String name=UniversityManager.getInstance().getField("Name");
            student.setName(name);
            return this;

        }
        public StudentBuilder setEmail(){
            String email=UniversityManager.getInstance().getField("email");
            student.setEmail(email);
            return this;
        }
        public StudentBuilder setPassword(){
            String password=UniversityManager.getInstance().getField("password");
            student.setPassword(password);
            return this;
        }
        public StudentBuilder setBalance(){
            Double balance=UniversityManager.getInstance().getDoubleField("balance");
            student.setBalance(balance);
            return this;
        }


    }
}
