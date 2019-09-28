package com.example.insta_clone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.insta_clone.Models.Comment;
import com.example.insta_clone.Models.Post;
import com.example.insta_clone.R;

import java.util.List;

public class Comments_listView_Adapter extends BaseAdapter{


    private List<Comment> result;
    private Context context;

    public Comments_listView_Adapter(Context context, List<Comment> comments) {

        result = comments;
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

        View view;

        view = inflater.inflate(R.layout.cardview_post, null);

        TextView comment_text = (TextView) view.findViewById(R.id.titleTextView);
        TextView user_id = (TextView) view.findViewById(R.id.detailsTextView);

        comment_text.setText(String.valueOf(result.get(position).getComment()));
        user_id.setText(String.valueOf(result.get(position).getUser()));


        return view;
    }

}