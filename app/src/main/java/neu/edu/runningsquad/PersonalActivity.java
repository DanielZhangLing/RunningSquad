package neu.edu.runningsquad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class PersonalActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_personal, contentFrameLayout);
        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = userInfo.getString("username", "");
    }

}
