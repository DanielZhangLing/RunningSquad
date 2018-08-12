package neu.edu.runningsquad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import neu.edu.runningsquad.model.User;
import neu.edu.runningsquad.util.Sessions;

import static neu.edu.runningsquad.util.Sessions.clearLogin;
import static neu.edu.runningsquad.util.Sessions.clearTemp;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    protected FrameLayout contentFrameLayout;
    private DatabaseReference mReference;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        contentFrameLayout = findViewById(R.id.content_frame);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.baseline_menu_white_18);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Log.i("Runner", "Main");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_squad:
                        Intent loadSquad = new Intent(getApplicationContext(), GroupInfoActivity.class);
                        startActivity(loadSquad);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_profile:
                        Intent loadProfile = new Intent(getApplicationContext(), PersonalActivity.class);
                        startActivity(loadProfile);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_groups:
                        Intent loadGroups = new Intent(getApplicationContext(), GroupSearchActivity.class);
                        startActivity(loadGroups);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_ranking:
                        Intent rankingGroups = new Intent(getApplicationContext(), ScoreBoardActivity.class);
                        startActivity(rankingGroups);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_run:
                        Intent loadRunning = new Intent(getApplicationContext(), RunningActivity.class);
                        startActivity(loadRunning);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        Intent loadLogin = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loadLogin);
                        clearTemp(getApplicationContext());
                        clearLogin(getApplicationContext());
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    case R.id.nav_acknowlegement:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Acknowlegement");
                        builder.setMessage(R.string.acknowledgement);
                        builder.setCancelable(false);
                        builder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {

                                    }
                                });
                        mDialog = builder.show();

                }
                return false;
            }
        });

        String username = Sessions.getUsername(this);
        mReference = FirebaseDatabase.getInstance().getReference();
//        mReference.child("users").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                showUserInfo(user);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//
//    private void showUserInfo(User user){
//
//
//        TextView username = findViewById(R.id.header_name);
//        TextView email = findViewById(R.id.header_email);
////        username.setText(user.getUsername());
////        email.setText(user.getEmail());
//
//    }
    public void store2SharedPreference(String file, String name, String value) {
        SharedPreferences userInfo = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(name, value);
        editor.commit();
    }

}
