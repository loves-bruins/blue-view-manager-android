package furtiveops.com.blueviewmanager.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.contentProviders.SettingsContract;
import info.hoang8f.widget.FButton;

/**
 * Created by lorenrogers on 12/1/16.
 */

public class SignInActivity extends AppCompatActivity  {

    private static final String TAG = SignInActivity.class.getSimpleName();

    @BindView(R.id.username)
    EditText userName;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.login)
    FButton login;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    public static Intent makeIntent(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signin_layout);

        ButterKnife.bind(this);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        // [END auth_state_listener]

        setupView();
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // [END on_stop_remove_listener]
    @OnClick (R.id.login)
    public void signIn() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0/* InputMethodManager.HIDE_IMPLICIT_ONLY*/);
        }
        mAuth.signInWithEmailAndPassword(userName.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            SnackbarManager.show(Snackbar.with(SignInActivity.this).text(R.string.auth_failed));
                        }
                        else {
                            final AuthResult authResult = task.getResult();

                            Intent intent = new Intent();
                            intent.putExtra(IntentConstants.USER_ID, authResult.getUser().getUid());
                            intent.putExtra(IntentConstants.USER_EMAIL, authResult.getUser().getEmail());
                            intent.putExtra(IntentConstants.PASSWORD, password.getText().toString());
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }
                        //finishActivity(HomeActivity.RC_LOGGED_IN);
                    }
                });
    }

    protected void setupView() {
        login.setEnabled(false);
        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!userName.getText().toString().isEmpty()
                        && !password.getText().toString().isEmpty()) {
                    login.setEnabled(true);
                }
                else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!userName.getText().toString().isEmpty()
                        && !password.getText().toString().isEmpty()) {
                    login.setEnabled(true);
                }
                else {
                    login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//        userName.setText("blueviewaquatics@gmail.com");
//        password.setText("password");
    }
}
