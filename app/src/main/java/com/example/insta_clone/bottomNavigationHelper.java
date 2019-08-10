package com.example.insta_clone;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.insta_clone.Home.HomeActivity;
import com.example.insta_clone.Profile.ProfileActivity;

public class bottomNavigationHelper extends AppCompatActivity {

 public static void bottomNavigationSetup(BottomNavigationView bottomNavigationView, final Context context) {

    bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.ic_house:
                            Intent intent0  = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                            context.startActivity(intent0);
                            break;

                        case R.id.ic_addBox:
                            Toast.makeText( context, "add post", Toast.LENGTH_LONG).show();

                            break;


                        case R.id.ic_search:
                            break;

                        case R.id.ic_person:
                            Intent intent3  = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 3
                            context.startActivity(intent3);
                            break;
                    }
                    return true;
                }
            });
}
}
