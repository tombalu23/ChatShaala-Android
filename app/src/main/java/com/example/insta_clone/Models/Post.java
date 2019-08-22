package com.example.insta_clone.Models;

import java.net.URI;

public class Post {
    String postid;
    URI image;
    User user;
    String Description;
    int Upvote;
    int Downvote;
    Comment[] comments;

    public Post(String postid, URI image, User user, String description, int upvote, int downvote, Comment[] comments) {
        this.postid = postid;
        this.image = image;
        this.user = user;
        Description = description;
        Upvote = upvote;
        Downvote = downvote;
        this.comments = comments;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getUpvote() {
        return Upvote;
    }

    public void setUpvote(int upvote) {
        Upvote = upvote;
    }

    public int getDownvote() {
        return Downvote;
    }

    public void setDownvote(int downvote) {
        Downvote = downvote;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }
}
