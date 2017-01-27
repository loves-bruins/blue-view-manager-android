package furtiveops.com.blueviewmanager.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import furtiveops.com.blueviewmanager.IntentConstants;
import furtiveops.com.blueviewmanager.R;
import furtiveops.com.blueviewmanager.models.User;
import furtiveops.com.blueviewmanager.utils.ValidationUtils;
import info.hoang8f.widget.FButton;

/**
 * Created by lorenrogers on 1/26/17.
 */

public class SignUpActivity extends AppCompatActivity {

    private static final String LOG_TAG = SignUpActivity.class.getSimpleName();

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.create)
    FButton create;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private boolean validParams = false;

    private User admin;

    public static Intent makeInent(final Context context, final User admin)
    {
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.putExtra(IntentConstants.USER_OBJECT, admin);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup_layout);

        ButterKnife.bind(this);

        admin = getIntent().getParcelableExtra(IntentConstants.USER_OBJECT);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateParams();
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
                validateParams();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.create)
    public void onCreate()
    {
        if (!validateForm()) {
            return;
        }

//        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            SnackbarManager.show(Snackbar.with(SignUpActivity.this).text(R.string.auth_failed));
                        }
                        else {

                            // For some reason Google decided to make the decision that when you create a user
                            // with this call, it will log you in with this user.
                            Intent intent = new Intent();
                            intent.putExtra(IntentConstants.USER_ID, task.getResult().getUser().getUid());
                            intent.putExtra(IntentConstants.USER_EMAIL, task.getResult().getUser().getEmail());

                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void validateParams()
    {
        validParams = false;
        create.setEnabled(false);

        boolean validEmail = false;
        boolean validPassword = false;

        final String emailText = email.getText().toString();
        final String passwordText = password.getText().toString();

        if(null != emailText && !emailText.isEmpty())
        {
            validEmail = ValidationUtils.isEmailValid(emailText);
        }

        if(null != passwordText && !passwordText.isEmpty())
        {
            validPassword = true;
        }

        if(validEmail && validPassword)
        {
            validParams = true;
            create.setEnabled(true);
        }
    }
    private boolean validateForm() {
        boolean valid = true;

        String emailText = email.getText().toString();
        if (TextUtils.isEmpty(emailText)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String passwordText = password.getText().toString();
        if (TextUtils.isEmpty(passwordText)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
}
