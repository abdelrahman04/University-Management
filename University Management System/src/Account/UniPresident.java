package Account;

public class UniPresident implements Accounts{
    private String username;
    private String password;

    public UniPresident(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean compare(String name, String password){
        return name.equals(getUsername())&&password.equals(getPassword());
    }
}
