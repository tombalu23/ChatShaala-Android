package com.example.insta_clone.Profile;

import android.content.Context;
import android.content.Intent;
import android.icu.text.StringSearch;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.Models.User;
import com.example.insta_clone.Models.UserSettings;
import com.example.insta_clone.R;
import com.example.insta_clone.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.yalantis.ucrop.UCropFragment.TAG;

/*
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageView mProfilePhoto, tick_edit, bcak_edit;
    private EditText mUsername,mDisplayname, mMail, mCollege, mPhoneno;
    private Button Change_imageview;

    //private OnFragmentInteractionListener mListener;
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    UniversalImageLoader universalImageLoader;
    private String userID;

    private FirebaseMethods firebaseMethods;
    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view=  inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
        mCollege = (EditText)view.findViewById(R.id.edit_college);
        mUsername = (EditText)view.findViewById(R.id.edit_username);
        mDisplayname =(EditText)view.findViewById(R.id.edit_display_name);
        mPhoneno = (EditText)view.findViewById(R.id.edit_phoneNumber);
        mMail = (EditText)view.findViewById(R.id.edit_email);
        firebaseMethods = new FirebaseMethods(getContext());
        initImageLoader();

        setProfileImage();
        setupFirebaseAuth();

        return view;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView back_arrow= (ImageView) getActivity().findViewById(R.id.backArrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "ButtonClicked",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        ImageView tick= (ImageView) getActivity().findViewById(R.id.saveChanges);
        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                SaveProfileSettings();
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });



    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setProfileImage(){
        String imgURL = "www.androidcentral.com/sites/androidcentral.com/files/styles/xlarge/public/article_images/2016/08/ac-lloyd.jpg?itok=bb72IeLf";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");
    }
    public void SetProfileWidgets(UserSettings userSettings){

        Log.d(TAG, "SetProfileWidgets: setting profile widgets" + userSettings);

        universalImageLoader.setImage(userSettings.getUserAccountSettings().getProfile_photo(), mProfilePhoto,null,"");
        mDisplayname.setText(userSettings.getUserAccountSettings().getDisplay_name());
        mUsername.setText(String.valueOf(userSettings.getUserAccountSettings().getUsername()));

        mCollege.setText(String.valueOf(userSettings.getUserAccountSettings().getCollege_name()));
        mPhoneno.setText(String.valueOf(userSettings.getUser().getMobile_no()));
        mMail.setText(String.valueOf(userSettings.getUser().getEmail()));

    }
    private void CheckIfUsernameExists(final String username){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child(getString(R.string.dbname_users)).orderByChild(getString(R.string.dbfield_username)).equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {// add username
                    firebaseMethods.UpdateUserName(username);
                    Toast.makeText(getActivity(), "Changed Username to "+username,Toast.LENGTH_LONG).show();

                }
                for(DataSnapshot singledatasnapshot: dataSnapshot.getChildren())
                {
                    if(singledatasnapshot.exists())
                    {
                        Log.d(TAG, "onDataChange: Username match found -"+ singledatasnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "Username Already Exists",Toast.LENGTH_LONG).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void SaveProfileSettings(){
        String Susername = mUsername.getText().toString();
        String Sdisplayname = mDisplayname.getText().toString();
        String Scollege = mCollege.getText().toString();
        String Smail = mMail.getText().toString();
        Long Sphoneno = Long.parseLong(mPhoneno.getText().toString());
        User user =new User();

        //changing Username If Not Exists
        

        CheckIfUsernameExists(Susername);



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





        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrieve user information from the database
                Log.d(TAG, "onDataChange: "+ dataSnapshot);
                UserSettings userSettings = firebaseMethods.getUserSettings(dataSnapshot);
                SetProfileWidgets(userSettings);

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






}
