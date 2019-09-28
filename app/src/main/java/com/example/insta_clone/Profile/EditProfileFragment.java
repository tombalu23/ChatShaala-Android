package com.example.insta_clone.Profile;
import com.example.insta_clone.ImageUpload.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.StringSearch;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.content.ContextCompat;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.ImageUpload.GlideApp;
import com.example.insta_clone.ImageUpload.ImagePickerActivity;
import com.example.insta_clone.MainActivity;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import butterknife.OnClick;
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
    private View view;
    Uri uri;
    public static final int REQUEST_IMAGE = 100;

    private FirebaseMethods firebaseMethods;
    private StorageReference mStorageRef;

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
        view=  inflater.inflate(R.layout.fragment_edit_profile, container, false);
        mProfilePhoto = (ImageView) view.findViewById(R.id.profile_photo);
        mCollege = (EditText)view.findViewById(R.id.edit_college);
        mUsername = (EditText)view.findViewById(R.id.edit_username);
        mDisplayname =(EditText)view.findViewById(R.id.edit_display_name);
        mPhoneno = (EditText)view.findViewById(R.id.edit_phoneNumber);
        mMail = (EditText)view.findViewById(R.id.edit_email);
        firebaseMethods = new FirebaseMethods(getContext());
        Change_imageview = (Button)view.findViewById(R.id.changeProfilePhoto);
        initImageLoader();

        setProfileImage();
        setupFirebaseAuth();

        Change_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Image Button Clicked", Toast.LENGTH_SHORT).show();
                onProfileImageClick();
            }
        });

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
        String Sphoneno_text = mPhoneno.getText().toString();
        User user =new User();

        //check if inputs are valid
        if(checkInputs(Susername, Sdisplayname, Scollege, Sphoneno_text,Scollege))
        {
            Long Sphoneno = Long.parseLong(Sphoneno_text);



            //changing Username If Not Exists and Changing the User Name in the Database
            CheckIfUsernameExists(Susername);
            firebaseMethods.UpdateCollegeName(Scollege);
            firebaseMethods.UpdateDisplayName(Sdisplayname);
            Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            if(uri!=null)
            firebaseMethods.UploadProfilePhoto(uri);

        }



    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
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
    //check if inputs are valid
    private boolean checkInputs(String email, String username, String college_name, String mobile_no_text,String display_name){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(email.trim().equals("") || username.trim().equals("") ||college_name.trim().equals("")|| mobile_no_text.trim().equals("") || display_name.trim().equals("")){
            Toast.makeText(getActivity() , "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        GlideApp.with(this).load(url)
                .into(mProfilePhoto);
        mProfilePhoto.setColorFilter(ContextCompat.getColor(getActivity(), android.R.color.transparent));
    }






    private void onProfileImageClick() {
        Toast.makeText(getActivity(),"Photo Clicked", Toast.LENGTH_SHORT).show();
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



    private void launchCameraIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), uri);

                    // loading profile image from local cache
                    loadProfile(uri.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", view.getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }






}
