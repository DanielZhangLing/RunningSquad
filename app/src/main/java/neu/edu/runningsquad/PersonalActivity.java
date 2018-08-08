package neu.edu.runningsquad;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import neu.edu.runningsquad.model.Squad;
import neu.edu.runningsquad.model.User;
import neu.edu.runningsquad.util.Sessions;

public class PersonalActivity extends MainActivity {

    private String username;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_personal, contentFrameLayout);
        username = Sessions.getUsername(this.getApplicationContext());
        mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("users/" + username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                showUserInfo(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showUserInfo(User user){

    }

}
