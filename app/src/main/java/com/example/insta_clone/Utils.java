package com.example.insta_clone;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.util.HashMap;
import java.util.Objects;

public class Utils {

    public static HashMap<String,String> postUser=new HashMap<>();
    public static void hideKeyboard(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isAcceptingText())
                inputManager.hideSoftInputFromWindow(
                        Objects.requireNonNull(((Activity) context).getCurrentFocus()).getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPostUserMap(String a,String b){
        postUser.put(a,b);
    }
}
