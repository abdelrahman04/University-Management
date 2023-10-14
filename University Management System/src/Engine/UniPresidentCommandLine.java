package Engine;

public class UniPresidentCommandLine implements CommandLine{
    private volatile static UniPresidentCommandLine uniPresidentCommandLine;
    private UniPresidentCommandLine(){
        help();
    }
    public static UniPresidentCommandLine getInstance() {
        if (uniPresidentCommandLine == null) {
            synchronized (UniPresidentCommandLine.class) {
                if (uniPresidentCommandLine == null) {
                    uniPresidentCommandLine = new UniPresidentCommandLine();
                }
            }
        }
        return uniPresidentCommandLine;
    }
    public AdminCommandLine adminCommandLine=new AdminCommandLine();
    public void commandline(String input){
        switch (input) {
            case "1" -> help();
            case "2" -> adminCommandLine.reset();
            case "3" -> adminCommandLine.startSemester();
            case "4" -> adminCommandLine.endSemester();
            case "5" -> adminCommandLine.showAccountRequests();
            case "6" -> adminCommandLine.handleAccountRequest();
            case "7" -> adminCommandLine.showFinancialRequests();
            case "8" -> adminCommandLine.handleFinancialRequest();
            case "9" -> UniversityManager.getInstance().showAllCourses();
            case "10" -> adminCommandLine.addCourse();
            case "11" -> adminCommandLine.modifyStudentBalance();
            case "12" -> adminCommandLine.pauseSemester();
            case "13" -> adminCommandLine.continueSemester();
            case "14" -> newCommand();
            case "15" -> UniversityManager.getInstance().signOut();

            default -> UniversityManager.getInstance().undefinedCommand();
        }

    }
    public void help(){
        System.out.println("please enter the number of the command you want\nCommands:\n1-help\n2-reset database\n3-start semester\n4-end semester\n5-show account requests\n6-handle account request\n7-show financial requests\n8-handle financial request\n9-show all courses\n10-add course\n11-modify student balance\n12-pause semester\n13-continue semester\n14-new command\n15-sign out");
    }
    public void newCommand(){
        //implementation
    }
}
