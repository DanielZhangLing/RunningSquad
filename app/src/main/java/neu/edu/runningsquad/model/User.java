package neu.edu.runningsquad.model;

import android.content.Context;

public class User {
    private String username;
    private String password;
    private String email;
    private String city;
    private String squad;
    private String role;
    private int star;
    private String icon;

    public User() {
    }

    public User(String username, String password, String email, String city) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.city = city;
        this.star = 0;
        this.role = "member";
        this.icon = "profile1";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSquad() {
        return squad;
    }

    public void setSquad(String squad) {
        this.squad = squad;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getImageId(Context context) {
        return context.getResources().getIdentifier("drawable/" + icon, null, context.getPackageName());
    }
}
