package com.ap.agroplus.network;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ap.agroplus.Callbacks.ProductsListener;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.VolleySingleton;
import com.ap.agroplus.information.Products;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class GetProducts {

    Context context;
    General general;
    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    ProductsListener productsListener;

    public GetProducts(Context context, ProductsListener productsListener) {
        this.context = context;
        general = new General(context);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        this.productsListener = productsListener;
    }

    public void GetAllProducts(final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {

        String url = AppConfig.WEB_URL + "Products.php";
        final ArrayList<Products> customData = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no product available.");
                            return;
                        }
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String username = object.getString("username");
                            String location = object.getString("location");
                            String image_dp = object.getString("image_path");
                            String product_image = object.getString("uploaded_image");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String time = object.getString("time");
                            String category = object.getString("category");
                            String caption = object.getString("caption");
                            String price = object.getString("price");
                            String product_link = object.getString("product_link");
                            Products products = new Products(id, username, location, image_dp, product_image, email, mobile, time, caption, price, category, product_link);
                            customData.add(products);
                        }
                    }
                    if (productsListener != null) {
                        productsListener.onProductCallback(customData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("An error occurred. Check your internet connection.");
                }
                //general.showAlert("An error occurred. Check your internet connection.");
            }
        });
        requestQueue.add(stringRequest);
    }

    public void GetAllProductFilter(String cat, String pr, final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {

        String url = AppConfig.WEB_URL + "Filter.php?category=" + cat + "&price=" + pr;
        final ArrayList<Products> customData = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no product available.");
                            return;
                        }
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String username = object.getString("username");
                            String location = object.getString("location");
                            String image_dp = object.getString("image_path");
                            String product_image = object.getString("uploaded_image");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String time = object.getString("time");
                            String category = object.getString("category");
                            String caption = object.getString("caption");
                            String price = object.getString("price");
                            String product_link = object.getString("product_link");
                            Products products = new Products(id, username, location, image_dp, product_image, email, mobile, time, caption, price, category, product_link);
                            customData.add(products);
                        }
                    }
                    if (productsListener != null) {
                        productsListener.onProductCallback(customData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("An error occurred. Check your internet connection.");
                }
                //general.showAlert("An error occurred. Check your internet connection.");
            }
        });
        requestQueue.add(stringRequest);
    }

    public void GetAllProducts(String category, final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {

        String url = AppConfig.WEB_URL + "ProductCategory.php?category=" + category;
        final ArrayList<Products> customData = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no product available.");
                            return;
                        }
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String username = object.getString("username");
                            String location = object.getString("location");
                            String image_dp = object.getString("image_path");
                            String product_image = object.getString("uploaded_image");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String time = object.getString("time");
                            String category = object.getString("category");
                            String caption = object.getString("caption");
                            String price = object.getString("price");
                            String product_link = object.getString("product_link");
                            Products products = new Products(id, username, location, image_dp, product_image, email, mobile, time, caption, price, category, product_link);
                            customData.add(products);
                        }
                    }
                    if (productsListener != null) {
                        productsListener.onProductCallback(customData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("An error occurred. Check your internet connection.");
                }
                //general.showAlert("An error occurred. Check your internet connection.");
            }
        });
        requestQueue.add(stringRequest);
    }

    public void GetAllProductsByUsername(String username, final AVLoadingIndicatorView loading, final ImageView iv, final TextView tv) {

        String url = AppConfig.WEB_URL + "ProductUsername.php?username=" + username;
        final ArrayList<Products> customData = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        if (loading != null && iv != null && tv != null) {
                            loading.smoothToHide();
                            iv.setVisibility(View.VISIBLE);
                            tv.setVisibility(View.VISIBLE);
                            tv.setText("There is no product available.");
                            return;
                        }
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String username = object.getString("username");
                            String location = object.getString("location");
                            String image_dp = object.getString("image_path");
                            String product_image = object.getString("uploaded_image");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String time = object.getString("time");
                            String category = object.getString("category");
                            String caption = object.getString("caption");
                            String price = object.getString("price");
                            String product_link = object.getString("product_link");
                            Products products = new Products(id, username, location, image_dp, product_image, email, mobile, time, caption, price, category, product_link);
                            customData.add(products);
                        }
                    }
                    if (productsListener != null) {
                        productsListener.onProductCallback(customData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("An error occurred. Check your internet connection.");
                }
                //general.showAlert("An error occurred. Check your internet connection.");
            }
        });
        requestQueue.add(stringRequest);
    }

    public void GetAllProductsById(int id, final ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        String url = AppConfig.WEB_URL + "ProductId.php?id=" + id;
        final ArrayList<Products> customData = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.contentEquals("null")) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(context, "Product does not exist", Toast.LENGTH_SHORT).show();
                    }
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String username = object.getString("username");
                            String location = object.getString("location");
                            String image_dp = object.getString("image_path");
                            String product_image = object.getString("uploaded_image");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String time = object.getString("time");
                            String category = object.getString("category");
                            String caption = object.getString("caption");
                            String price = object.getString("price");
                            String product_link = object.getString("product_link");
                            Products products = new Products(id, username, location, image_dp, product_image, email, mobile, time, caption, price, category, product_link);
                            customData.add(products);
                        }
                    }
                    if (productsListener != null) {
                        productsListener.onProductCallback(customData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "An error occurred. Check your internet connection.", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
