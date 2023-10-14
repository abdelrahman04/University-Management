package Engine;

import Account.TaAccount;
import Exceptions.CourseUnavailableException;
import Material.Course;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class TeacherCommandLine implements CommandLine{
    private volatile static TeacherCommandLine teacherCommandLine;
    private TeacherCommandLine(){
        help();
    }
    public static TeacherCommandLine getInstance() {
        if (teacherCommandLine == null) {
            synchronized (TeacherCommandLine.class) {
                if (teacherCommandLine == null) {
                    teacherCommandLine = new TeacherCommandLine();
                }
            }
        }
        return teacherCommandLine;
    }
    public void commandline(String input){
        int x;
        switch(input){
            case "1":help();break;
            case "2":
                UniversityManager.getInstance().showAllCourses();break;
            case "3":
                UniversityManager.getInstance().showMyCourses();break;
            case "4":
                UniversityManager.getInstance().registerCourse();break;
            case "5":withdrawCourse();break;
            case "6":viewMyStudents();break;
            case "7":
                UniversityManager.getInstance().signOut();break;
            default:
                UniversityManager.getInstance().undefinedCommand();
        }

    }
    public void help(){
        System.out.println("please enter the number of the command you want\nCommands:\n1-help\n2-show all courses\n3-show my courses\n4-register course\n5-withdraw course\n6-view all my students\n7-sign out");
    }
    private void withdrawCourse(){
        if(UniversityManager.getInstance().semesterRunning){
            System.out.println("semester running");
            return;
        }
        try {
            System.out.println("enter your the name of the course you want to withdraw: ");
            String name= UniversityManager.getInstance().Reader.readLine();
            ((TaAccount) UniversityManager.getInstance().currentUser).removeCourse(name);



        } catch (IOException e) {
            System.out.println("No Input received");
        }catch(NumberFormatException e){
            System.out.println("Wrong format");
        } catch (CourseUnavailableException e) {
            System.out.println("No such course");
        }


    }

    private void viewMyStudents(){
        ((TaAccount) UniversityManager.getInstance().currentUser).View_My_Students();
    }


}
