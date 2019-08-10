package com.example.insta_clone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone.Home.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();


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

        email = input_email.getText().toString();
        password = input_password.getText().toString();


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

    }


    /**
     * FIREBASE AUTHENTICATION METHOD
     */

    @Override
    public void onStart() {

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        //Check if user is signed in (not null)
        if (currentUser == null) {
            Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT);
        } else {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        //updateUI(currentUser);
        super.onStart();
    }


}
