package Engine;

import Account.Admin;
import Account.StudentAccount;
import Account.TaAccount;
import Material.Course;
import Material.Grade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AdminCommandLine implements CommandLine{

    public static Grade[]Grades={Grade.A,Grade.B,Grade.C,Grade.D,Grade.E,Grade.F};
    public static long startTime=0;
    public static double remainingSemesterTime =4*60;
    public static AtomicBoolean pausedSemester=new AtomicBoolean(false);
    private volatile static AdminCommandLine adminCommandLine;
    AdminCommandLine(){
        help();
    }
    public static AdminCommandLine getInstance() {
        if (adminCommandLine == null) {
            synchronized (AdminCommandLine.class) {
                if (adminCommandLine == null) {
                    adminCommandLine = new AdminCommandLine();
                }
            }
        }
        return adminCommandLine;
    }
    public void commandline(String input){
        switch (input) {
            case "1" -> help();
            case "2" -> reset();
            case "3" -> startSemester();
            case "4" -> endSemester();
            case "5" -> showAccountRequests();
            case "6" -> handleAccountRequest();
            case "7" -> showFinancialRequests();
            case "8" -> handleFinancialRequest();
            case "9" -> UniversityManager.getInstance().showAllCourses();
            case "10" -> addCourse();
            case "11" -> modifyStudentBalance();
            case "12" -> pauseSemester();
            case "13" -> continueSemester();
            case "14" -> UniversityManager.getInstance().signOut();

            default -> UniversityManager.getInstance().undefinedCommand();
        }

    }
    protected void reset(){
        UniversityManager.getInstance().Students= new ArrayList<>();
        UniversityManager.getInstance().Teachers= new ArrayList<>();
        UniversityManager.getInstance().courses = new ArrayList<>();
        UniversityManager.getInstance().accountRequests =new ArrayList<>();
        UniversityManager.getInstance().financialRequests =new ArrayList<>();
        UniversityManager.getInstance().currentUser =null;
        UniversityManager.getInstance().semesterCount =0;
        UniversityManager.getInstance().semesterRunning =false;
        UniversityManager.getInstance().lastSaved =10;
        UniversityManager.getInstance().multiThreadSave();
        System.out.println("database emptied successfully");
    }
    public void help(){
        System.out.println("please enter the number of the command you want\nCommands:\n1-help\n2-reset database\n3-start semester\n4-end semester\n5-show account requests\n6-handle account request\n7-show financial requests\n8-handle financial request\n9-show all courses\n10-add course\n11-modify student balance\n12-pause semester\n13-continue semester\n14-sign out");
    }
    protected void startSemester(){
        if(UniversityManager.getInstance().semesterRunning){
            System.out.println("you cant start a running semester");
        }
        AtomicBoolean Found=new AtomicBoolean(false);
        UniversityManager.getInstance().Students.forEach(student->{
            if(student.getCreditHours()<9&&student.getCreditHours()!=0){
                Found.set(true);
                System.out.println();
                System.out.println("Student "+student.getName()+" has less than 9 credit hours\n semester cant start");
            }
        });
        UniversityManager.getInstance().courses.forEach(course -> {
            if(course.getInstructor()==null){
                UniversityManager.getInstance().removeCourse(course);
            }
        });
        if(Found.get())
            return;
        System.out.println("Semester started");
        UniversityManager.getInstance().Students.forEach(student->{
            student.getAllGrades().add(student.getGrades());
        });
        UniversityManager.getInstance().semesterCount++;
        UniversityManager.getInstance().semesterRunning =true;
        startTime=System.currentTimeMillis();
        remainingSemesterTime =60;
        UniversityManager.getInstance().executor.execute(()->startCountdown());
        UniversityManager.getInstance().multiThreadSave();
    }
    private void startCountdown(){
        while(true){
            remainingSemesterTime -=(System.currentTimeMillis()-startTime)/1000;
            startTime=System.currentTimeMillis();
            if(remainingSemesterTime <=0){
                endSemester();
                break;
            }
            System.out.println("remaining time in semester: "+remainingSemesterTime);
            if(pausedSemester.get()){
                break;
            }
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    protected void pauseSemester(){
        if (!UniversityManager.getInstance().semesterRunning||pausedSemester.get()){
            System.out.println("sem not running");
            return;
        }
        pausedSemester.set(true);
        System.out.println("paused");
    }
    protected void continueSemester(){
        if(!pausedSemester.get()){
            System.out.println("you cant continue an unpaused semester");
            return;
        }
        pausedSemester.set(false);
        startTime=System.currentTimeMillis();
        UniversityManager.getInstance().executor.execute(()->startCountdown());
        System.out.println("continued");
    }

    protected void endSemester(){
        if(!UniversityManager.getInstance().semesterRunning){
            System.out.println("semester is not running");
            return;
        }
        UniversityManager.getInstance().Students.forEach(student->{
            int random_grade=(int)(Math.random()*6);
            student.getAllGrades().get(UniversityManager.getInstance().semesterCount -1).replaceAll((course, grade) -> grade=Grades[random_grade]);
        });
        UniversityManager.getInstance().Students.forEach(student->{
            student.setGrades(new HashMap<>());
            student.setCourses(new ArrayList<>());
            student.setCreditHours(0);
        });
        UniversityManager.getInstance().semesterRunning =false;
        System.out.println("semester ended");
        UniversityManager.getInstance().multiThreadSave();
    }

    protected void showAccountRequests(){
        AtomicInteger counter= new AtomicInteger(1);
        UniversityManager.getInstance().accountRequests.forEach(request->System.out.println((counter.getAndIncrement())+"-" +request.printRequest()));
    }

    protected void showFinancialRequests(){
        UniversityManager.getInstance().financialRequests.forEach(request->System.out.println(request.printRequest()+"\nHas requested financial aid"));
    }

    protected void handleAccountRequest(){
            try {
                System.out.println("Do you want to accept or reject: ");
                String response= UniversityManager.getInstance().Reader.readLine();
                boolean response_flag=false;
                if(response.equals("accept"))
                    response_flag=true;
                else if (response.equals("reject"))
                    response_flag=false;
                else{
                    System.out.println("Invalid input");
                    return;
                }
                System.out.println("enter the number of the requests you want to handle separated with commas");
                String requests[]= UniversityManager.getInstance().Reader.readLine().split(",");
                Arrays.sort(requests);
                int requestNumbers[]=new int[requests.length];
                int pointer=0;
                for(String request:requests){
                    int request_number= Integer.parseInt(request);
                    if(request_number> UniversityManager.getInstance().accountRequests.size()||request_number<=0) {
                        System.out.println("Invalid Request Numbers");
                        return;
                    }
                    requestNumbers[pointer++]=request_number;
                }
                for(int request_number=requestNumbers.length-1;request_number>=0;request_number--) {
                    var current_request = UniversityManager.getInstance().accountRequests.get(requestNumbers[request_number] - 1);
                    if (response_flag) {
                        if (current_request instanceof TaAccount) {
                            UniversityManager.getInstance().Teachers.add((TaAccount) current_request);
                            UniversityManager.getInstance().teacherLogins.put(((TaAccount) current_request).getName(), (TaAccount) current_request);
                        } else {
                            UniversityManager.getInstance().Students.add((StudentAccount) current_request);
                            UniversityManager.getInstance().studentLogins.put(((StudentAccount) current_request).getName(), (StudentAccount) current_request);
                        }
                        System.out.println("Request Accepted");
                    } else
                        System.out.println("Request Rejected");
                    UniversityManager.getInstance().accountRequests.remove(current_request);
                }
            } catch (IOException e) {
                System.out.println("No Input received");
            }
    }

    protected void handleFinancialRequest(){
            try {
                System.out.println("Do you want to accept or reject: ");
                String response= UniversityManager.getInstance().Reader.readLine();
                boolean response_flag=false;
                if(response.equals("accept"))
                    response_flag=true;
                else if (response.equals("reject"))
                    response_flag=false;
                else{
                    System.out.println("Invalid input");
                    return;
                }
                System.out.println("enter the number of the requests you want to handle separated with commas");
                String requests[]= UniversityManager.getInstance().Reader.readLine().split(",");
                Arrays.sort(requests);
                int requestNumbers[]=new int[requests.length];
                int pointer=0;
                for(String request:requests){
                    int request_number= Integer.parseInt(request);
                    if(request_number> UniversityManager.getInstance().financialRequests.size()||request_number<=0) {
                        System.out.println("Invalid Request Numbers");
                        return;
                    }
                    requestNumbers[pointer++]=request_number;
                }
                for(int request_number=requestNumbers.length-1;request_number>=0;request_number--) {
                    var current_request = UniversityManager.getInstance().financialRequests.get(request_number - 1);
                    if (response_flag) {
                        ((StudentAccount) current_request).setFinancialAided(true);
                        System.out.println("Request Accepted");
                    } else
                        System.out.println("Request Rejected");
                    UniversityManager.getInstance().accountRequests.remove(current_request);
                }

            } catch (IOException e) {
                System.out.println("No Input received");
            }

    }
    protected void addCourse(){

        try {
            System.out.println("enter the name of the course: ");
            String name= UniversityManager.getInstance().Reader.readLine();
            AtomicBoolean found= new AtomicBoolean(false);
            UniversityManager.getInstance().courses.forEach(course->{
                if(course.getName().equals(name))
                    found.set(true);
            });
            if(found.get()){
                System.out.println("There exist a course with this name already");
                return;
            }
            System.out.println("enter the credit hours of the course: ");
            int credit_hours= Integer.parseInt(UniversityManager.getInstance().Reader.readLine());
            System.out.println("enter the specialization of the course: ");
            String specialization= UniversityManager.getInstance().Reader.readLine();
            System.out.println("enter the max students of the course: ");
            int max_students= Integer.parseInt(UniversityManager.getInstance().Reader.readLine());
            UniversityManager.getInstance().courses.add(new Course(name,credit_hours,max_students,specialization));
            System.out.println("Course added successfully");
        } catch (IOException e) {
            System.out.println("No Input received");
        }catch(NumberFormatException e){
            System.out.println("Wrong format");
        }
    }

    protected void modifyStudentBalance(){

        try {
            System.out.println("enter the id of the student: ");
            String id= UniversityManager.getInstance().Reader.readLine();
            AtomicBoolean found= new AtomicBoolean(false);
            AtomicReference<StudentAccount> Student_Found=new AtomicReference<>();
            UniversityManager.getInstance().Students.forEach(student->{
                if(student.getId().equals(id)) {
                    found.set(true);
                    Student_Found.set(student);
                }
            });
            if(!found.get()){
                System.out.println("No student with this id");
                return;
            }
            System.out.println("the old balance of the student is: "+Student_Found.get().getBalance());
            System.out.println("enter the new balance: ");
            Double balance= Double.parseDouble(UniversityManager.getInstance().Reader.readLine());
            Student_Found.get().setBalance(balance);
            System.out.println("new balance added");
        } catch (IOException e) {
            System.out.println("No Input received");
        }catch(NumberFormatException e){
            System.out.println("Wrong format");
        }
    }

}
