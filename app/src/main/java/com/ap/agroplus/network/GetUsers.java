package com.ap.agroplus.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.Callbacks.UserCallback;
import com.ap.agroplus.General;
import com.ap.agroplus.VolleySingleton;
import com.ap.agroplus.information.Search;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sanniAdewale on 15/04/2017.
 */

public class GetUsers {

    Context context;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    UserCallback userCallback;

    public GetUsers(Context context, UserCallback userCallback) {
        this.context = context;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.userCallback = userCallback;
    }


    public void CheckUser(String username, final AVLoadingIndicatorView loading) {

        //String url = AppConfig.WEB_URL + "CheckUsername.php?username=" + "%" + username + "%";
        String url = AppConfig.WEB_URL + "CheckUsername.php?username=" + username;
        final ArrayList<Search> customData = new ArrayList<>();

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contentEquals("null")) {
                    loading.smoothToHide();
                    Toast.makeText(context, "No user found", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String getUsername = object.getString("username");
                        String email = object.getString("email");
                        String mobile = object.getString("mobile");
                        String imagePath = object.getString("image_path");
                        Search search = new Search(getUsername, email, mobile, imagePath);
                        customData.add(search);
                    }
                    if (userCallback != null) {
                        userCallback.onUserSearch(customData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.smoothToHide();
                Log.e("error_user", error.toString());
                Toast.makeText(context, "An error occurred. Check your connectivity.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
