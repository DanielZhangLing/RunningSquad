package neu.edu.runningsquad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.model.User;

import static neu.edu.runningsquad.util.Sessions.clearTemp;
import static neu.edu.runningsquad.util.Sessions.getSquadName;
import static neu.edu.runningsquad.util.Sessions.getTempSquad;
import static neu.edu.runningsquad.util.Sessions.getUsername;

public class GroupInfoActivity extends MainActivity {

    private String squadname;
    private String username;
    private DatabaseReference mReference;
    private ViewGroup tableView;
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
        initGroupInfo();

    }

    public void initGroupInfo() {
        mReference = FirebaseDatabase.getInstance().getReference();
        username = getUsername(this);
        String mySquadname = getSquadName(this);
        String tempSquadname = getTempSquad(this);
        if (tempSquadname != null && !tempSquadname.equals("")) {
            squadname = tempSquadname;
            clearTemp(this);
        } else
            squadname = mySquadname;
        if (mySquadname == null || mySquadname.equals("")) {
            showJoinButton();
        }
        tableView = findViewById(R.id.table_member);
        initGroupData();
        initMemberData();
    }

    private void showJoinButton() {
        findViewById(R.id.join_squad).setVisibility(View.VISIBLE);
    }

    public void joinSquad(View view) {
        if (number < 10) {
            try {
                mReference.child("users").child(username).child("squad").setValue(squadname);
                mReference.child("users").child(username).child("role").setValue("member");
                mReference.child("squads").child(squadname).child("members").child(username).setValue(true);
                mReference.child("squads").child(squadname).child("number").setValue(number + 1);
                Toast.makeText(getApplicationContext(), R.string.join_squad_success, Toast.LENGTH_LONG).show();
                tableView.removeAllViews();
                initMemberData();
            } catch (Exception e) {
                System.err.println("Joining squad failed: " + e.getMessage());
            }

        } else {
            Toast.makeText(getApplicationContext(), R.string.join_squad_warning, Toast.LENGTH_LONG).show();
        }
    }

    private void initMemberData() {
        try {
            mReference.child("users").orderByChild("star").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        if (user.getSquad() != null && user.getSquad().equals(squadname)) {
                            View row = getLayoutInflater().inflate(R.layout.member_row, tableView, false);
                            showMemberInfo(row, user);
                            tableView.addView(row, 2);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            System.err.println("Getting memberData failed: " + e.getMessage());
        }
    }

    public void initGroupData() {
        try {
            mReference.child("squads").child(squadname).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Squad squad = dataSnapshot.getValue(Squad.class);
                    number = squad.getNumber();
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
        ((TextView) findViewById(R.id.squad_info_name)).setText(squad.getName());
        ((TextView) findViewById(R.id.squad_info_city)).setText(squad.getCity());
        ((TextView) findViewById(R.id.squad_info_desc)).setText(squad.getDescription());
        ((TextView) findViewById(R.id.squad_info_prize)).setText(squad.prizes2String());
        ((TextView) findViewById(R.id.squad_info_totalStars)).setText(Integer.toString(squad.getTotalStars()));
    }

    public void showMemberInfo(View row, final User user) {
        ((TextView) row.findViewById(R.id.member_name)).setText(user.getUsername());
        ((TextView) row.findViewById(R.id.member_star)).setText(Integer.toString(user.getStar()));
        ((TextView) row.findViewById(R.id.member_role)).setText(user.getRole());
        row.findViewById(R.id.member_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                store2SharedPreference("tempInfo", "username", user.getUsername());
                jump2Person();
            }
        });
    }

    public void jump2Person() {
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivity(intent);
    }
}
