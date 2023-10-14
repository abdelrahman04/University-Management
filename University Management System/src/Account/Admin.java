package Account;

import Account.Accounts;

public class Admin implements Accounts {
    private String username;
    private String password;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean compare(String name,String password){
        return name.equals(getUsername())&&password.equals(getPassword());
    }

    private String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
