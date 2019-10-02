package com.example.insta_clone;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.insta_clone.Home.HomeActivity;
import com.example.insta_clone.Posts.NewPostActivity;
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
                            intent0.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent0);
                            break;

                        case R.id.ic_addBox:
                            Intent intent1 = new Intent(context, NewPostActivity.class);//ACTIVITY_NUM = 1
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                            break;


                        case R.id.ic_search:
                            Intent intent2 = new Intent(context, Search.class);//ACTIVITY_NUM = 2
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent2);
                            break;

                        case R.id.ic_person:
                            Intent intent3  = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 3
                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent3);
                            break;
                    }
                    return true;
                }
            });
}
}
