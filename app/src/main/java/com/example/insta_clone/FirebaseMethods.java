package com.example.insta_clone;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.insta_clone.Models.Post;
import com.example.insta_clone.Models.User;
import com.example.insta_clone.Models.UserAccountSettings;
import com.example.insta_clone.Models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * NOTE by Balaji 21-08-2019
 * Helper class for all Firebase Methods
 */



public class FirebaseMethods {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    DatabaseReference userRef;
    private StorageReference mStorageRef;
    private String imageURL = "Yes";
    private static final String TAG = "FirebaseMethods";

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    private Context mContext;
    private String image;


    public FirebaseMethods(Context context) {
        mAuth = FirebaseAuth.getInstance();
        mContext = context;
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();

        userRef= mFirebaseDatabase.getReference("users");
        mStorageRef = FirebaseStorage.getInstance().getReference();


        if(mAuth.getCurrentUser() != null){
            //Gets Uid (from Firebase authentication, not from database)
            userID = mAuth.getCurrentUser().getUid();
        }
    }



    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot){
        Log.d(TAG, "checkIfUsernameExists: checking if " + username + " already exists.");

        User user = new User();

        for (DataSnapshot ds: datasnapshot.getChildren()){
            Log.d(TAG, "checkIfUsernameExists: datasnapshot: " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "checkIfUsernameExists: username: " + user.getUsername());

            if(user.getUsername().equals(username)){
                Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + user.getUsername());
                return true;
            }
        }
        return false;
    }



    /**
     * Register a new email and password with Firebase Authentication
     */
    public void registerNewEmail(final String email, final String password, final String username, final String college_name, final String profile_photo, final long mobile_no, final String display_name){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(mContext, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();

                        }
                        else if(task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Toast.makeText(mContext, R.string.auth_success, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Authstate changed: " + userID);
                            addNewUser(email, username, college_name, display_name, profile_photo, mobile_no);

                        }

                    }
                });
    }

    public void addNewUser(String email, String username, String college_name, String display_name ,String profile_photo, long mobile_no){

        User user = new User( userID, email, username, mobile_no);
        UserAccountSettings userAccountSettings= new UserAccountSettings(college_name,display_name,0,0,0,profile_photo,username);
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);
        myRef.child("user-account-settings")
                .child(userID)
                .setValue(userAccountSettings);
        Log.d(TAG, "addNewUser: added new user");
        Toast.makeText(mContext, "Signup successful. Sending verification email.", Toast.LENGTH_SHORT).show();

    }

    public void UpdateUserName(String username){

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.dbfield_username))
                .setValue(username);
        myRef.child("user-account-settings")
                .child(userID)
                .child("username")
                .setValue(username);

    }

    public void UpdateDisplayName(String displayname){

        myRef.child("user-account-settings")
                .child(userID)
                .child("display_name")
                .setValue(displayname);


    }
    public void UpdateCollegeName(String collegename){

        myRef.child("user-account-settings")
                .child(userID)
                .child("college_name")
                .setValue(collegename);


    }

    public void UpdateProfilePhoto(String string){
        if(string == null)
            return;
        myRef.child("user-account-settings")
                    .child(userID)
                    .child("profile_photo")
                    .setValue(string);



    }

    public void UploadProfilePhoto(Uri uri){

     StorageReference riversRef = mStorageRef.child("images/users/"+userID+".jpg");
       /* riversRef.putFile(uri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(mContext, "Your Picture Saved Successfully",Toast.LENGTH_SHORT).show();
                            String Downloadurl = task.getResult().getStorage().getDownloadUrl().toString();
                            while(!Downloadurl.is)
                            UpdateProfilePhoto(Downloadurl);
                        }
                    }

                });*/

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                       Task<Uri> UriDownload = taskSnapshot.getStorage().getDownloadUrl();
                       while(!UriDownload.isComplete());
                       Uri url = UriDownload.getResult();
                       String urlstring = url.toString();
                       UpdateProfilePhoto(urlstring);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });


    }

    public void UploadPostPhoto(Uri uri, String postID, Post post){
        StorageReference riversRef = mStorageRef.child("images/posts/"+userID+"/"+postID+".jpg");
        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Task<Uri> UriDownload = taskSnapshot.getStorage().getDownloadUrl();
                        while(!UriDownload.isComplete());
                        Uri url = UriDownload.getResult();
                        String urlstring = url.toString();
                        UpdatePostPhoto(urlstring,post );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public void UpdatePostPhoto(String string, Post post){
        myRef.child("posts")
                .child(post.getPostid())
                .child("image")
                .setValue(string);
        myRef.child("user-posts")
                .child(userID)
                .child(post.getPostid())
                .child("image")
                .setValue(string);

    }




    public UserSettings getUserSettings(DataSnapshot dataSnapshot){

        Log.d(TAG, "getUserSettings: retrieving user settigns from firebase..");
        User user = new User();
        UserAccountSettings settings = new UserAccountSettings();
        for(DataSnapshot ds: dataSnapshot.getChildren()) {
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserSettings: Datasnapshot" + ds);

                try {

                    settings.setDisplay_name(
                            ds.child(userID).getValue(UserAccountSettings.class).getDisplay_name()
                    );

                    settings.setCollege_name(
                            ds.child(userID).getValue(UserAccountSettings.class).getCollege_name()
                    );

                    settings.setFollowers(
                            ds.child(userID).getValue(UserAccountSettings.class).getFollowers()
                    );

                    settings.setFollowing(
                            ds.child(userID).getValue(UserAccountSettings.class).getFollowing()
                    );

                    settings.setPosts(
                            ds.child(userID).getValue(UserAccountSettings.class).getPosts()
                    );

                    settings.setProfile_photo(
                            ds.child(userID).getValue(UserAccountSettings.class).getProfile_photo()
                    );

                    settings.setUsername(
                            ds.child(userID).getValue(UserAccountSettings.class).getUsername()
                    );

                } catch (Exception e) {
                    Log.d(TAG, "getUserSettings: SettingsException " + e.getMessage());
                }
            }

            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUser: Datasnapshot" + ds);
                try {

                    user.setEmail(
                            ds.child(userID).getValue(User.class).getEmail()
                    );

                    user.setMobile_no(
                            ds.child(userID).getValue(User.class).getMobile_no()
                    );

                    user.setUser_id(
                            ds.child(userID).getValue(User.class).getUser_id()
                    );

                    user.setUsername(
                            ds.child(userID).getValue(User.class).getUsername()
                    );
                } catch (Exception e) {
                    Log.d(TAG, "getUserSettings: UserException" + e.getMessage());
                }


            }
        }

        return new UserSettings(user , settings);
    }
    public String getPhotoForUser(String user)
    {
    DatabaseReference imageref = myRef.child(mContext.getString(R.string.dbname_user_account_settings)).child(user).child("profile_photo");
     imageref.addListenerForSingleValueEvent(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            image = (String) dataSnapshot.getValue();

         }

         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {

         }
     });

      return  image;
    }


    public UserSettings getUserSettingsForUserID(DataSnapshot dataSnapshot, String UserID){

        Log.d(TAG, "getUserSettings: retrieving user settigns from firebase..");
        User user = new User();
        UserAccountSettings settings = new UserAccountSettings();


        for(DataSnapshot ds: dataSnapshot.getChildren()) {
            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserSettings: Datasnapshot" + ds);

                try {

                    settings.setDisplay_name(
                            ds.child(UserID).getValue(UserAccountSettings.class).getDisplay_name()
                    );

                    settings.setCollege_name(
                            ds.child(UserID).getValue(UserAccountSettings.class).getCollege_name()
                    );

                    settings.setFollowers(
                            ds.child(UserID).getValue(UserAccountSettings.class).getFollowers()
                    );

                    settings.setFollowing(
                            ds.child(UserID).getValue(UserAccountSettings.class).getFollowing()
                    );

                    settings.setPosts(
                            ds.child(UserID).getValue(UserAccountSettings.class).getPosts()
                    );

                    settings.setProfile_photo(
                            ds.child(UserID).getValue(UserAccountSettings.class).getProfile_photo()
                    );

                    settings.setUsername(
                            ds.child(UserID).getValue(UserAccountSettings.class).getUsername()
                    );

                } catch (Exception e) {
                    Log.d(TAG, "getUserSettings: SettingsException " + e.getMessage());
                }
            }

            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUser: Datasnapshot" + ds);
                try {

                    user.setEmail(
                            ds.child(UserID).getValue(User.class).getEmail()
                    );

                    user.setMobile_no(
                            ds.child(UserID).getValue(User.class).getMobile_no()
                    );

                    user.setUser_id(
                            ds.child(UserID).getValue(User.class).getUser_id()
                    );

                    user.setUsername(
                            ds.child(UserID).getValue(User.class).getUsername()
                    );
                } catch (Exception e) {
                    Log.d(TAG, "getUserSettings: UserException" + e.getMessage());
                }


            }
        }

        return new UserSettings(user , settings);
    }


    public void UploadPost(Post post)
    {

        myRef.child("posts")
                .child(post.getPostid())
                .setValue(post);
        myRef.child("user-posts")
                .child(userID)
                .child(post.getPostid())
                .setValue(post);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserSettings userSettings = getUserSettings(dataSnapshot);
                inc_count(userSettings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void inc_count(UserSettings userSettings){

        myRef.child("user-account-settings")
                .child(userID)
                .child("posts")
                .setValue(userSettings.getUserAccountSettings().getPosts()+1);
    }

    public String return_photo(DataSnapshot dataSnapshot){

        UserAccountSettings userAccountSettings = dataSnapshot.getValue(UserAccountSettings.class);
        imageURL = userAccountSettings.getProfile_photo();

        return imageURL;
    }

    public void followUser (String followUserID){
        myRef.child("follow").child(userID).child(followUserID).setValue("true");


        myRef.child("following").child(followUserID).child(userID).setValue("true");

        DatabaseReference followUserRef = myRef.child("user-account-settings").child(followUserID);
        DatabaseReference userRef = myRef.child("user-account-settings").child(userID);

        UserAccountSettings followUserSettings = new UserAccountSettings();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings userSettings = new UserAccountSettings();
                userSettings=dataSnapshot.getValue(UserAccountSettings.class);
                userRef.child("following").setValue(userSettings.getFollowing()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        followUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings userSettings = new UserAccountSettings();
                userSettings=dataSnapshot.getValue(UserAccountSettings.class);
                followUserRef.child("followers").setValue(userSettings.getFollowers()+1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }





    public void unfollowUser (String followUserID){

        myRef.child("follow").child(userID).child(followUserID).removeValue();

        myRef.child("following").child(followUserID).child(userID).removeValue();

        DatabaseReference followUserRef = myRef.child("user-account-settings").child(followUserID);
        DatabaseReference userRef = myRef.child("user-account-settings").child(userID);

        UserAccountSettings followUserSettings = new UserAccountSettings();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings userSettings = new UserAccountSettings();
                userSettings=dataSnapshot.getValue(UserAccountSettings.class);
                userRef.child("following").setValue(userSettings.getFollowing()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        followUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccountSettings userSettings = new UserAccountSettings();
                userSettings=dataSnapshot.getValue(UserAccountSettings.class);
                followUserRef.child("followers").setValue(userSettings.getFollowers()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }







}
