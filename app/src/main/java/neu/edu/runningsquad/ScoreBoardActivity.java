package neu.edu.runningsquad;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import neu.edu.runningsquad.model.Record;
import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.util.GroupRankingAdapter;
import neu.edu.runningsquad.util.RecordAdapter;
import neu.edu.runningsquad.util.Sessions;

public class ScoreBoardActivity extends MainActivity {

    private ListView listView;
    private List<Squad> squadList = new ArrayList<>();
    private GroupRankingAdapter groupRankingAdapter;
    private EditText cityNameText;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_score_board, contentFrameLayout);
        mReference = FirebaseDatabase.getInstance().getReference();

        cityNameText = this.findViewById(R.id.score_board_city);

        groupRankingAdapter = new GroupRankingAdapter(this, squadList);
        listView = findViewById(R.id.score_board_list_view);
        listView.setAdapter(groupRankingAdapter);

        cityNameText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Log.i("runningsquad", v.getText().toString());
                    searchForGroups(v.getText().toString());
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                Squad squad = (Squad) arg0.getItemAtPosition(position);
                Sessions.saveTempInfo("", squad.getName(), ScoreBoardActivity.this);
                Intent intent = new Intent(ScoreBoardActivity.this, GroupInfoActivity.class);
                startActivity(intent);
            }

        });

    }


    void searchForGroups(String city){
        mReference.child("squads").orderByChild("city").equalTo(city).orderByChild("totalStars").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot recordSnapshot: dataSnapshot.getChildren()) {
                    Squad squad = recordSnapshot.getValue(Squad.class);
                    squadList.add(squad);

                }
                groupRankingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
