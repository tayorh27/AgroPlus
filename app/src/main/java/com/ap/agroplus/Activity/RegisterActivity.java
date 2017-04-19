package com.ap.agroplus.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ap.agroplus.ApiConfig;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.R;
import com.ap.agroplus.ServerResponse;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.extras.UploadImages;
import com.ap.agroplus.network.GetLoginFromServer;
import com.ap.agroplus.network.RegisterUser;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.github.bijoysingh.starter.util.PermissionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegisterActivity extends AppCompatActivity {

    EditText et_username, et_email, et_mobile, et_password;
    General general;
    CircleImageView imageView;

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //Bitmap to get image from gallery
    private Bitmap bitmap;

    //Uri to store the image uri
    private Uri filePath;
    String image_path = AppConfig.WEB_URL + "displayimages/";
    AppData data;

    int upload_image_code = 0;
    String username, email, mobile, password;
    BootstrapButton bootstrapButton;
    String path = "", access_code = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        general = new General(RegisterActivity.this);
        data = new AppData(RegisterActivity.this);

        et_username = (EditText) findViewById(R.id.edit_username);
        et_email = (EditText) findViewById(R.id.edit_email);
        et_mobile = (EditText) findViewById(R.id.edit_number);
        et_password = (EditText) findViewById(R.id.edit_password);
        imageView = (CircleImageView) findViewById(R.id.upload_dp);
        bootstrapButton = (BootstrapButton) findViewById(R.id.btn_reg);

        if (Build.VERSION.SDK_INT > 19) {
            RequestPermissions();
        }
    }

    private void RequestPermissions() {
        // Could be more than one permissions here
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE};

        // Initialise the manager object, with required permissions
        PermissionManager manager = new PermissionManager(RegisterActivity.this, permissions);

        if (!manager.hasAllPermissions()) {
            manager.requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AgroPlus");

    // boolean isPhoto = false;

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
                        //.setOutputUri(Uri.fromFile(file))
                        .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    //file.mkdirs();
                    //isPhoto = true;
                    Uri resultUri = result.getUri();
                    filePath = resultUri;
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                    imageView.setImageBitmap(bitmap);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        Log.e("Image Path", "Path = " + path);
        return path;
    }


    public void Register(View view) {
        username = et_username.getText().toString();
        email = et_email.getText().toString();
        mobile = et_mobile.getText().toString();
        password = et_password.getText().toString();

        if (username.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty()) {
            general.showAlert("Please all fields must be filled.");
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            general.showAlert("Invalid email address.");
            return;
        }
        if (filePath == null) {
            general.showAlert("Please upload a picture.");
            return;
        }
        path = filePath.getPath();//getPath(filePath);
        String filename = path.substring(path.lastIndexOf("/") + 1);
        image_path += filename;
        Log.e("register", "path = " + path + " filename = " + filename);
        access_code = "AP" + username.substring(0, 1) + email.substring(0, 1) + new Random().nextInt(9000);
        String tag = bootstrapButton.getTag().toString();
        if (tag.contentEquals("login")) {
            GetLoginFromServer getLoginFromServer = new GetLoginFromServer(RegisterActivity.this, username, password);
            getLoginFromServer.LoginAfterReg(bootstrapButton, RegisterActivity.this);
        } else if (tag.contentEquals("register")) {
            RegisterUser registerUser = new RegisterUser(RegisterActivity.this, username, email, mobile, password, image_path, path, access_code);
            registerUser.Register(bootstrapButton, RegisterActivity.this);
        } else {
            Upload_one(path, access_code);
        }

    }


    public void Upload_one(final String path1, final String access_code) {
        general.showProgress("Please wait...");
        File file = new File(path1);

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
                        general.dismissProgress();
                        RegisterUser registerUser = new RegisterUser(RegisterActivity.this, username, email, mobile, password, image_path, path, access_code);
                        registerUser.Register(bootstrapButton, RegisterActivity.this);
                        Log.e("tonSuccess", serverResponse.getMessage());
                        //Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("tonFailure", serverResponse.getMessage());
                        general.dismissProgress();
                        general.showAlert("An error occurred. Check your internet connection.");
                    }
                } else {
                    assert serverResponse != null;
                    Log.e("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                general.dismissProgress();
                general.showAlert("An error occurred. Check your internet connection.");
            }
        });
    }

    public void AlreadyHaveAccount(View view) {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
