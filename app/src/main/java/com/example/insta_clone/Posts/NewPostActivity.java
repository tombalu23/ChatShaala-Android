package com.example.insta_clone.Posts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.Home.HomeActivity;
import com.example.insta_clone.ImageUpload.GlideApp;
import com.example.insta_clone.ImageUpload.ImagePickerActivity;
import com.example.insta_clone.Models.Post;
import com.example.insta_clone.R;
import com.example.insta_clone.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class NewPostActivity extends AppCompatActivity {

    ImageView imageView;
    EditText title_text, description_text;
    Button  post_btn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String userID;
    FirebaseMethods firebaseMethods;
    Date date;
    Timestamp timestamp;
    LocalDateTime localDateTime;
    Calendar calendar;
    java.sql.Timestamp currentTimestamp;
    public static final int REQUEST_IMAGE = 100;
    private Uri uri;
    UniversalImageLoader universalImageLoader;
    Post post;
    long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        imageView = findViewById(R.id.imageView);
        title_text = findViewById(R.id.titleEditText);
        description_text = findViewById(R.id.descriptionEditText);
        post_btn = findViewById(R.id.Postbtn);
        firebaseMethods = new FirebaseMethods(getApplicationContext());
        post =new Post();
        date = new Date();
        now = System.currentTimeMillis();


    }


    @Override
    protected void onStart() {
        super.onStart();
        initImageLoader();
        loadProfileDefault();
        setupFirebaseAuth();


        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfEmpty(title_text)){
                    title_text.setError("Give Some Title For your Post");
                    return;
                }
                if (checkIfEmpty(description_text)){
                    description_text.setError("Give Some Title For your Post");
                    return;
                }
                String desription_string = description_text.getText().toString();
                post.setDescription(desription_string);
                post.setTitle(title_text.getText().toString());
                post.setPostid(UUID.randomUUID().toString());
                post.setDownvote(0);
                post.setUpvote(0);
                post.setComment_count(0);
                post.setUser(userID);
                post.setTimestamp(now);
                if(uri == null)
                {Toast.makeText(getApplicationContext(), "Pls Upload A Image",Toast.LENGTH_SHORT).show();
                return;}


                firebaseMethods.UploadPostPhoto(uri, post.getPostid(), post);
                firebaseMethods.UploadPost(post);
                startActivity(new Intent(NewPostActivity.this, HomeActivity.class));
                Toast.makeText(getApplicationContext(), "Upload Successful",Toast.LENGTH_SHORT).show();


            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProfileImageClick();
            }
        });


    }
    private void initImageLoader(){
        universalImageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void onProfileImageClick() {
        Toast.makeText(NewPostActivity.this,"Photo Clicked", Toast.LENGTH_SHORT).show();
        Dexter.withActivity(NewPostActivity.this)
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

    public boolean checkIfEmpty(EditText edt) {
        String str = edt.getText().toString().trim();
        return TextUtils.isEmpty(str) || str.length() == 0;
    }


    private void launchCameraIntent() {
        Intent intent = new Intent(NewPostActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 201); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 120);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void launchGalleryIntent() {
        Intent intent = new Intent(NewPostActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 2); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(NewPostActivity.this, new ImagePickerActivity.PickerOptionListener() {
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
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
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        universalImageLoader.setImage(url, imageView,null,"");
      /*  GlideApp.with(this).load(url)
                .into(imageView);
        imageView.setColorFilter(ContextCompat.getColor(NewPostActivity.this, android.R.color.transparent));*/
    }

    private void loadProfileDefault() {
        GlideApp.with(this).load(R.drawable.ic_stub)
                .into(imageView);

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




    }



}
