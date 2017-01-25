package furtiveops.com.blueviewmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.User;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String TAG = "HomeActivity";

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.content_home)
    RelativeLayout contentHome;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mFirebaseDatabaseReference;

    private User user;
    public static final int RC_LOGGED_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser;
        firebaseUser = mFirebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            // Not signed in, launch the Sign In activity
            Intent intent = SignInActivity.makeIntent(this);
            startActivityForResult(intent, RC_LOGGED_IN);
            return;
        } else {
            user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), getRole(firebaseUser.getEmail()));
            setupHomeMenu();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RC_LOGGED_IN == requestCode && Activity.RESULT_OK == resultCode) {
            if(null != data) {
                final String userId = data.getStringExtra(IntentConstants.USER_ID);
                final String email = data.getStringExtra(IntentConstants.USER_EMAIL);
                user = new User(userId, email, getRole(email));

            }
            setupHomeMenu();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if(id == R.id.action_signout)
        {
            mFirebaseAuth.signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_call_bva)
        {
            // Handle the phone intent
        }
        else if (id == R.id.nav_email_bva)
        {

        }
        else if (id == R.id.nav_entire_history)
        {

        }
        else if (id == R.id.nav_schedule_work)
        {

        }
        else if (id == R.id.nav_shop_bva)
        {

        }
        else if (id == R.id.nav_track_tests)
        {

        }
        else if (id == R.id.show_users)
        {
            UsersActivity.UsersFragment fragment = UsersActivity.UsersFragment.newInstance(user.getUid());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home, fragment)
                    .commit();
            //Intent intent = UsersActivity.makeIntent(this, user.getUid());
            //startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void navigateToUserHistory(final String userId) {
        UserHistoryActivity.UserHistoryFragment fragment = UserHistoryActivity.UserHistoryFragment.newInstance(userId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home, fragment)
                .addToBackStack(UserHistoryActivity.UserHistoryFragment.TAG)
                .commit();

    }

    public void navigateToCycleTestHistory(final String userId) {
        CycleTestsActivity.CycleTestsFragment fragment = CycleTestsActivity.CycleTestsFragment.newInstance(userId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home, fragment)
                .addToBackStack(CycleTestsActivity.CycleTestsFragment.TAG)
                .commit();

    }

    private String getRole(final String email) {
        if (("blueviewaquatics@gmail.com").equalsIgnoreCase(email)) {
            return "admin";
        }
        return "user";
    }

    private void setupHomeMenu() {
        Menu menu = navigationView.getMenu();
        if(user.getRole() == "admin") {
            menu.setGroupVisible(R.id.user_group, false);
            menu.findItem(R.id.show_users).setVisible(true);
            menu.findItem(R.id.user_communicate_item).setVisible(false);
        }
        else {
            menu.setGroupVisible(R.id.user_group, true);
            menu.findItem(R.id.show_users).setVisible(false);
            menu.findItem(R.id.user_communicate_item).setVisible(true);
        }
    }
}
