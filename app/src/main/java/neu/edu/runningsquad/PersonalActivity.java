package neu.edu.runningsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import neu.edu.runningsquad.model.Record;
import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.model.User;
import neu.edu.runningsquad.util.RecordAdapter;
import neu.edu.runningsquad.util.Sessions;

public class PersonalActivity extends MainActivity {

    private String username;
    private DatabaseReference mReference;

    private TextView emailTextView;
    private TextView usernameTextView;
    private TextView starTextView;

    private TextView groupNameTextView;
    private TextView groupStarsTextView;
    private TextView groupRankingTextView;
    private ListView listView;
    private List<Record> recordList = new ArrayList<Record>();
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_personal, contentFrameLayout);
        String myUsername = Sessions.getUsername(this.getApplicationContext());
        String tempUsername = Sessions.getTempUsername(this.getApplicationContext());
        if (tempUsername != null && !tempUsername.equals("")) {
            username = tempUsername;
            Sessions.clearTemp(this.getApplicationContext());
        } else {
            username = myUsername;
        }
        emailTextView = findViewById(R.id.personal_email);
        usernameTextView = findViewById(R.id.personal_username);
        starTextView = findViewById(R.id.personal_stars);
        groupNameTextView = findViewById(R.id.personal_group_name);
        groupStarsTextView = findViewById(R.id.personal_group_stars);
        groupRankingTextView = findViewById(R.id.personal_group_ranking);
        mReference = FirebaseDatabase.getInstance().getReference();
        recordAdapter = new RecordAdapter(this, recordList);
        listView = findViewById(R.id.personal_records_list);
        listView.setAdapter(recordAdapter);
        mReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                showUserInfo(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mReference.child("user-records").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot recordSnapshot : dataSnapshot.getChildren()) {
                    Record record = recordSnapshot.getValue(Record.class);
                    recordList.add(record);

                }
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showUserInfo(User user) {

        emailTextView.setText(user.getEmail());
        usernameTextView.setText(user.getUsername());
        starTextView.setText(Integer.toString(user.getStar()));

        String squad = user.getSquad();
        initGroupData(squad);


    }


    public void initGroupData(String squadname) {
        try {
            mReference.child("squads").child(squadname).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Squad squad = dataSnapshot.getValue(Squad.class);
                    showSquadInfo(squad);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            System.err.println("Getting squadData failed: " + e.getMessage());
        }
    }

    public void showSquadInfo(Squad squad) {

        groupNameTextView.setText(squad.getName());
        groupRankingTextView.setText("0");
        groupStarsTextView.setText(String.valueOf(squad.getTotalStars()));
    }

    public void startToRun(View view) {
        Intent intent = new Intent(this, RunningActivity.class);
        startActivity(intent);
    }

}
