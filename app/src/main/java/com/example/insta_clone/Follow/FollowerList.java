package com.example.insta_clone.Follow;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.insta_clone.Adapters.Users_listView_Adapter;
import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.Models.User;
import com.example.insta_clone.Profile.Friendprofile;
import com.example.insta_clone.R;
import com.example.insta_clone.bottomNavigationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class FollowerList extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList= new ArrayList();
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    private BottomNavigationView bottomNavigationView;
    private int ACTIVITY_NUM = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);
        listView = findViewById(R.id.listView);
        userID = getIntent().getExtras().getString("userID");

        setupBottomNav();
        setupFirebaseAuth();


        DatabaseReference UsersRef = myRef.child("following").child(userID);
        UsersRef.addValueEventListener(new ValueEventListener() {



            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    arrayList.add((String) ds.getValue());
                }
                Users_listView_Adapter posts_listView_adapter = new Users_listView_Adapter(getApplicationContext(), arrayList);
                listView.setAdapter(posts_listView_adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String user_id = arrayList.get(position);

                //Toast.makeText(getApplicationContext(), user_id, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), Friendprofile.class);
                intent.putExtra("userID", user_id);
                startActivity(intent);

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();


    }
    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();

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
                // ...
            }
        };
    }

    private void setupBottomNav() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        bottomNavigationHelper help = new bottomNavigationHelper();
        help.bottomNavigationSetup(bottomNavigationView, getApplicationContext());
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

}
