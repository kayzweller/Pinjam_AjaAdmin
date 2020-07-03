/*
 * Copyright (c) 2020 Albert Kristaen (DBC 113 008)
 * ONLY USE UNDER PERMISSION -OR- I AM GONNA CHOP YOUR HANDS OFF!
 */

package com.xoxltn.pinjam_ajaadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainLoginActivity extends AppCompatActivity {

    //deklarasi variabel
    TextInputLayout mEmailInput, mPasswordInput;
    Button mMasukButton;
    ProgressBar mProgressBar;

    //deklarasi firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;
    private FirebaseUser mFireUser;

    //-------------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        //hooks
        mEmailInput = findViewById(R.id.login_email);
        mPasswordInput = findViewById(R.id.login_password);
        mMasukButton = findViewById(R.id.login_button);
        mProgressBar = findViewById(R.id.progress_bar_loading);

        mAuth = FirebaseAuth.getInstance();
        mFireUser = mAuth.getCurrentUser();
        mFire = FirebaseFirestore.getInstance();
    }

    //-------------------------------------------------------------------------------------------//

    @Override
    public void onStart() {
        super.onStart();

        mProgressBar.setMax(100);
        mProgressBar.setAlpha(0f);
        mProgressBar.setProgress(0);

        executeAutoLogin();
    }

    //-------------------------------------------------------------------------------------------//

    private void executeAutoLogin() {
        if (mFireUser != null) {
            progressBarLoad();
            Intent mainActivity = new Intent(this, MainDashboardActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }

    //-------------------------------------------------------------------------------------------//

    private Boolean validateEmail() {
        String val = Objects.requireNonNull(mEmailInput.getEditText()).getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z.-]+\\.[a-z]+";

        if (val.isEmpty()) {
            mEmailInput.setError("Masukan alamat email Anda!");
            return false;
        } else if (!val.matches(emailPattern)) {
            mEmailInput.setError("Alamat email salah!");
            return false;
        } else {
            mEmailInput.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = Objects.requireNonNull(mPasswordInput.getEditText()).getText().toString();

        if (val.isEmpty()) {
            mPasswordInput.setError("Masukan password Anda!");
            return false;
        } else {
            mPasswordInput.setError(null);
            return true;
        }
    }

    public void onLoginButtonClick(View v) {

        progressBarLoad();

        if (!validateEmail() | !validatePassword()) {
            progressBarUnload();
            return;
        }

        String email = Objects.requireNonNull(mEmailInput.getEditText()).getText().toString();
        String password = Objects.requireNonNull(mPasswordInput.getEditText()).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            String keyAdmin = "(vNSDP534cgPHAbqocLjJmgQm68d2)";

                            if (userID.matches(keyAdmin)) {
                                progressBarUnload();
                                Toast.makeText(getApplicationContext(), "LOGIN BERHASIL!",
                                        Toast.LENGTH_SHORT).show();
                                Intent bioActivity = new Intent(MainLoginActivity.
                                        this, MainDashboardActivity.class);
                                startActivity(bioActivity);
                                finish();
                            }
                            /*
                            else {
                                progressBarUnload();
                                Toast.makeText(getApplicationContext(),
                                        "Anda Tidak Memiliki Hak Akses Admin!",
                                        Toast.LENGTH_LONG).show();
                            }
                             */

                        } else {
                            progressBarUnload();
                            Toast.makeText(getApplicationContext(), Objects
                                    .requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                });

    }

    //-------------------------------------------------------------------------------------------//

    private void progressBarLoad() {
        mProgressBar.setAlpha(1.0f);
        mProgressBar.setProgress(100);

        mMasukButton.setEnabled(false);
    }

    private void progressBarUnload() {
        mProgressBar.setAlpha(0f);
        mProgressBar.setProgress(0);

        mMasukButton.setEnabled(true);
    }

    //-------------------------------------------------------------------------------------------//

    // variabel untuk fungsi tombol BACK
    boolean doubleBackToExitPressedOnce = false;
    int DELAY_PRESS = 2000;

    // method untuk tekan tombol BACK dua kali untuk keluar
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, DELAY_PRESS);
    }

}
