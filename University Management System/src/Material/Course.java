package Material;

import Account.StudentAccount;
import Account.TaAccount;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable {
    static final long serialVersionUID =
            -5849794470654667212L;
    private String name;
    private TaAccount Instructor;
    private int credit_hours;
    private int max_students;
    private ArrayList<StudentAccount> students_enrolled=new ArrayList<>();
    private String specialization;

    public Course(String name, int credit_hours, int max_students, String specialization) {
        this.name = name;
        this.credit_hours = credit_hours;
        this.max_students = max_students;
        this.specialization = specialization;
    }

    public Course() {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public TaAccount getInstructor() {
        return Instructor;
    }
    public void setInstructor(TaAccount instructor) {
        Instructor = instructor;
    }
    public int getCredit_hours() {
        return credit_hours;
    }
    public void setCredit_hours(int credit_hours) {
        this.credit_hours = credit_hours;
    }
    public int getMax_students() {
        return max_students;
    }
    public void setMax_students(int max_students) {
        this.max_students = max_students;
    }
    public ArrayList<StudentAccount> getStudents_enrolled() {
        return students_enrolled;
    }
    public void setStudents_enrolled(ArrayList<StudentAccount> students_enrolled) {
        this.students_enrolled = students_enrolled;
    }
    public String getSpecialization() {
        return specialization;
    }
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    public String toString(){
        return String.format("Course Name: %s\nCredit Hours: %d\nSpecialization: %s\n",name,credit_hours,specialization);
    }


}
