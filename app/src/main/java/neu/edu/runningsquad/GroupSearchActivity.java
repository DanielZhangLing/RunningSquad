

package neu.edu.runningsquad;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GroupSearchActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_group_search, contentFrameLayout);
    }
}
