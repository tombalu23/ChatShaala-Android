package com.example.insta_clone.Profile;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.GridImageAdapter;
import com.example.insta_clone.Models.Post;
import com.example.insta_clone.Models.UserSettings;
import com.example.insta_clone.R;
import com.example.insta_clone.UniversalImageLoader;
import com.example.insta_clone.bottomNavigationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friendprofile extends AppCompatActivity {


    private TextView mDisplayName;
    private TextView mUsername;
    private TextView mDescription;
    private CircleImageView mProfilePhoto;
    private TextView mPosts;
    private TextView mFollowers;
    private TextView mFollowing;
    private ProgressBar mProgressBar;
    private GridView gridView;
    private ImageView profileMenu;
    private BottomNavigationView bottomNavigationView;
    private TextView follow;
    private static final int ACTIVITY_NUM = 0;
    private String userID;
    private String currentUserID;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private static final String TAG = "FriendProfileActivity";
    ImageLoader mImageLoader;
    UniversalImageLoader universalImageLoader;
    private FirebaseMethods firebaseMethods;
    private TextView followButton;
    boolean followStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendprofile);
        mDisplayName = (TextView) findViewById(R.id.display_name);
        mUsername = (TextView) findViewById(R.id.profileName);
        mDescription = (TextView) findViewById(R.id.descriptionText);
        mProfilePhoto = (CircleImageView) findViewById(R.id.profile_image);
        mPosts = (TextView) findViewById(R.id.tvPosts);
        mFollowers = (TextView) findViewById(R.id.tvFollowers);
        mFollowing = (TextView) findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        gridView = (GridView) findViewById(R.id.profileImagesGridView);
        profileMenu = (ImageView) findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavViewBar);
        followButton = (TextView) findViewById(R.id.btn_follow);
        setupBottomNavigationView();
        initImageLoader();
        setupFirebaseAuth();
        userID = getIntent().getExtras().getString("userID");
        firebaseMethods = new FirebaseMethods(getApplicationContext());
        isFollow(currentUserID, userID);



        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(followStatus == false) {
                    firebaseMethods.followUser(userID);
                    followButton.setText("Unfollow");
                    followButton.setBackgroundColor(getColor(android.R.color.holo_red_light));
                    followStatus=true;
                }

                else
                {
                    firebaseMethods.unfollowUser(userID);
                    followButton.setText("Follow");
                    followButton.setBackgroundColor(getColor(android.R.color.holo_blue_light));
                    followStatus=false;
                }
            }


        });

    }


    public void SetProfileWidgets(UserSettings userSettings) {

        Log.d(TAG, "SetProfileWidgets: setting profile widgets" + userSettings);
        universalImageLoader.setImage(userSettings.getUserAccountSettings().getProfile_photo(), mProfilePhoto, null, "");
        mDisplayName.setText(userSettings.getUserAccountSettings().getDisplay_name());
        mDescription.setText(userSettings.getUserAccountSettings().getCollege_name());
        mFollowers.setText(String.valueOf(userSettings.getUserAccountSettings().getFollowers()));
        mFollowing.setText(String.valueOf(userSettings.getUserAccountSettings().getFollowing()));
        mPosts.setText(String.valueOf(userSettings.getUserAccountSettings().getPosts()));
        mUsername.setText(userSettings.getUser().getUsername());
        tempGridSetup();

    }

    private void setupBottomNavigationView() {
        Log.d("bottom", "setupBottomNavigationView: setting up BottomNavigationView");

        bottomNavigationHelper help = new bottomNavigationHelper();
        help.bottomNavigationSetup(bottomNavigationView, Friendprofile.this);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void initImageLoader() {
        universalImageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupFirebaseAuth() {
        Log.d("FireBase", "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        currentUserID = mAuth.getCurrentUser().getUid();
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                Log.d(TAG, "onDataChange: " + dataSnapshot);


                UserSettings userSettings = firebaseMethods.getUserSettingsForUserID(dataSnapshot, userID);
                SetProfileWidgets(userSettings);
                //tempGridSetup();

                // relativeLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                //retrieve images for the user in question

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tempGridSetup() {
        ArrayList<String> imgURLs = new ArrayList<>();

        DatabaseReference postref = FirebaseDatabase.getInstance().getReference("user-posts/" + userID);
        postref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> UserPosts = new ArrayList<String>();
                Post post = new Post();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    post = ds.getValue(Post.class);
                    imgURLs.add(post.getImage());
                }
                setupImageGrid(imgURLs);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
        private void setupImageGrid (ArrayList < String > imgURLs) {
            GridView gridView = (GridView) findViewById(R.id.profileImagesGridView);

            GridImageAdapter adapter = new GridImageAdapter(getApplicationContext(), R.layout.layout_grid_imageview, "", imgURLs);
            gridView.setAdapter(adapter);
        }




    public void isFollow(String userID, String followUserID)
    {
        DatabaseReference checkUserRef = myRef.child("follow").child(userID);
        checkUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    if(ds.getValue().equals(followUserID)) {
                        isFollowAssign(true);
                        break;
                    }
                     else
                    {isFollowAssign(false);

                    }
                }
                checkFollow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void isFollowAssign(boolean status){
        followStatus = status;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    public void checkFollow(){
        if(followStatus == false){
            followButton.setText("Follow");
            followButton.setBackgroundColor(getColor(android.R.color.holo_blue_light));
        }
        else{
            followButton.setText("Unfollow");
            followButton.setBackgroundColor(getColor(android.R.color.holo_red_light));
        }
    }
}
