package com.example.insta_clone.Posts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.insta_clone.R;

public class NewPostActivity extends AppCompatActivity {

    ImageView imageView;
    EditText title_text, description_text;
    Button  post_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        imageView = findViewById(R.id.imageView);
        title_text = findViewById(R.id.titleEditText);
        description_text = findViewById(R.id.descriptionEditText);
        post_btn = findViewById(R.id.Postbtn);


    }

    @Override
    protected void onStart() {
        super.onStart();


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

                if(null!=imageView.getDrawable())
                {
                    Toast.makeText(NewPostActivity.this,"Please Select an Image to Post",Toast.LENGTH_LONG).show();
                }


            }
        });



    }

    public boolean checkIfEmpty(EditText edt) {
        String str = edt.getText().toString().trim();
        return TextUtils.isEmpty(str) || str.length() == 0;
    }
}
