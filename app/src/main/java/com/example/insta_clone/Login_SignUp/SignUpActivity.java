package com.example.insta_clone.Login_SignUp;

import android.content.Context;
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

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
String username;
FirebaseMethods firebaseMethods;


FirebaseDatabase firebaseDatabase;
DatabaseReference myRef;




private FirebaseAuth mAuth;
private Context mContext;
    DataSnapshot dataSnapshot;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        btnSignUp = findViewById(R.id.btn_signUp);

        input_name = findViewById(R.id.input_full_name);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        mProgressBar = findViewById(R.id.loginRequestLoadingProgressbar);
        mPleaseWaitText = findViewById(R.id.pleaseWaitText);
        email = input_email.getText().toString();
        password = input_password.getText().toString();
        username = input_name.getText().toString();

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseMethods = new FirebaseMethods(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("users");


        go_to_login = findViewById(R.id.link_signin);
        go_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        setupFirebaseAuth();
        init();




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
     *
     * 21-08-2019
     * Discontinued Create Account method
     * setUpFirebaseAuth() - verifies if user is signed in, checks username availability
     * init() - checks null input, calls registerNewUser
     *
     */






    private void init(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = input_email.getText().toString();
                username = input_name.getText().toString();
                password = input_password.getText().toString();

                if(checkInputs(email, username, password)){
                    mProgressBar.setVisibility(View.GONE);

                    firebaseMethods.registerNewEmail(email, password, username);
                }
            }
        });
    }

   /* private void createAccount()
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
                username = input_name.getText().toString();

                if(!checkInputs(email, username, password )){
                    Toast.makeText(getApplicationContext(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                }


                //if (email.trim().equals("") || password.equals("")) {
                //    Toast.makeText(getApplicationContext(), "You must fill out all the fields", Toast.LENGTH_SHORT).show();
                //}


                else {
                    Log.d(TAG, "inside else part");
                    mProgressBar.setVisibility(View.GONE);

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //1st check: Make sure the username is not already in use
                            if(firebaseMethods.checkIfUsernameExists(username, dataSnapshot)){
                                Log.d(TAG, "onDataChange: username already exists.");

                            }
                            //add new user to the database
                            firebaseMethods.addNewUser(email, username, " ", " ", " ", 1823879);

                            Toast.makeText(getApplicationContext(), "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();




                            else{
                                Log.d(TAG, "Username " + username +  " taken");
                                Toast.makeText(getApplicationContext(), "Username taken", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


               /*     mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        Toast.makeText(getApplicationContext(), "Sign Up success!.",Toast.LENGTH_SHORT).show();

                                        firebaseMethods.addNewUser(email, username, " ", " ", " ", 3284798);

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

*/




    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }





    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");



        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference("users");
        //initialising username to avoid null pointer exception
        username="balaji";


                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //1st check: Make sure the username is not already in use


                        if(firebaseMethods.checkIfUsernameExists(username, dataSnapshot)){

                            Log.d(TAG, "onDataChange: username already exists");
                        }

                        else {
                            //add new user to the database
                            Log.d(TAG, "username available");

                            firebaseMethods.addNewUser(email, username, " ", " ", " ", 1234567);

                            Toast.makeText(getApplicationContext(), "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





                // ...
            }



}
