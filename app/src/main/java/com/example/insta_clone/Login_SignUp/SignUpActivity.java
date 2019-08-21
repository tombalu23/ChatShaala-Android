package com.example.insta_clone.Login_SignUp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
TextView go_to_login;
String TAG = "signUpActivity";
EditText input_email;
EditText input_password;
EditText input_name;
EditText input_mobile_no;
ProgressBar mProgressBar;
Button btnSignUp;
TextView mPleaseWaitText;
String email;
String password;

private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp= findViewById(R.id.btn_login);

        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        mProgressBar = findViewById(R.id.loginRequestLoadingProgressbar);
        mPleaseWaitText = findViewById(R.id.pleaseWaitText);
        email = input_email.getText().toString();
        password = input_password.getText().toString();

        //Firebase
        mAuth = FirebaseAuth.getInstance();


        go_to_login = findViewById(R.id.link_signin);
        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        createAccount();

    }


    /**
     * NOTE by Balaji 17/08/2019
     * Create Account method
     *
     * currently (17/08/2019) creates account with just email and password
     *
     *
     * TODO Get Name, College Name and store in Firebase
     *
     * TODO Navigate to Home Activity on success
     */

    private void createAccount()
    {
        mProgressBar.setVisibility(View.INVISIBLE);
        mPleaseWaitText.setVisibility(View.INVISIBLE);

        //initialize the button for logging in
        Button btn_signUp = (Button) findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to sign up.");

                email = input_email.getText().toString();
                password = input_password.getText().toString();

                if (email.trim().equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressBar.setVisibility(View.VISIBLE);

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(getApplicationContext(), "Sign Up success!.",Toast.LENGTH_SHORT).show();

                                        //TODO Navigate to Home Activity on successful sign up



                                        FirebaseUser user = mAuth.getCurrentUser();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

}


}
