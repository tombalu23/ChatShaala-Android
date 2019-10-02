package com.example.insta_clone.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insta_clone.FirebaseMethods;
import com.example.insta_clone.Home.HomeActivity;
import com.example.insta_clone.Models.Post;
import com.example.insta_clone.Models.UserAccountSettings;
import com.example.insta_clone.Models.UserSettings;
import com.example.insta_clone.Profile.Friendprofile;
import com.example.insta_clone.R;
import com.example.insta_clone.UniversalImageLoader;
import com.example.insta_clone.bottomNavigationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.internal.ListenerClass;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.yalantis.ucrop.UCropFragment.TAG;

public class Posts_listView_Adapter extends BaseAdapter {


    private List<Post> result;
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
    public Posts_listView_Adapter(Context context, List<Post> post) {

        result = post;
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

        view = inflater.inflate(R.layout.cardview_post, null);
        initImageLoader();
        setupFirebaseAuth();
        TextView title = (TextView) view.findViewById(R.id.titleTextView);
        TextView description = (TextView)view.findViewById(R.id.detailsTextView);
        TextView upvotes = (TextView)view.findViewById(R.id.upvote_CounterTextView);
        TextView downvotes = (TextView)view.findViewById(R.id.Downvote_CounterTextView);
        TextView comment = (TextView)view.findViewById(R.id.commentsCountTextView);
        ImageView postImageView  = (ImageView)view.findViewById(R.id.postImageView);
        CircleImageView authorimageview = (CircleImageView)view.findViewById(R.id.authorImageView);
        firebaseMethods = new FirebaseMethods(context);
        upvotes.setText(String.valueOf(result.get(position).getUpvote()));
        downvotes.setText(String.valueOf(result.get(position).getDownvote()));
        comment.setText(String.valueOf(result.get(position).getComment_count()));
        title.setText(result.get(position).getTitle());
        description.setText(result.get(position).getDescription());

        DatabaseReference firebaserefernce = myRef.child("user-account-settings").child(result.get(position).getUser());
        firebaserefernce.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageURL = firebaseMethods.return_photo(dataSnapshot);
                universalImageLoader.setImage(imageURL,authorimageview, null, "");
                universalImageLoader.setImage(result.get(position).getImage(), postImageView, null, "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        authorimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Friendprofile.class);
                intent.putExtra("userID", result.get(position).getUser());
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



}}