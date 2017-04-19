package com.ap.agroplus.network;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ap.agroplus.Activity.Home2Activity;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.VolleySingleton;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.mancj.slideup.SlideUp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class RegisterProduct {

    Context context;
    General general;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    int username;
    String location, uploaded_image, caption, price, time, category, product_link;
    //SlideUp slideUp;
    RelativeLayout relativeLayout;
    EditText edit_caption, edit_price;
    AutoCompleteTextView mAutocompleteTextView;
    Spinner spinner;
    ImageView retry;
    AwesomeTextView tv;

    public RegisterProduct(Context context, int username, String location, String uploaded_image, String caption, String price, String time, String category, String product_link,
                           RelativeLayout relativeLayout, EditText edit_caption, EditText edit_price, Spinner spinner, AutoCompleteTextView autoCompleteTextView,
                           ImageView retry, AwesomeTextView tv) {
        this.context = context;
        //this.slideUp = slideUp;
        general = new General(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.username = username;
        this.location = location;
        this.uploaded_image = uploaded_image;
        this.caption = caption;
        this.price = price;
        this.time = time;
        this.category = category;
        this.product_link = product_link;
        this.relativeLayout = relativeLayout;
        this.edit_caption = edit_caption;
        this.edit_price = edit_price;
        this.spinner = spinner;
        this.mAutocompleteTextView = autoCompleteTextView;
        this.retry = retry;
        this.tv = tv;
    }

    public void Register() {
        String url = AppConfig.WEB_URL + "ProductReg.php";
        //general.showProgress("Uploading product");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int success;
                try {
                    JSONObject object = new JSONObject(response);
                    success = object.getInt("success");
                    if (success == 1) {
                        //general.dismissProgress();
                        relativeLayout.setVisibility(View.GONE);
                        edit_caption.setText("");
                        edit_price.setText("");
                        spinner.setSelection(0);
                        mAutocompleteTextView.setText("");
                        //Snackbar.make(, "", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(context, "Product uploaded successfully.", Toast.LENGTH_LONG).show();
                        Home2Activity home2Activity = new Home2Activity();
                        //home2Activity.First_Initial();
                        //home2Activity.SyncingProducts2();
                    } else if (success == 0) {
                        tv.setText("Upload failed. Try again");
                        retry.setVisibility(View.VISIBLE);
                        retry.setTag("volley");
                        //general.dismissProgress();
                        //general.showAlert("An error occurred. Check your internet connection.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("Upload failed. Try again");
                retry.setVisibility(View.VISIBLE);
                retry.setTag("volley");
                //general.dismissProgress();
                //general.showAlert("An error occurred. Check your internet connection.");
            }
        }) {
            @Override
            public String getPostBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", username + "");
                params.put("location", location);
                params.put("uploaded_image", uploaded_image);
                params.put("caption", caption);
                params.put("price", price);
                params.put("category", category);
                params.put("time", time);
                params.put("product_link", product_link);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
