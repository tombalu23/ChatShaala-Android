package com.example.insta_clone.Login_SignUp;

import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    String TAG = "LoginActivity";

    AppCompatButton btnLogin;
    TextView sign_up_text;
    EditText input_email;
    EditText input_password;
    String email;
    String password;
    TextView mPleaseWaitText;
    ProgressBar mProgressBar;
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
        mPleaseWaitText = findViewById(R.id.pleaseWaitText);
        email = input_email.getText().toString();
        password = input_password.getText().toString();

        //Firebase
        mAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        sign_up_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);

            }
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
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to log in.");

                String email = input_email.getText().toString();
                String password = input_password.getText().toString();

                if(email.trim().equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }else{
                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.w(TAG, "signInWithEmail:failed", task.getException());

                                        Toast.makeText(LoginActivity.this, "Sign In Failed",
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWaitText.setVisibility(View.GONE);

                                    }
                                    else{
                                        Log.d(TAG, "signInWithEmail: successful login");
                                        Toast.makeText(LoginActivity.this, "Sign In Success",
                                                Toast.LENGTH_SHORT).show();
                                        mProgressBar.setVisibility(View.GONE);
                                        mPleaseWaitText.setVisibility(View.GONE);

                                        //Navigate to Home Activity on sign in success
                                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                        startActivity(intent);
                                    }

                                    // ...
                                }
                            });
                }

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
