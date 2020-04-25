package com.mohamedamgd.chatapp.ui.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.mohamedamgd.chatapp.R;
import com.mohamedamgd.chatapp.ui.MainActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mUsernameEditText;
    EditText mEmailEditText;
    EditText mPasswordEditText;
    EditText mConfirmPasswordEditText;
    Button mSignUpButton;
    TextView mLoginTextView;
    ProgressBar mLoadingProgressBar;
    ImageView mImageView;
    AnimatedVectorDrawableCompat mAnimatedVector;
    private String TAG = getClass().getSimpleName();
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        checkUser();
        mUsernameEditText = findViewById(R.id.username);
        mEmailEditText = findViewById(R.id.email);
        mPasswordEditText = findViewById(R.id.password);
        mConfirmPasswordEditText = findViewById(R.id.confirm_password);
        mSignUpButton = findViewById(R.id.sign_up);
        mLoginTextView = findViewById(R.id.login);
        mLoadingProgressBar = findViewById(R.id.loading);

        mImageView = findViewById(R.id.logo);

        mAnimatedVector = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim);
        mImageView.setImageDrawable(mAnimatedVector);
        final Handler mainHandler = new Handler(Looper.getMainLooper());
        mAnimatedVector.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationEnd(final Drawable drawable) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAnimatedVector.start();
                    }
                });
            }
        });
        mAnimatedVector.start();

        mSignUpButton.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);
    }

    private boolean validInput() {
        boolean result = true;
        String username = mUsernameEditText.getText().toString().trim();
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            mUsernameEditText.setError(getString(R.string.required));
            result = false;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailEditText.setError(getString(R.string.required));
            result = false;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.required));
            result = false;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            mConfirmPasswordEditText.setError(getString(R.string.required));
            result = false;
        }
        if (!(TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmPassword))) {
            if (!email.contains("@")) {
                mUsernameEditText.setError(getString(R.string.invalid_username));
                result = false;
            }
            if (!(password.length() > 5)) {
                mPasswordEditText.setError(getString(R.string.invalid_password));
                result = false;
            }
            if (!confirmPassword.equals(password)) {
                mConfirmPasswordEditText.setError(getString(R.string.invalid_confirm));
                result = false;
            }
        }
        return result;
    }

    private void showRegisterSuccessful() {
        // Sign in success, update UI with the signed-in user's information
        final String username = mUsernameEditText.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;
        user.updateProfile(new UserProfileChangeRequest
                .Builder()
                .setDisplayName(username)
                .build()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext()
                        , getString(R.string.registered),
                        Toast.LENGTH_SHORT).show();

                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    private void showRegisterFailed(Task<AuthResult> task) {
        // If sign in fails, display a message to the user.
        try {
            if (task != null && task.getException() != null) {
                throw task.getException();
            }
        } catch (FirebaseAuthWeakPasswordException e) {
            mPasswordEditText.setError(getString(R.string.weak_password));
            mPasswordEditText.requestFocus();
        } catch (FirebaseAuthInvalidCredentialsException e) {
            mEmailEditText.setError(getString(R.string.invalid_username));
            mEmailEditText.requestFocus();
        } catch (FirebaseAuthUserCollisionException e) {
            mEmailEditText.setError(getString(R.string.user_exists));
            mEmailEditText.requestFocus();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.register_failed),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());

        }
    }

    void checkUser() {
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sign_up) {
            if (validInput()) {
                // Check if no view has focus:
                View view = getCurrentFocus();
                if (view != null) {
                    //hide soft keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mLoadingProgressBar.setVisibility(View.VISIBLE);
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    showRegisterSuccessful();
                                } else {
                                    try {
                                        showRegisterFailed(task);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                mLoadingProgressBar.setVisibility(View.GONE);
                            }
                        });
            }
        } else if (v.getId() == R.id.login) {
            finish();
        }
    }
}
