package furtiveops.com.blueviewmanager.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.contentProviders.SettingsContract;
import furtiveops.com.blueviewmanager.models.User;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PropertyChangeListener {

    static final String TAG = "HomeActivity";

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.container)
    RelativeLayout contentHome;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FirebaseAuth mFirebaseAuth;

    private User user;                              // Currently logged in user.
    private String currentFragmentTag;
    private FirebaseUser firebaseUser = null;
    private String selectedUserId;                  // For admin only.  The selected user from list
    private String selectedServiceUUID;             // For admin only.  The selected service to add.

    public static final int RC_LOGGED_IN = 100;
    public static final int RC_CREATED_USER = 101;

    private String adminUserName;
    private String adminPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen_entire_layout);

        ButterKnife.bind(this);
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick(currentFragmentTag);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.openDrawer(GravityCompat.START, true);

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
                if(index >= 0) {
                    FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                    currentFragmentTag = backEntry.getName();
                    updateFAB(currentFragmentTag);
                }
            }
        });

        if (null != savedInstanceState) {
            currentFragmentTag = savedInstanceState.getString(IntentConstants.CURRENT_FRAGMENT_TAG);
            updateFAB(currentFragmentTag);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IntentConstants.CURRENT_FRAGMENT_TAG, currentFragmentTag);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RC_LOGGED_IN == requestCode && Activity.RESULT_OK == resultCode) {
            if(null != data) {
                final String userId = data.getStringExtra(IntentConstants.USER_ID);
                final String email = data.getStringExtra(IntentConstants.USER_EMAIL);
                final String password = data.getStringExtra(IntentConstants.PASSWORD);

                user = new User(userId, email, getRole(email));
                if(user.getRole().equals("admin"))
                    updateSettings(email, password);

            }
            setupHomeMenu();
        }
        else if(RC_CREATED_USER == requestCode && Activity.RESULT_OK == resultCode){
            if(null != data) {
                final String userId = data.getStringExtra(IntentConstants.USER_ID);
                final String email = data.getStringExtra(IntentConstants.USER_EMAIL);
                User newUser = new User(userId, email, getRole(email));

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(UsersActivity.UsersFragment.TAG);
                if(null != fragment)
                {
                    UsersActivity.UsersFragment usersFragment = (UsersActivity.UsersFragment)fragment;
                    usersFragment.createUserInDatabase(newUser);
                }

                mFirebaseAuth.signOut();

                mFirebaseAuth.signInWithEmailAndPassword(adminUserName, adminPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                        }
                        else
                        {
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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
        else if (id == R.id.nav_service_history)
        {
            navigateToServices(user.getUid());
        }
        else if (id == R.id.nav_schedule_work)
        {

        }
        else if (id == R.id.nav_shop_bva)
        {

        }
        else if (id == R.id.nav_track_tests)
        {
            navigateToCycleTestHistory(user.getUid());
        }
        else if (id == R.id.show_users)
        {
            UsersActivity.UsersFragment fragment = UsersActivity.UsersFragment.newInstance(user.getUid());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment, UsersActivity.UsersFragment.TAG)
                    .addToBackStack(UsersActivity.UsersFragment.TAG)
                    .commit();
        }
        else if(id == R.id.sign_out)
        {
            mFirebaseAuth.signOut();
            Intent intent = SignInActivity.makeIntent(this);
            startActivityForResult(intent, RC_LOGGED_IN);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateSettings (final String userName, final String password) {
        ContentValues values = new ContentValues();
        values.put(SettingsContract.Settings.COLUMN_NAME_USERNAME, userName);
        values.put(SettingsContract.Settings.COLUMN_NAME_PASSWORD, password);

        Uri uri = this.getContentResolver().insert(SettingsContract.Settings.CONTENT_URI, values);
    }

    public void navigateToUserHistory(final String userId) {
        UserHistoryActivity.UserHistoryFragment fragment = UserHistoryActivity.UserHistoryFragment.newInstance(userId);
        selectedUserId = userId;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, UserHistoryActivity.UserHistoryFragment.TAG)
                .addToBackStack(UserHistoryActivity.UserHistoryFragment.TAG)
                .commit();
    }

    public void navigateToCycleTestHistory(final String userId) {

        CycleTestsActivity.CycleTestsFragment fragment = CycleTestsActivity.CycleTestsFragment.newInstance(userId);
        selectedUserId = userId;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, CycleTestsActivity.CycleTestsFragment.TAG)
                .addToBackStack(CycleTestsActivity.CycleTestsFragment.TAG)
                .commit();
    }

    public void navigateToServices(final String userId) {

        ServicesActivity.ServicesFragment fragment = ServicesActivity.ServicesFragment.newInstance(userId);
        selectedUserId = userId;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, ServicesActivity.ServicesFragment.TAG)
                .addToBackStack(ServicesActivity.ServicesFragment.TAG)
                .commit();
    }

    private String getRole(final String email) {
        if (("blueviewaquatics@gmail.com").equalsIgnoreCase(email)) {
            return "admin";
        }
        return "user";
    }

    private void setupHomeMenu() {

        fab.setVisibility(View.GONE);

        Cursor c = this.getContentResolver().query(SettingsContract.Settings.CONTENT_URI,
                SettingsContract.Settings.PROJECTION_ALL,
                "user_name = ?", new String[]{user.getUserName()},
                SettingsContract.Settings.DEFAULT_SORT_ORDER);

        if(c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                adminUserName = c.getString(1);
                adminPassword = c.getString(2);
            }
        }
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
        drawerLayout.openDrawer(GravityCompat.START, true);
    }

    private void updateFAB(final String fragmentId) {
        if(user.getRole() == "user") {
            fab.setVisibility(View.GONE);
        }
        else {
            if(UsersActivity.UsersFragment.TAG.equals(fragmentId))
            {
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.mipmap.ic_account_plus_white_24dp);

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
                params.setMargins(0, 0, params.rightMargin, 42);
                fab.setLayoutParams(params);
                fab.requestLayout();
            }
            else if(UserHistoryActivity.UserHistoryFragment.TAG.equals(fragmentId))
            {
                fab.setVisibility(View.GONE);
            }
            else if(CycleTestsActivity.CycleTestsFragment.TAG.equals(fragmentId))
            {
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.mipmap.ic_test_tube_white_24dp);

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
                params.setMargins(0, 0, params.rightMargin, 42);
                fab.setLayoutParams(params);
                fab.requestLayout();
            }
            else if(ServicesActivity.ServicesFragment.TAG.equals(fragmentId))
            {
                fab.setVisibility(View.VISIBLE);
                fab.setImageResource(R.mipmap.ic_screwdriver_white_24dp);
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)fab.getLayoutParams();
                params.setMargins(0, 0, params.rightMargin, 110);
                fab.setLayoutParams(params);
                fab.requestLayout();
            }
        }
    }

    private void fabClick(final String fragmentId) {
        if(UsersActivity.UsersFragment.TAG.equals(fragmentId))
        {
            Intent intent = SignUpActivity.makeInent(this, user);
            startActivityForResult(intent, RC_CREATED_USER);
        }
        else if(UserHistoryActivity.UserHistoryFragment.TAG.equals(fragmentId))
        {
        }
        else if(CycleTestsActivity.CycleTestsFragment.TAG.equals(fragmentId))
        {
            Intent intent = AddCycleTestActivity.makeIntent(this, selectedUserId);
            startActivity(intent);
        }
        else if(ServicesActivity.ServicesFragment.TAG.equals(fragmentId))
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentId);
            if(null != fragment && fragment instanceof ServicesActivity.ServicesFragment) {
                Intent intent = AddServiceActivity.makeIntent(this, selectedUserId, ((ServicesActivity.ServicesFragment)fragment).getSelectedUUID());
                startActivity(intent);
            }
        }
    }

    /*
     * PropertyChangedListener
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
