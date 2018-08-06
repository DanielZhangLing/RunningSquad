package neu.edu.runningsquad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PersonalActivity extends MainActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_personal, contentFrameLayout);
        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences tempInfo = getSharedPreferences("tempInfo", Context.MODE_PRIVATE);
        username = tempInfo.getString("username", userInfo.getString("username", ""));
        tempInfo.edit().clear();
    }

}
