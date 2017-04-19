package com.ap.agroplus.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.ap.agroplus.information.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class AppData {

    Context context;
    SharedPreferences pref;

    public AppData(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("users", Context.MODE_PRIVATE);
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("id", user.id);
        editor.putString("username", user.username);
        editor.putString("email", user.email);
        editor.putString("mobile", user.mobile);
        editor.putString("password", user.password);
        editor.putString("image_path", user.image_path);
        editor.putString("local_path", user.local_path);
        editor.apply();
    }

    public User getUser() {
        int id = pref.getInt("id", 0);
        String username = pref.getString("username", "");
        String email = pref.getString("email", "");
        String mobile = pref.getString("mobile", "");
        String password = pref.getString("password", "");
        String image_path = pref.getString("image_path", "");
        String local_path = pref.getString("local_path", "");
        User user = new User(id, username, email, mobile, password, image_path, local_path);
        return user;
    }

    public void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("loggedIn", loggedIn);
        editor.apply();
    }

    public boolean getLoggedIn() {
        return pref.getBoolean("loggedIn", false);
    }

    public void setSearchResults(Set<String> results) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet("search_results", results);
        editor.apply();
    }

    public Set<String> getSearchResults() {
        return pref.getStringSet("search_results", new HashSet<String>());
    }

    public void setHomeResults(Set<String> results) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putStringSet("home_results", results);
        editor.apply();
    }

    public Set<String> getHomeResults() {
        return pref.getStringSet("home_results", new HashSet<String>());
    }

    public void LogOut() {
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }
}
