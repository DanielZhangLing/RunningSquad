package neu.edu.runningsquad.model;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class Squad {

    private String name;
    private String description;
    private String city;
    private Map<String, Boolean> members;
    private Map<String, Boolean> prizes;
    private String owner;
    private int number;
    private int totalStars;
    private String icon;

    public Squad() {
    }

    public Squad(String name, String description, String city, String owner) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.owner = owner;
        members = new HashMap<>();
        prizes = new HashMap<>();
        number = 1;
        totalStars = 0;
        icon = "animal1";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    public Map<String, Boolean> getPrizes() {
        return prizes;
    }

    public void setPrizes(Map<String, Boolean> prizes) {
        this.prizes = prizes;
    }

    public String prizes2String() {
        if (prizes != null && !prizes.isEmpty()) {
            String res = "";
            for (String prize : prizes.keySet()) {
                res += prize + "\n";
            }
            return res.substring(0, res.length() - 2);
        }
        return "";
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(int totalStars) {
        this.totalStars = totalStars;
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
