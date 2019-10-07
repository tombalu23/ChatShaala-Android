package com.example.insta_clone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.Models.Post;
import com.example.insta_clone.Models.UserAccountSettings;
import com.example.insta_clone.Models.UserSettings;
import com.example.insta_clone.Profile.Friendprofile;
import com.example.insta_clone.R;
import com.example.insta_clone.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.yalantis.ucrop.UCropFragment.TAG;

public class Users_listView_Adapter  extends BaseAdapter implements Filterable {


    private List<String> result;
    private Context context;
    UniversalImageLoader universalImageLoader;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;
    UserSettings userSettings;
    UserAccountSettings settings;
    String imageURL;
    public Users_listView_Adapter (Context context, List<String> users) {

        result = users;
        this.context = context;
    }


    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return result.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view ;

        view = inflater.inflate(R.layout.user_item_list_view, null);
        initImageLoader();
        setupFirebaseAuth();

        CircleImageView circleImageView = view.findViewById(R.id.photoUserImageView);
        TextView name = view.findViewById(R.id.nameUserTextView);
        firebaseMethods = new FirebaseMethods(view.getContext());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserSettings userSettings = new UserSettings();
                userSettings = firebaseMethods.getUserSettingsForUserID(dataSnapshot, result.get(position));
                universalImageLoader.setImage(userSettings.getUserAccountSettings().getProfile_photo(),circleImageView, null, "");
                name.setText(userSettings.getUser().getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

             name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Friendprofile.class);
                intent.putExtra("userID", result.get(position));
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return view;
    }

    private void initImageLoader(){
        universalImageLoader = new UniversalImageLoader(context);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

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

    @Override
    public Filter getFilter() {


        return null;
    }
}
