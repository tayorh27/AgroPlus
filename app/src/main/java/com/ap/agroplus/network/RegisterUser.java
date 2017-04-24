package com.ap.agroplus.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ap.agroplus.Activity.Home2Activity;
import com.ap.agroplus.Activity.HomeActivity;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.VolleySingleton;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.User;
import com.beardedhen.androidbootstrap.BootstrapButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class RegisterUser {

    Context context;
    General general;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    String username, email, mobile, password, path, local_path, access_code;
    AppData data;

    public RegisterUser(Context context, String username, String email, String mobile, String password, String path, String local_path, String access_code) {
        this.context = context;
        general = new General(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.username = username;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.path = path;
        this.local_path = local_path;
        this.access_code = access_code;
        data = new AppData(context);
    }

    public void Register(final BootstrapButton button, final Activity activity) {
        String url = AppConfig.WEB_URL + "Register.php";
        general.showProgress("Registering user");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int success;
                try {
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if (success == 1) {
                        general.dismissProgress();
                        button.setTag("login");
                        GetLoginFromServer getLoginFromServer = new GetLoginFromServer(context, username, password);
                        getLoginFromServer.LoginAfterReg(button, activity);
                        //Toast.makeText(context, "Registration successful. Login now.", Toast.LENGTH_LONG).show();

                    } else if (success == 0) {
                        button.setTag("register");
                        general.dismissProgress();
                        general.showAlert("An error occurred. Check your internet connection.");
                    } else if (success == 2) {
                        button.setTag("register");
                        general.dismissProgress();
                        general.showAlert("An error occurred. Email already exists.");
                    } else if (success == 3) {
                        button.setTag("register");
                        general.dismissProgress();
                        general.showAlert("An error occurred. Username already exists.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                button.setTag("register");
                general.dismissProgress();
                general.showAlert("An error occurred. Check your internet connection.");
            }
        }) {
            @Override
            public String getPostBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("email", email);
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("image_path", path);
                params.put("local_path", local_path);
                params.put("access_code", access_code);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
