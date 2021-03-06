

package neu.edu.runningsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.util.SquadAdapter;

import static neu.edu.runningsquad.util.Sessions.getUsername;
import static neu.edu.runningsquad.util.Sessions.saveLoginInfo;

public class GroupSearchActivity extends MainActivity {

    private String username;
    private DatabaseReference mReference;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_group_search, contentFrameLayout);
        initGroupSearch();
    }

    public void initGroupSearch() {
        mReference = FirebaseDatabase.getInstance().getReference();
        username = getUsername(this.getApplicationContext());
        mRecyclerView = findViewById(R.id.squad_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        searchEditText = findViewById(R.id.search_text);
        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case EditorInfo.IME_ACTION_SEND:
                        case KeyEvent.KEYCODE_ENTER:
                            searchSquad(findViewById(android.R.id.content));
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        searchSquad(findViewById(android.R.id.content));
    }

    public void createSquad(View view) {

        final String squadname = ((TextView) findViewById(R.id.create_squad_name)).getText().toString();
        String desc = ((TextView) findViewById(R.id.create_squad_desc)).getText().toString();
        String city = ((TextView) findViewById(R.id.create_squad_city)).getText().toString();
        final Squad newSquad = new Squad(squadname, desc, city, username);
        try {
            mReference.child("squads").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(squadname)) {
                            Toast.makeText(getApplicationContext(), R.string.squad_warning, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    mReference.child("squads").child(squadname).setValue(newSquad);
                    updateUser(squadname);
                    Toast.makeText(getApplicationContext(), R.string.squad_success, Toast.LENGTH_LONG).show();
                    saveLoginInfo(username, squadname,getApplicationContext());
                    jump2GroupInfo();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            System.err.println("Creating squad failed: " + e.getMessage());
        }
    }

    public void updateUser(String squadname) {
        mReference.child("users").child(username).child("squad").setValue(squadname);
        mReference.child("users").child(username).child("role").setValue("owner");
        mReference.child("squads").child(squadname).child("members").child(username).setValue(true);
        mReference.child("squads").child(squadname).child("owner").setValue(username);
    }

    public void searchSquad(View view) {
        final String searchText = searchEditText.getText().toString();
        try {
            mReference.child("squads").addListenerForSingleValueEvent(new ValueEventListener() {
                List<Squad> squads = new ArrayList<>();

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Squad squad = child.getValue(Squad.class);
                        if (checkQualified(searchText, squad.getName()))
                            squads.add(squad);
                    }
                    mAdapter = new SquadAdapter(squads);
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            System.err.println("Fetching squadData failed: " + e.getMessage());
        }
    }

    public boolean checkQualified(String condition, String name) {
        Pattern r = Pattern.compile(".*" + condition + ".*");
        Matcher m = r.matcher(name);
        if (m.find())
            return true;
        else
            return false;
    }

    public void jump2CreateSquad(View view) {
        findViewById(R.id.group_create_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.group_search_layout).setVisibility(View.GONE);
    }

    public void jump2SearchSquad() {
        findViewById(R.id.group_search_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.group_create_layout).setVisibility(View.GONE);
    }

    public void jump2GroupInfo() {
        Intent intent = new Intent(this, GroupInfoActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.group_search_layout).getVisibility() == View.GONE) {
            jump2SearchSquad();
        } else {
            super.onBackPressed();
        }
    }
//
//    public void initGroupData() {
//        try {
//            mReference.child("squads").addListenerForSingleValueEvent(new ValueEventListener() {
//                List<Squad> squads = new ArrayList<>();
//
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//                        Squad squad = child.getValue(Squad.class);
//                        squads.add(squad);
//                    }
//                    mAdapter = new SquadAdapter(squads);
//                    mRecyclerView.setAdapter(mAdapter);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                }
//            });
//
//        } catch (Exception e) {
//            System.err.println("Fetching squadData failed: " + e.getMessage());
//        }
//    }
}
