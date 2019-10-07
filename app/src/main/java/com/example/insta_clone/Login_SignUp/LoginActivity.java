package com.example.insta_clone.Login_SignUp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone.Home.HomeActivity;
import com.example.insta_clone.R;
import com.example.insta_clone.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";

    AppCompatButton btnLogin;
    TextView sign_up_text;
    EditText input_email;
    EditText input_password;
    String email;
    String password;
    ProgressBar mProgressBar;
    SharedPreferences sp;
    private FirebaseAuth mAuth;


/*
= new FirebaseAuth().getInstance();
Initialize Firebase Auth
mAuth = FirebaseAuth.getInstance();
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        sign_up_text = findViewById(R.id.link_signup);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        mProgressBar = findViewById(R.id.loginRequestLoadingProgressbar);
        email = input_email.getText().toString();
        password = input_password.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("My_Preferences", Context.MODE_PRIVATE);
        if(sp.getBoolean("loggedin",false)){
            finish();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        sign_up_text.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(i);
        });

        init();

    }


    /**NOTE by Balaji 11-08-2019
     * FIREBASE AUTHENTICATION METHOD
     * Modified code from Firebase docs
     */

    private void init(){
        mProgressBar.setVisibility(View.INVISIBLE);
        //initialize the button for logging in
        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(v -> {
            Log.d(TAG, "onClick: attempting to log in.");

            String email = input_email.getText().toString();
            String password = input_password.getText().toString();

            if(email.trim().equals("") || password.equals("")){
                Toast.makeText(getApplicationContext(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
            }else{
                btnLogin.setEnabled(false);
                Utils.hideKeyboard(LoginActivity.this);
                mProgressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            btnLogin.setEnabled(true);

                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());

                                Toast.makeText(LoginActivity.this, String.format("Login failed : %s",
                                        Objects.requireNonNull(task.getException()).getLocalizedMessage()),
                                        Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                            else{
                                Log.d(TAG, "signInWithEmail: successful login");
                                mProgressBar.setVisibility(View.GONE);

                                //added by girish 26-08-2019 - Saving Username in Shared-Preferences
                                String currentuser = mAuth.getCurrentUser().getDisplayName();
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("username",currentuser);
                                editor.putBoolean("loggedin",true);
                                editor.apply();


                                finish();

                                //Navigate to Home Activity on sign in success
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                            }

                            // ...
                        });
            }

        });
    }


    /**
     * NOTE by Balaji 11-08-19
     * Uncomment else block for auto sign-in
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Check if user is signed in (not null)
        if (currentUser == null) {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT);
        }
        else {
           // Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
           // startActivity(intent);
        }



    }



}
