package uk.ac.soton.comp2211.runwayredeclaration.Component;

public class User {
    String username;
    String password;
    String permissionLevel;

    public User(String username, String password, String permissionLevel) {
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }


    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
