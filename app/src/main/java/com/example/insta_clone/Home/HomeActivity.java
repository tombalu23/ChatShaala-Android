package com.example.insta_clone.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.insta_clone.Login_SignUp.LoginActivity;
import com.example.insta_clone.Profile.account_settings;
import com.example.insta_clone.R;
import com.example.insta_clone.bottomNavigationHelper;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);

 /*       bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ic_addBox:
                                Toast.makeText(HomeActivity.this, "add post", Toast.LENGTH_LONG ).show();
                                break;
                            case R.id.ic_person:
                                break;
                            case R.id.ic_house:
                                break;
                            case R.id.ic_search:
                                break;
                        }
                        return true;
                    }
                });
        */
        setupBottomNav();
        setupViewPager();



    }

    //Adding 3 tabs Camera, Messages and Home
    private void setupViewPager() {
        SectionsPagerAdapter sectionsPagerAdapter= new SectionsPagerAdapter(getSupportFragmentManager());

        sectionsPagerAdapter.addFragment(new CameraFragment());  //Fragment 0
        sectionsPagerAdapter.addFragment(new HomeFragment()); //Fragment1
        sectionsPagerAdapter.addFragment(new MessagesFragment()); //Fragment2

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout= findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_house);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_paperplane);

        //Selects Home tab by default
        tabLayout.getTabAt(1).select();




    }

    private void setupBottomNav() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavViewBar);
        bottomNavigationHelper help = new bottomNavigationHelper();
        help.bottomNavigationSetup(bottomNavigationView, getApplicationContext());
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.logout) {
            getSharedPreferences("My_Preferences", Context.MODE_PRIVATE).edit().putBoolean("loggedin",false).apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }


}
