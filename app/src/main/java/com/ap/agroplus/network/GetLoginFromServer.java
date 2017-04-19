package com.ap.agroplus.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ap.agroplus.Activity.Home2Activity;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.VolleySingleton;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.User;
import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sanniAdewale on 26/03/2017.
 */

public class GetLoginFromServer {

    public Context context;
    public VolleySingleton volleySingleton;
    public RequestQueue requestQueue;
    String username, password;
    General general;
    AppData data;

    public GetLoginFromServer(Context context, String username, String password) {
        this.context = context;
        this.username = username;
        this.password = password;
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        general = new General(context);
        data = new AppData(context);
    }

    public void LoginUser(final Activity activity) {

        String url = AppConfig.WEB_URL + "LoginUser.php?username=" + username;
        general.displayDialog("Checking user login details");

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        general.dismissDialog();
                        general.error("Incorrect username");
                        return;
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    int id = object.getInt("id");
                    String getUsername = object.getString("username");
                    String email = object.getString("email");
                    String mobile = object.getString("mobile");
                    String getPassword = object.getString("password");
                    String imagePath = object.getString("image_path");
                    String local_path = object.getString("local_path");

                    if (!getPassword.contentEquals(password)) {
                        general.dismissDialog();
                        general.error("Incorrect Password");
                    } else {
                        User user = new User(id, getUsername, email, mobile, getPassword, imagePath, local_path);
                        data.setUser(user);
                        data.setLoggedIn(true);
                        general.dismissDialog();
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, Home2Activity.class));
                        activity.finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                general.dismissDialog();
                general.error("An error occurred. Check your connectivity.");
            }
        });

        requestQueue.add(stringRequest);
    }

    public void LoginAfterReg(final BootstrapButton button, final Activity activity) {

        String url = AppConfig.WEB_URL + "LoginUser.php?username=" + username;
        general.displayDialog("Signing user in...");

        String _url = url.replace(" ", "%20");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, _url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject object = jsonArray.getJSONObject(0);
                    int id = object.getInt("id");
                    String getUsername = object.getString("username");
                    String email = object.getString("email");
                    String mobile = object.getString("mobile");
                    String getPassword = object.getString("password");
                    String imagePath = object.getString("image_path");
                    String local_path = object.getString("local_path");

                    User user = new User(id, getUsername, email, mobile, getPassword, imagePath, local_path);
                    data.setUser(user);
                    data.setLoggedIn(true);
                    general.dismissDialog();
                    Toast.makeText(context, "Signup successful", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Home2Activity.class));
                    activity.finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setTag("login");
                general.dismissDialog();
                general.error("An error occurred. Check your connectivity.");
            }
        });

        requestQueue.add(stringRequest);
    }
}
