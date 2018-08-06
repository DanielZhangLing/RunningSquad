

package neu.edu.runningsquad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import neu.edu.runningsquad.model.Squad;

public class GroupSearchActivity extends MainActivity {

    private String username;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_group_search, contentFrameLayout);
        initGroupSearch();
    }

    public void initGroupSearch() {
        mReference = FirebaseDatabase.getInstance().getReference();
        SharedPreferences userInfo = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        username = userInfo.getString("username", "");
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
                    store2SharedPreference("userInfo", "squadname", squadname);
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
}
