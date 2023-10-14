package Engine;

import Account.StudentAccount;
import Account.TaAccount;

import java.io.IOException;

public class NullCommandLine implements CommandLine {
    private volatile static NullCommandLine nullCommandLine;

    private NullCommandLine() {
        help();
    }

    public static NullCommandLine getInstance() {
        if (nullCommandLine == null) {
            synchronized (NullCommandLine.class) {
                if (nullCommandLine == null) {
                    nullCommandLine = new NullCommandLine();
                }
            }
        }
        return nullCommandLine;
    }

    public void commandline(String input) {
        switch (input) {
            case "1" -> help();
            case "2" -> createStudentAccount();
            case "3" -> createTeacherAccount();
            case "4" -> adminSignIn();
            case "5" -> studentSignIn();
            case "6" -> teacherSignIn();
            case "7" -> uniPresidentSignIn();
            default -> UniversityManager.getInstance().undefinedCommand();
        }
    }

    public void help() {
        System.out.println("please enter the number of the command you want\nCommands:\n1-help\n2-create student account\n3-create teacher account\n4-admin sign in\n5-student sign in\n6-teacher sign in\n7-uni president sign in");
    }

    private void adminSignIn() {
        String name = UniversityManager.getInstance().getField("Name");
        String password = UniversityManager.getInstance().getField("password");
        if (UniversityManager.getInstance().admin.compare(name, password)) {
            System.out.println("signed in successfully");
            UniversityManager.getInstance().currentUser = UniversityManager.getInstance().admin;
            UniversityManager.getInstance().commandLine = AdminCommandLine.getInstance();
        } else
            System.out.println("Invalid credentials");

    }

    public void uniPresidentSignIn() {
        String name = UniversityManager.getInstance().getField("Name");
        String password = UniversityManager.getInstance().getField("password");
        if (UniversityManager.getInstance().uniPresident.compare(name, password)) {
            System.out.println("signed in successfully");
            UniversityManager.getInstance().currentUser = UniversityManager.getInstance().uniPresident;
            UniversityManager.getInstance().commandLine = UniPresidentCommandLine.getInstance();
        } else
            System.out.println("Invalid credentials");
    }

    private void studentSignIn() {
        try {
            System.out.println("enter your username: ");
            String name = UniversityManager.getInstance().Reader.readLine();
            System.out.println("enter your password: ");
            String password = UniversityManager.getInstance().Reader.readLine();
            if (UniversityManager.getInstance().studentLogins.containsKey(name)) {
                StudentAccount student = UniversityManager.getInstance().studentLogins.get(name);
                if (student.getPassword().equals(password)) {
                    System.out.println("signed in successfully");
                    UniversityManager.getInstance().currentUser = student;
                    UniversityManager.getInstance().commandLine = StudentCommandLine.getInstance();
                } else
                    System.out.println("Invalid credentials");
            } else
                System.out.println("Invalid credentials");
        } catch (IOException e) {
            System.out.println("No Input received");
        }
    }

    private void teacherSignIn() {
        try {
            System.out.println("enter your username: ");
            String name = UniversityManager.getInstance().Reader.readLine();
            System.out.println("enter your password: ");
            String password = UniversityManager.getInstance().Reader.readLine();
            if (UniversityManager.getInstance().teacherLogins.containsKey(name)) {
                TaAccount teacher = UniversityManager.getInstance().teacherLogins.get(name);
                if (teacher.getPassword().equals(password)) {
                    System.out.println("signed in successfully");
                    UniversityManager.getInstance().currentUser = teacher;
                    UniversityManager.getInstance().commandLine = TeacherCommandLine.getInstance();
                } else
                    System.out.println("Invalid credentials");
            } else
                System.out.println("Invalid credentials");
        } catch (IOException e) {
            System.out.println("No Input received");
        }
    }

    private void createStudentAccount() {
        UniversityManager.getInstance().accountRequests.add(new StudentAccount.StudentBuilder().setName().setPassword().setEmail().setBalance().getStudent());
        System.out.println("request submitted successfully");
    }

    private void createTeacherAccount() {
        UniversityManager.getInstance().courses = null;
        UniversityManager.getInstance().accountRequests.add(new TaAccount.TeacherBuilder().setName().setPassword().setSpecialization().setEmail().getTeacher());
        System.out.println("request submitted successfully");
    }


}
