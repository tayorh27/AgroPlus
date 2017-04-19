package com.ap.agroplus.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.agroplus.ApiConfig;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.R;
import com.ap.agroplus.ServerResponse;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.User;
import com.ap.agroplus.network.RegisterUser;
import com.ap.agroplus.network.UpdateUserImage;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    BootstrapCircleThumbnail iv;
    TextView username, email, phone;
    AppData data;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    String image_path = AppConfig.WEB_URL + "displayimages/";
    ProgressBar progressBar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        data = new AppData(ProfileActivity.this);
        User user = data.getUser();

        iv = (BootstrapCircleThumbnail) findViewById(R.id.imageViewDp);
        username = (TextView) findViewById(R.id.tvUsername);
        email = (TextView) findViewById(R.id.tvEmail);
        phone = (TextView) findViewById(R.id.tvPhone);
        progressBar = (ProgressBar) findViewById(R.id.myProgressBar);

        username.setText(user.username);
        email.setText(user.email);
        phone.setText(user.mobile);

        File file = new File(user.local_path);
        if (file.exists()) {
            iv.setImageDrawable(BitmapDrawable.createFromPath(user.local_path));
        } else {
            String url = user.image_path;
            Glide.with(ProfileActivity.this).load(url.replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv);
        }
    }

    public void UpdateImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null)
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAutoZoomEnabled(true)
                        .setOutputCompressQuality(100)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    filePath = resultUri;
                    //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    //iv.setImageBitmap(bitmap);
                    UpdateImageNow();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void UpdateImageNow() {
        String path = filePath.getPath();//getPath(filePath);
        String filename = path.substring(path.lastIndexOf("/") + 1);
        image_path += filename;
        Log.e("register", "path = " + path + " filename = " + filename);
        Upload_one(path, image_path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_change) {
            startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void Upload_one(final String path1, final String image_path) {
        File file = new File(path1);
        progressBar.setVisibility(View.VISIBLE);
        // Parsing any Media type file
        RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file1", file.getName(), requestBody1);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<ServerResponse> call = getResponse.uploadDisplayImage(fileToUpload1);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        UpdateUserImage updateUserImage = new UpdateUserImage(ProfileActivity.this, path1, image_path, iv, progressBar);
                        updateUserImage.UpdateImage();
                        Log.e("tonSuccess", serverResponse.getMessage());
                    } else {
                        Log.e("tonFailure", serverResponse.getMessage());
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "An error occurred. Check your internet connectivity", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.e("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, "An error occurred. Check your internet connectivity", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
