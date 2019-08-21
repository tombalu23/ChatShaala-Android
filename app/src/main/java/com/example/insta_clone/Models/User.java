package com.example.insta_clone.Models;

public class User {

    public User(){

    }

    public User( String user_id, String email, String username, long mobile_no) {
        this.email = email;
        this.username = username;
        this.mobile_no = mobile_no;
        this.user_id = user_id;
    }

    private String email;
    private String username;
    private long mobile_no;
    private String user_id;





    public String getEmail() {
        return email;
    }

    public User(String email) {
        this.email = email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(long mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
