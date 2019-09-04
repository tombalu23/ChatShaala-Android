package com.example.insta_clone.Profile;

import android.content.Intent;
import android.nfc.Tag;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.insta_clone.GridImageAdapter;
import com.example.insta_clone.R;
import com.example.insta_clone.UniversalImageLoader;
import com.example.insta_clone.bottomNavigationHelper;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUM = 3 ;
    BottomNavigationView bottomNavigationView;
    ProgressBar profile_progress;
    TextView edit_profile_tv;
    ImageView mProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        mProfilePhoto=findViewById(R.id.profile_image);

        //Edit Profile Button
    /*    edit_profile_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_settings accountSettings=new account_settings();

                //FragmentNumber of EditProfileFragment is 0
            }
        });*/

        //setupBottomNav();
        //setupToolbar();

        //To load Profile Picture
        //initImageLoader();
        //setProfileImage();

        //To load Gridview Images
        //tempGridSetup();

        init();


    }


    private void init(){
        Log.d("profile error", "init: inflating " + getString(R.string.profile_fragment));

        profile fragment = new profile();
        FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }

  /*  private void setupBottomNav() {
        bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        bottomNavigationHelper help = new bottomNavigationHelper();
        help.bottomNavigationSetup(bottomNavigationView, getApplicationContext());
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile_settings:
                        Intent intent = new Intent(getApplicationContext(), account_settings.class);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }


 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.profile_settings) {

            Intent intent = new Intent(getApplicationContext(), account_settings.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
*/

  /*  private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getApplicationContext());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setProfileImage(){
        String imgURL = "https://images.immediate.co.uk/production/volatile/sites/3/2018/12/Sherlock-Phone-5818b69.jpg?quality=90&resize=620,413";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null,"");
    }



    //Temporarily loading GridView images
    //TODO Later pull images from Firebase
    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://pbs.twimg.com/profile_images/616076655547682816/6gMRtQyY.jpg");
        imgURLs.add("https://i.redd.it/9bf67ygj710z.jpg");
        imgURLs.add("https://c1.staticflickr.com/5/4276/34102458063_7be616b993_o.jpg");
        imgURLs.add("http://i.imgur.com/EwZRpvQ.jpg");
        imgURLs.add("http://i.imgur.com/JTb2pXP.jpg");
        imgURLs.add("https://i.redd.it/59kjlxxf720z.jpg");
        imgURLs.add("https://i.redd.it/pwduhknig00z.jpg");
        imgURLs.add("https://i.redd.it/clusqsm4oxzy.jpg");
        imgURLs.add("https://i.redd.it/svqvn7xs420z.jpg");
        imgURLs.add("http://i.imgur.com/j4AfH6P.jpg");
        imgURLs.add("https://i.redd.it/89cjkojkl10z.jpg");
        imgURLs.add("https://i.redd.it/aw7pv8jq4zzy.jpg");
        imgURLs.add("https://images.immediate.co.uk/production/volatile/sites/3/2018/12/Sherlock-Phone-5818b69.jpg?quality=90&resize=620,413");

        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(ArrayList<String> imgURLs){
        GridView gridView = (GridView) findViewById(R.id.imagesGridView);

        GridImageAdapter adapter = new GridImageAdapter(getApplicationContext(), R.layout.layout_grid_imageview , "", imgURLs);
        gridView.setAdapter(adapter);
    }


    private void setupActivityWidgets(){
        profile_progress = (ProgressBar) findViewById(R.id.profileProgressBar);
        profile_progress.setVisibility(View.GONE);
        mProfilePhoto = (ImageView) findViewById(R.id.profile_photo);
    }
*/

}
