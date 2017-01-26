package furtiveops.com.blueviewmanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

    @BindView(R.id.container)
    RelativeLayout contentHome;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mFirebaseDatabaseReference;

    private User user;
    private String currentFragmentTag;

    public static final int RC_LOGGED_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.home_screen_entire_layout);

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View newView = view;
                int j;
                j = 10;
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
                currentFragmentTag = backEntry.getName();
                updateFAB(currentFragmentTag);
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
                    .replace(R.id.container, fragment, UsersActivity.UsersFragment.TAG)
                    .addToBackStack(UsersActivity.UsersFragment.TAG)
                    .commit();

            //getSupportFragmentManager().executePendingTransactions();
        }
        else if(id == R.id.sign_out)
        {
            mFirebaseAuth.signOut();
            Intent intent = SignInActivity.makeIntent(this);
            startActivityForResult(intent, RC_LOGGED_IN);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void navigateToUserHistory(final String userId) {
        UserHistoryActivity.UserHistoryFragment fragment = UserHistoryActivity.UserHistoryFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, UserHistoryActivity.UserHistoryFragment.TAG)
                .addToBackStack(UserHistoryActivity.UserHistoryFragment.TAG)
                .commit();

        //getSupportFragmentManager().executePendingTransactions();
    }

    public void navigateToCycleTestHistory(final String userId) {

        CycleTestsActivity.CycleTestsFragment fragment = CycleTestsActivity.CycleTestsFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, CycleTestsActivity.CycleTestsFragment.TAG)
                .addToBackStack(CycleTestsActivity.CycleTestsFragment.TAG)
                .commit();

        //getSupportFragmentManager().executePendingTransactions();
    }

    public void navigateToServices(final String userId) {

        ServicesActivity.ServicesFragment fragment = ServicesActivity.ServicesFragment.newInstance(userId);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, ServicesActivity.ServicesFragment.TAG)
                .addToBackStack(ServicesActivity.ServicesFragment.TAG)
                .commit();

       // getSupportFragmentManager().executePendingTransactions();
    }

    private String getRole(final String email) {
        if (("blueviewaquatics@gmail.com").equalsIgnoreCase(email)) {
            return "admin";
        }
        return "user";
    }

    private void setupHomeMenu() {
        fab.setVisibility(View.GONE);

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

    private void updateFAB(final String fragmentId) {
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
