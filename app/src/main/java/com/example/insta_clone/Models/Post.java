package com.example.insta_clone.Models;

public class Post {
    String postid;
    String image;
    String user;
    String description;
    String title;
    long timestamp;
    int upvote;
    int downvote;
    int comment_count;

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public Post() {
    }

    public Post(String postid, String image, String user, String description, String title, long timestamp, int upvote, int downvote, int comment_count) {
        this.postid = postid;
        this.image = image;
        this.user = user;
        this.description = description;
        this.title = title;
        this.timestamp = timestamp;
        this.upvote = upvote;
        this.downvote = downvote;
        this.comment_count = comment_count;
    }
}
