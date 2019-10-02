package com.example.insta_clone.Profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.Follow.FollowerList;
import com.example.insta_clone.Follow.FollowingList;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = "ProfileFragment";

    private static final int ACTIVITY_NUM = 3 ;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;


    private TextView mPosts, mFollowers, mFollowing, mDisplayName, mUsername, mWebsite, mDescription, mbtn_editprofile;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private android.support.v7.widget.Toolbar toolbar;
    private ImageView profileMenu;
    private BottomNavigationView bottomNavigationView;
    private FirebaseMethods firebaseMethods;
    private Context mContext;
    private RelativeLayout relativeLayout;
    int number_grid_columns = 3;
    ImageLoader mImageLoader;
    UniversalImageLoader universalImageLoader;
    View view;
    String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        mDisplayName = (TextView) view.findViewById(R.id.display_name);
        mUsername = (TextView) view.findViewById(R.id.profileName);
        mDescription = (TextView) view.findViewById(R.id.descriptionText);
        mProfilePhoto = (CircleImageView) view.findViewById(R.id.profile_image);
        mPosts = (TextView) view.findViewById(R.id.tvPosts);
        mFollowers = (TextView) view.findViewById(R.id.tvFollowers);
        mFollowing = (TextView) view.findViewById(R.id.tvFollowing);
        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = (GridView) view.findViewById(R.id.profileImagesGridView);
        toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.profileToolBar);
        profileMenu = (ImageView) view.findViewById(R.id.profileMenu);
        bottomNavigationView = (BottomNavigationView) view.findViewById(R.id.bottomNavViewBar);
        mbtn_editprofile = (TextView)view.findViewById(R.id.btn_EditProfile);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.relLayoutProfileCenter);
        mContext = getActivity();
        initImageLoader();
        firebaseMethods = new FirebaseMethods(mContext);
        Log.d(TAG, "onCreateView: stared.");


        setupBottomNavigationView();
        setupToolbar();
        relativeLayout.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        setupFirebaseAuth();

        mFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), FollowingList.class);
                intent.putExtra("userID", userID);
                getActivity().startActivity(intent);

            }
        });

        mFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity().getApplicationContext(), FollowerList.class);
                intent.putExtra("userID", userID);
                getActivity().startActivity(intent);

            }
        });

        return view;
    }


    /**
     * Responsible for setting up the profile toolbar
     */
    private void setupToolbar(){

        ((ProfileActivity)getActivity()).setSupportActionBar(toolbar);

        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to account settings.");
                Intent intent = new Intent(mContext, account_settings.class);
                startActivity(intent);
            }
        });
    }

    private void GotoEditPage(){
        Log.d("profile error", "init: inflating " + "Edit Profile");

        EditProfileFragment fragment = new EditProfileFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack("Edit Profile");
        transaction.commit();
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");

        bottomNavigationHelper help = new bottomNavigationHelper();
        help.bottomNavigationSetup(bottomNavigationView, getContext());
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void initImageLoader(){
        universalImageLoader = new UniversalImageLoader(getContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

      /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    /**
     * Setup the firebase auth object
     */

    public void SetProfileWidgets(UserSettings userSettings){

        Log.d(TAG, "SetProfileWidgets: setting profile widgets" + userSettings);
        universalImageLoader.setImage(userSettings.getUserAccountSettings().getProfile_photo(), mProfilePhoto,null,"");
        mDisplayName.setText(userSettings.getUserAccountSettings().getDisplay_name());
        mDescription.setText(userSettings.getUserAccountSettings().getCollege_name());
        mFollowers.setText(String.valueOf(userSettings.getUserAccountSettings().getFollowers()));
        mFollowing.setText(String.valueOf(userSettings.getUserAccountSettings().getFollowing()));
        mPosts.setText(String.valueOf(userSettings.getUserAccountSettings().getPosts()));
        mUsername.setText(userSettings.getUser().getUsername());
        tempGridSetup();

    }
    private void setupFirebaseAuth(){
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
        mbtn_editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GotoEditPage();
                          }
        });


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                Log.d(TAG, "onDataChange: "+ dataSnapshot);



                UserSettings userSettings = firebaseMethods.getUserSettings(dataSnapshot);
                SetProfileWidgets(userSettings);
                //tempGridSetup();

                relativeLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                //retrieve images for the user in question

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
  private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();

        DatabaseReference postref = FirebaseDatabase.getInstance().getReference("user-posts/"+userID);
        postref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> UserPosts= new ArrayList<String>();
                Post post = new Post();
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
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

    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView) view.findViewById(R.id.profileImagesGridView);

        GridImageAdapter adapter = new GridImageAdapter(view.getContext(), R.layout.layout_grid_imageview , "", imgURLs);
        gridView.setAdapter(adapter);
    }



  }



