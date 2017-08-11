package com.example.android.dor.ManagerClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.android.dor.activity.HomeActivity;
import com.example.android.dor.activity.LoginActivity;
import com.example.android.dor.activity.MainActivity;

import java.util.HashMap;

/**
 * Created by darip on 17-06-2017.
 */

public class SessionManager{
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "DorPref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_USERID = "userId";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_AGE = "age";
    public static final String KEY_IMAGE = "image";


    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(int id, String name, String username, String email, int age, String phone, String img){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
//        editor.putString(KEY_PASSWORD, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);
        editor.putInt(KEY_USERID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_AGE, age);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_IMAGE, img);

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){

        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
            HomeActivity.activity.finish();
        }
    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
//        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_USERID, (pref.getInt(KEY_USERID, 0)+""));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
        user.put(KEY_PHONE, pref.getString(KEY_PHONE, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_AGE, (pref.getInt(KEY_AGE, 0)+""));
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));

        // return user
        return user;
    }

    public void putUserDetails(int id, String name, String username, int age, String phone){
        editor.putInt(KEY_USERID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putInt(KEY_AGE, age);
        editor.putString(KEY_PHONE, phone);
    }

    public void setImage(String img){
        editor.putString(KEY_IMAGE, img);
        editor.commit();
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void setName(String name){
        editor.putString(KEY_NAME, name);
        editor.commit();
    }

    public String getImg(){
        return pref.getString(KEY_IMAGE, null);
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
        HomeActivity.activity.finish();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
