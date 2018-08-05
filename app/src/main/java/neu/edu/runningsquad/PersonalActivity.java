package neu.edu.runningsquad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class PersonalActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_personal, contentFrameLayout);
        Bundle extras = getIntent().getExtras();

        if (extras.containsKey("username")) {
            String username = extras.getString("username");
        }
    }

}
