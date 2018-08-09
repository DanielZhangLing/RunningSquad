package neu.edu.runningsquad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    protected FrameLayout contentFrameLayout;

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
                    case R.id

            }
            return false;
            }
        });
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

    public void store2SharedPreference(String file, String name, String value) {
        SharedPreferences userInfo = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(name, value);
        editor.commit();
    }

}
