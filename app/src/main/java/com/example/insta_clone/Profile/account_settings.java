package com.example.insta_clone.Profile;

import android.app.MediaRouteButton;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.insta_clone.Home.CameraFragment;
import com.example.insta_clone.Home.HomeFragment;
import com.example.insta_clone.Home.MessagesFragment;
import com.example.insta_clone.Home.SectionsPagerAdapter;
import com.example.insta_clone.R;
import com.example.insta_clone.SectionsStatePagerAdapter;
import com.example.insta_clone.bottomNavigationHelper;

import java.util.ArrayList;

public class account_settings extends AppCompatActivity {

    private static final int ACTIVITY_NUM =3 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        setupSettingsList();
        setupBottomNav();

        ImageView back_arrow= (ImageView)findViewById(R.id.backArrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

    }


    private void setupBottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavViewBar);
        bottomNavigationHelper help = new bottomNavigationHelper();
        help.bottomNavigationSetup(bottomNavigationView, getApplicationContext());
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }


/*
    private void setupViewPager(int Fragmentnumber) {
        SectionsStatePagerAdapter sectionsStatePagerAdapter= new SectionsStatePagerAdapter(getSupportFragmentManager());

        sectionsStatePagerAdapter.addFragment(new EditProfileFragment());  //Fragment 0
        sectionsStatePagerAdapter.addFragment(new sign_out_Fragment()); //Fragment1

        RelativeLayout mRelativeLayout = findViewById(R.id.relLayout1);
        mRelativeLayout.setVisibility(View.GONE);

        ViewPager viewPager= findViewById(R.id.container);
        viewPager.setAdapter(sectionsStatePagerAdapter);
    }

*/

    public void onFragmentSelect(int Fragmentnumber)
    {
        SectionsStatePagerAdapter sectionsStatePagerAdapter= new SectionsStatePagerAdapter(getSupportFragmentManager());
        ViewPager viewPager= findViewById(R.id.container);
        RelativeLayout mRelativeLayout = findViewById(R.id.relLayout1);
        mRelativeLayout.setVisibility(View.GONE);

        switch(Fragmentnumber) {
            case 0:
                sectionsStatePagerAdapter.addFragment(new EditProfileFragment());  //Fragment 0
                viewPager.setAdapter(sectionsStatePagerAdapter);
                break;

            case 1:
                sectionsStatePagerAdapter.addFragment(new sign_out_Fragment()); //Fragment1
                viewPager.setAdapter(sectionsStatePagerAdapter);
                break;


        }
    }

    private void setupSettingsList(){
        ListView options_lv= findViewById(R.id.lvAccountSettings);
        ArrayList<String> options=new ArrayList<String>();
        options.add(getString(R.string.edit_profile));
        options.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, options );
        options_lv.setAdapter(adapter);
        options_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onFragmentSelect(position);
            }
        });
    }


}
