package com.example.insta_clone.Models;

import org.w3c.dom.Text;

public class Comment {
    String comment_id;
    Text comment;
    User user;

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public Text getComment() {
        return comment;
    }

    public void setComment(Text comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Comment(String comment_id, Text comment, User user) {
        this.comment_id = comment_id;
        this.comment = comment;
        this.user = user;
    }
}
