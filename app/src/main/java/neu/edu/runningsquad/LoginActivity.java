package neu.edu.runningsquad;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import neu.edu.runningsquad.model.User;
import neu.edu.runningsquad.util.AES;
import neu.edu.runningsquad.util.Sessions;

import static neu.edu.runningsquad.util.Sessions.saveLoginInfo;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference mReference;
    private final String KEY = "runningsquad";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Sessions.clearTemp(this);
        initLogin();
    }

    public void initLogin() {
        mReference = FirebaseDatabase.getInstance().getReference();
        ((EditText) findViewById(R.id.password_input)).setTransformationMethod(PasswordTransformationMethod.getInstance());
        ((EditText) findViewById(R.id.register_password)).setTransformationMethod(PasswordTransformationMethod.getInstance());

    }

    public void login(View view) {
        View loginButton = findViewById(R.id.login_button);
        View loginProgress = findViewById(R.id.login_progressBar);
        loginButton.setVisibility(View.GONE);
        loginProgress.setVisibility(View.VISIBLE);
        final String username = ((TextView) findViewById(R.id.username_input)).getText().toString();
        final String password = ((TextView) findViewById(R.id.password_input)).getText().toString();
        try {
            mReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        User user = child.getValue(User.class);
                        if (child.getKey().equals(username)) {
                            if (AES.decrypt(user.getPassword(), KEY).equals(password)) {
                                Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_LONG).show();
                                saveLoginInfo(user.getUsername(), user.getPassword(), user.getSquad(), getApplicationContext());
                                if (user.getSquad() != null && !user.getSquad().equals(""))
                                    jump2Person();
                                else
                                    jump2GroupSearch();
                                return;
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), R.string.login_fail, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            loginButton.setVisibility(View.VISIBLE);
            loginProgress.setVisibility(View.GONE);
        }
    }

    public void register(View view) {

        final String username = ((TextView) findViewById(R.id.register_username)).getText().toString();
        final String password = AES.encrypt(((TextView) findViewById(R.id.register_password)).getText().toString(), KEY);
        String email = ((TextView) findViewById(R.id.register_email)).getText().toString();
        String city = ((TextView) findViewById(R.id.register_city)).getText().toString();
        final User newUser = new User(username, password, email, city);
        try {
            mReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals(username)) {
                            Toast.makeText(getApplicationContext(), R.string.register_warning, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    mReference.child("users").child(username).setValue(newUser);
                    saveLoginInfo(username, password, "", getApplicationContext());
                    jump2GroupSearch();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

        } catch (Exception e) {
            System.err.println("Creating user failed: " + e.getMessage());
        }
    }

    public void jump2Person() {
        Intent intent = new Intent(this, PersonalActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void jump2GroupSearch() {
        Intent intent = new Intent(this, GroupSearchActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void jump2Register(View view) {
        findViewById(R.id.register_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.login_layout).setVisibility(View.GONE);
    }

    public void jump2Login() {
        findViewById(R.id.register_layout).setVisibility(View.GONE);
        findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.login_layout).getVisibility() == View.GONE) {
            jump2Login();
        } else {
            super.onBackPressed();
        }
    }
}
