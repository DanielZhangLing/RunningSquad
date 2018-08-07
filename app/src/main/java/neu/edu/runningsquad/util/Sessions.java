package neu.edu.runningsquad.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Map;

public class Sessions {

    static String appName = "neu.edu.runningsquad";

    static public void saveLoginInfo(String username, String password, String squadname, Context context){

        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("squadname", squadname);
        editor.commit();
    }

    static public String getUsername(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        return preferences.getString("username", null);

    }

    static public String getPassword(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        return preferences.getString("password", null);
    }

    static public String getSquadName(Context context){
        SharedPreferences preferences =
                context.getSharedPreferences(appName, Context.MODE_PRIVATE);
        return preferences.getString("squadname", null);
    }
}
