package neu.edu.runningsquad.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Map;

public class Sessions {

    static String appName = "neu.edu.runningsquad";
    static String tempFile = "tempInfo";

    static public void saveLoginInfo(String username, String password, String squadname, Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("squadname", squadname);
        editor.apply();
    }

    static public void saveTempInfo(String username, String squadname, Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(tempFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("squadname", squadname);
        editor.apply();
    }

    static public void saveLoginInfo(String username, String squadname, Context context) {

        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("squadname", squadname);
        editor.apply();
    }

    static public String getUsername(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        return preferences.getString("username", null);

    }

    static public String getPassword(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        return preferences.getString("password", null);
    }

    static public String getSquadName(Context context) {
        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        return preferences.getString("squadname", null);
    }

    static public String getTempSquad(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(tempFile, Context.MODE_PRIVATE);
        return preferences.getString("squadname", null);
    }

    static public String getTempUsername(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(tempFile, Context.MODE_PRIVATE);
        return preferences.getString("username", null);
    }

    static public void clearTemp(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(tempFile, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
}
