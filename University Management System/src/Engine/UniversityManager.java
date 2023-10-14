package Engine;

import Account.*;
import Exceptions.*;
import Material.Course;
import Material.Requests;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class UniversityManager {
    public ArrayList<StudentAccount> Students= new ArrayList<>();
    public HashMap<String,StudentAccount> studentLogins=new HashMap<>();
    public ArrayList<TaAccount> Teachers= new ArrayList<>();
    public HashMap<String,TaAccount> teacherLogins=new HashMap<>();

    public ArrayList<Course> courses = new ArrayList<>();
    public ArrayList<Requests> accountRequests =new ArrayList<>();
    public ArrayList<Requests> financialRequests =new ArrayList<>();
    public Admin admin=new Admin("admin","admin");
    public UniPresident uniPresident=new UniPresident("president","president");
    public Accounts currentUser;
    public BufferedReader Reader = new BufferedReader(
            new InputStreamReader(System.in));
    public Integer semesterCount =0;
    public Boolean semesterRunning =false;
    public int lastSaved =10;
    public CommandLine commandLine;
    public ExecutorService executor =
            Executors.newCachedThreadPool();
    private volatile static UniversityManager universityManager;
    private UniversityManager(){
        read();
        commandLine= NullCommandLine.getInstance();
    }
    public static UniversityManager getInstance() {
        if (universityManager == null) {
            synchronized (UniversityManager.class) {
                if (universityManager == null) {
                    universityManager = new UniversityManager();
                }
            }
        }
        return universityManager;
    }

    public void start(){
        while (true){
            String input;
            try {
                input= Reader.readLine();
                if(input.equals("save")){
                    multiThreadSave();
                } else
                commandLine.commandline(input);
            } catch (IOException e) {
                System.out.println("No available input to read from");
                continue;
            }


            setLast_saved(lastSaved -1);
            /*design pattern observer*/

        }
    }



    public void setLast_saved(int last_save) {
        if(last_save<0)
            multiThreadSave();
        getInstance().lastSaved = last_save<0?10:last_save;
    }
    public void removeCourse(Course course){
        ArrayList<StudentAccount> Student_List = course.getStudents_enrolled();
        Student_List.forEach(student-> student.removeCourse(course));
        getInstance().courses.remove(course);
    }

    public String getField(String field){
        System.out.println("enter your "+field+": ");
        String fieldValue = "";
        try {
            fieldValue= Reader.readLine();
            
        } catch (IOException e) {
            System.out.println("couldn't read input");
        }
        return fieldValue;
    }
    public double getDoubleField(String field){
        System.out.println("enter your "+field+": ");
        double fieldValue = 0;
        try {
            fieldValue= Double.parseDouble(Reader.readLine());
        } catch (IOException e) {
            System.out.println("couldn't read input");
        }catch (NumberFormatException e){
            System.out.println("Wrong format, you will get 0 as a result");
        }
        return fieldValue;
    }



    public void undefinedCommand(){
        System.out.println("Wrong command\nenter help to view list of commands");
    }

    public void signOut(){
        currentUser =null;
        System.out.println("signed out successfully");
        commandLine=NullCommandLine.getInstance();
    }

    public void multiThreadSave(){
        executor.execute(()->save());
    }



    public void save(){
        Path path = Paths.get("DataBase.ser");
        try {
            Files.newBufferedWriter(path , StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error erasing old file");
        }

        try (ObjectOutputStream Out_Stream = new ObjectOutputStream(new FileOutputStream("DataBase.ser"))){

            Out_Stream.writeObject(Students);
            Out_Stream.writeObject(studentLogins);
            Out_Stream.writeObject(Teachers);
            Out_Stream.writeObject(teacherLogins);
            Out_Stream.writeObject(courses);
            Out_Stream.writeObject(accountRequests);
            Out_Stream.writeObject(financialRequests);
            Out_Stream.writeObject(semesterCount);
            Out_Stream.writeObject(semesterRunning);
        } catch (IOException ex) {
            System.out.println("saving failed");
        }
    }
    public void read(){
        //with resources
        try {
            ObjectInputStream In_Stream = new ObjectInputStream(new FileInputStream("DataBase.ser"));
            Students=(ArrayList<StudentAccount>) In_Stream.readObject();
            studentLogins=(HashMap<String, StudentAccount>) In_Stream.readObject();
            Teachers=(ArrayList<TaAccount>) In_Stream.readObject();
            teacherLogins=(HashMap<String, TaAccount>) In_Stream.readObject();
            courses=(ArrayList<Course>) In_Stream.readObject();
            accountRequests =(ArrayList<Requests>) In_Stream.readObject();
            financialRequests =(ArrayList<Requests>) In_Stream.readObject();
            semesterCount =(Integer) In_Stream.readObject();
            semesterRunning =(Boolean)In_Stream.readObject();
            In_Stream.close();

        } catch (Exception ex) {
            System.out.println("reading data base failed");
        }
    }

    public void showAllCourses(){
        //el null yetla3 foo2
        if( currentUser instanceof TaAccount)
            ((TaAccount) currentUser).showCourses();
        else
        courses.forEach(course->System.out.println(course.toString()));
    }
    public void showMyCourses(){
        if(currentUser instanceof Admin)
            System.out.println("You are the admin");
        else
        ((UserAccount) currentUser).showMyCourses();

    }

    public void registerCourse(){

        try {
                System.out.println("enter your the name of the course you want to register: ");
                String name= Reader.readLine();

                ((UserAccount) currentUser).registerCourse(name);

        } catch (IOException e) {
            System.out.println("No Input received");
        }catch(NumberFormatException e){
            System.out.println("Wrong format");
        } catch (SystemException e) {
            System.out.println(e.getMessage());;
        }

    }

}
