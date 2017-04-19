package com.ap.agroplus.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.VolleySingleton;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.User;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by sanniAdewale on 14/04/2017.
 */

public class UpdateUserImage {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String local_path, image_path;
    AppData data;
    BootstrapCircleThumbnail iv;
    ProgressBar progressBar;
    User user;

    public UpdateUserImage(Context context, String local_path, String image_path, BootstrapCircleThumbnail iv, ProgressBar progressBar) {
        this.context = context;
        this.local_path = local_path;
        this.image_path = image_path;
        this.iv = iv;
        this.progressBar = progressBar;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        data = new AppData(context);
        user = data.getUser();
    }

    public void UpdateImage() {
        String url = AppConfig.WEB_URL + "UpdateImage.php?email=" + user.email + "&local_path=" + local_path + "&image_path=" + image_path;
        //String _url = url.replace(" ", "%20");
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int success;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt("success");
                    if (success == 1) {
                        Toast.makeText(context, "Profile Image updated", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        User new_user = new User(user.id, user.username, user.email, user.mobile, user.password, image_path, local_path);
                        data.setUser(new_user);
                        User myUser = data.getUser();
                        File file = new File(myUser.local_path);
                        if (file.exists()) {
                            iv.setImageDrawable(BitmapDrawable.createFromPath(myUser.local_path));
                        } else {
                            String url = myUser.image_path;
                            Glide.with(context).load(url.replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "An error occurred. Check your internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "An error occurred. Check your internet connectivity", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
