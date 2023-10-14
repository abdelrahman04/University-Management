package Engine;

import Account.StudentAccount;

import java.io.IOException;

public class StudentCommandLine implements CommandLine{
    private volatile static StudentCommandLine studentCommandLine;
    private StudentCommandLine(){
        help();
    }
    public static StudentCommandLine getInstance() {
        if (studentCommandLine == null) {
            synchronized (StudentCommandLine.class) {
                if (studentCommandLine == null) {
                    studentCommandLine = new StudentCommandLine();
                }
            }
        }
        return studentCommandLine;
    }
    public void commandline(String input){
        switch(input){
            case "1":help();break;
            case "2":
                UniversityManager.getInstance().showAllCourses();break;
            case "3":
                UniversityManager.getInstance().showMyCourses();break;
            case "4":
                UniversityManager.getInstance().registerCourse();break;
            case "5":submitFinancialRequest();break;
            case "6":showGrades();break;
            case "7":
                UniversityManager.getInstance().signOut();break;
            default:
                UniversityManager.getInstance().undefinedCommand();

        }
    }
    public void help(){
        System.out.println("please enter the number of the command you want\nCommands:\n1-help\n2-show all courses\n3-show my courses\n4-register course\n5-submit financial request\n6-show grades\n7-sign out");
    }
    private void showGrades(){
        try {
            System.out.println("which semester do you want to show your grades");
            int semester= Integer.parseInt(UniversityManager.getInstance().Reader.readLine());
            if(semester>UniversityManager.getInstance().semesterCount||semester<1||semester<=UniversityManager.getInstance().semesterCount-((StudentAccount) UniversityManager.getInstance().currentUser).getAllGrades().size()) {
                System.out.println("No such semester");
                return;
            }
            if(UniversityManager.getInstance().semesterCount == semester)
                if(UniversityManager.getInstance().semesterRunning)
                    System.out.println("grades not yet published");
                else
                    ((StudentAccount) UniversityManager.getInstance().currentUser).getAllGrades().get(semester-1-(UniversityManager.getInstance().semesterCount-((StudentAccount) UniversityManager.getInstance().currentUser).getAllGrades().size())).forEach(((course, grade) -> System.out.println("course: "+course.getName()+" Grade: "+grade)));
            else{
                ((StudentAccount) UniversityManager.getInstance().currentUser).getAllGrades().get(semester-1-(UniversityManager.getInstance().semesterCount-((StudentAccount) UniversityManager.getInstance().currentUser).getAllGrades().size())).forEach(((course, grade) -> System.out.println("course: "+course.getName()+" Grade: "+grade)));
            }
        }catch (IOException e) {
            System.out.println("No Input received");
        }catch(NumberFormatException e){
            System.out.println("Wrong format");
        }
    }














    private void submitFinancialRequest(){

        if(((StudentAccount) UniversityManager.getInstance().currentUser).isFinancialAided())
            System.out.println("You are already financial aided");
        else
            UniversityManager.getInstance().financialRequests.add((StudentAccount) UniversityManager.getInstance().currentUser);

    }
}
