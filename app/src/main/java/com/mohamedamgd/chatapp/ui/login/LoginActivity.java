package com.mohamedamgd.chatapp.ui.login;
/**
 * Copyright 2020 Mohamed Amgd
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import com.google.firebase.auth.FirebaseUser;
import com.mohamedamgd.chatapp.R;
import com.mohamedamgd.chatapp.ui.MainActivity;
import com.mohamedamgd.chatapp.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //TAG
    private String TAG = getClass().getSimpleName();

    //UI
    EditText mUsernameEditText;
    EditText mPasswordEditText;
    Button mLoginButton;
    TextView mRegisterTextView;
    ProgressBar mLoadingProgressBar;
    ImageView mImageView;
    AnimatedVectorDrawableCompat mAnimatedVector;

    //Firebase
    private FirebaseAuth mAuth;

    private boolean isBackClicked = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        if (checkUser(mAuth.getCurrentUser())) {
            moveToMain();
        }
        mUsernameEditText = findViewById(R.id.username);
        mPasswordEditText = findViewById(R.id.password);
        mLoginButton = findViewById(R.id.sign_in);
        mRegisterTextView = findViewById(R.id.register);
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

        mRegisterTextView.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }

    private boolean validInput() {
        boolean result = true;
        String email = mUsernameEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mUsernameEditText.setError(getString(R.string.required));
            result = false;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordEditText.setError(getString(R.string.required));
            result = false;
        }
        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            if (!email.contains("@")) {
                mUsernameEditText.setError(getString(R.string.invalid_username));
                result = false;
            }
            if (!(password.length() > 5)) {
                mPasswordEditText.setError(getString(R.string.invalid_password));
                result = false;
            }
        }
        return result;
    }

    private void showLoginSuccessful() {
        // Sign in success, update UI with the signed-in user's information
        FirebaseUser user = mAuth.getCurrentUser();
        if (!checkUser(user)) return;
        String username = user.getDisplayName();
        if (username == null) return;
        Toast.makeText(getApplicationContext()
                , getString(R.string.welcome).concat(username),
                Toast.LENGTH_SHORT).show();
        moveToMain();
    }

    private void showLoginFailed(Task<AuthResult> task) {
        // If sign in fails, display a message to the user.
        try {
            if (task != null && task.getException() != null) {
                throw task.getException();
            }
        } catch (FirebaseAuthInvalidCredentialsException  e) {
            mUsernameEditText.setError(null);
            mPasswordEditText.setError(null);
            Toast.makeText(this, getString(R.string.invalid_credentials),
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.login_failed),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, e.toString());
        }
    }

    boolean checkUser(FirebaseUser user) {
        return user != null;
    }

    void moveToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in:
                if (validInput()) {
                    // Check if no view has focus:
                    View view = getCurrentFocus();
                    if (view != null) {
                        //hide soft keyboard
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    mLoadingProgressBar.setVisibility(View.VISIBLE);
                    String email = mUsernameEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString().trim();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        showLoginSuccessful();
                                    } else {
                                        showLoginFailed(task);
                                    }
                                    mLoadingProgressBar.setVisibility(View.GONE);
                                }
                            });
                }
                break;
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackClicked) {
            super.onBackPressed();
            finish();

        } else {
            Toast.makeText(this
                    , getString(R.string.back_again_to_exit)
                    , Toast.LENGTH_SHORT).show();

            isBackClicked = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackClicked = false;
                }
            }, 2000);
        }

    }
}
