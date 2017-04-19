package com.ap.agroplus.network;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ap.agroplus.Activity.Home2Activity;
import com.ap.agroplus.ApiConfig;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.R;
import com.ap.agroplus.ServerResponse;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.User;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;

import org.json.JSONArray;

import java.io.File;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sanniAdewale on 07/04/2017.
 */

public class FileUpload {

    Context context;
    ProgressBar progressBar;
    AwesomeTextView tv;
    ImageView iv, retry;
    RelativeLayout relativeLayout;
    Call<ServerResponse> call;
    int count;
    String pt1, pt2, pt3;
    EditText edit_caption, edit_price;
    AutoCompleteTextView mAutocompleteTextView;
    Spinner spinner;
    AppData data;
    JSONArray jsonArray;
    String category_;

    public FileUpload(int count, Context context, final RelativeLayout relativeLayout, ProgressBar progressBar, AwesomeTextView tv, ImageView iv, final ImageView retry,
                      EditText edit_caption, EditText edit_price, Spinner spinner, AutoCompleteTextView autoCompleteTextView,
                      JSONArray jsonArray, String category_) {
        this.context = context;
        this.relativeLayout = relativeLayout;
        this.progressBar = progressBar;
        this.tv = tv;
        this.iv = iv;
        this.retry = retry;
        this.edit_caption = edit_caption;
        this.edit_price = edit_price;
        this.spinner = spinner;
        this.mAutocompleteTextView = autoCompleteTextView;
        this.jsonArray = jsonArray;
        this.category_ = category_;
        this.count = count;
        data = new AppData(context);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (call != null) {
                    call.cancel();
                    relativeLayout.setVisibility(View.GONE);
                }
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTag = retry.getTag().toString();
                if (getTag.contentEquals("volley")) {
                    uploadProduct();
                } else {
                    retry_upload();
                }
            }
        });
    }

    public void SetPaths(String pt1, String pt2, String pt3) {
        this.pt1 = pt1;
        this.pt2 = pt2;
        this.pt3 = pt3;
    }

    private void retry_upload() {
        tv.setText("Uploading your product...");
        if (count == 1) {
            Upload_one(pt1);
        } else if (count == 2) {
            Upload_two(pt1, pt2);
        } else if (count == 3) {
            Upload_three(pt1, pt2, pt3);
        }
        retry.setVisibility(View.GONE);
    }

    public void Upload_one(String path1) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path1);

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file.getName(), requestBody1);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        call = getResponse.uploadMulFile(fileToUpload1);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Log.e("tonSuccess", serverResponse.getMessage());
                        tv.setText("Finishing Up...");
                        uploadProduct();
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("tonFailure", serverResponse.getMessage());
                        tv.setText("Upload failed. Try again");
                        retry.setVisibility(View.VISIBLE);
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.e("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                tv.setText("Upload failed. Try again");
                retry.setVisibility(View.VISIBLE);
                //Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Upload_two(String path1, String path2) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path1);
        File file1 = new File(path2);

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file1);

        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file2", file1.getName(), requestBody2);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        call = getResponse.uploadMulFile(fileToUpload1, fileToUpload2);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Log.e("tonSuccess", serverResponse.getMessage());
                        tv.setText("Finishing Up...");
                        uploadProduct();
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("tonFailure", serverResponse.getMessage());
                        tv.setText("Upload failed. Try again");
                        retry.setVisibility(View.VISIBLE);
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.e("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                tv.setText("Upload failed. Try again");
                retry.setVisibility(View.VISIBLE);
                //Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Upload_three(String path1, String path2, String path3) {
        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(path1);
        File file1 = new File(path2);
        File file2 = new File(path3);

        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
        RequestBody requestBody2 = RequestBody.create(MediaType.parse("*/*"), file1);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("*/*"), file2);

        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file.getName(), requestBody1);
        MultipartBody.Part fileToUpload2 = MultipartBody.Part.createFormData("file2", file1.getName(), requestBody2);
        MultipartBody.Part fileToUpload3 = MultipartBody.Part.createFormData("file3", file2.getName(), requestBody3);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        call = getResponse.uploadMulFile(fileToUpload1, fileToUpload2, fileToUpload3);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Log.e("tonSuccess", serverResponse.getMessage());
                        tv.setText("Finishing Up...");
                        uploadProduct();
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("tonFailure", serverResponse.getMessage());
                        tv.setText("Upload failed. Try again");
                        retry.setVisibility(View.VISIBLE);
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.e("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                tv.setText("Upload failed. Try again");
                retry.setVisibility(View.VISIBLE);
                //Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void uploadProduct() {
        tv.setText("Finishing Up...");
        User user = data.getUser();
        int username = user.id;
        String uploaded_image = jsonArray.toString();
        String location = mAutocompleteTextView.getText().toString();
        String caption = edit_caption.getText().toString();
        String price = edit_price.getText().toString();
        String category = category_;
        String product_link = AppConfig.WEB_URL + "?category=" + category;
        Date date = new Date();
        String time = date.toLocaleString();
        RegisterProduct registerProduct = new RegisterProduct(context, username, location, uploaded_image, caption, price, time, category, product_link,
                relativeLayout, edit_caption, edit_price, spinner, mAutocompleteTextView, retry, tv);
        registerProduct.Register();

    }

}
