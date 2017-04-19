package com.ap.agroplus.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ap.agroplus.Callbacks.ClickCallback;
import com.ap.agroplus.Callbacks.ProductsListener;
import com.ap.agroplus.Adapter.HomeAdapter;
import com.ap.agroplus.Adapter.PlaceArrayAdapter;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.R;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.extras.UploadImages;
import com.ap.agroplus.information.Products;
import com.ap.agroplus.information.User;
import com.ap.agroplus.network.GetProducts;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.mancj.slideup.SlideUp;
import com.wang.avi.AVLoadingIndicatorView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ProductsListener, ClickCallback {

    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    private final static int CAMERA_RQ = 6969;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    General general;

    //Uri to store the image uri
    private Uri filePath;
    View slideView, dim;
    SlideUp slideUp;
    ImageView productImage;
    String image_path = AppConfig.WEB_URL + "product_images/";
    AppData data;
    AutoCompleteTextView mAutocompleteTextView;
    private static final String LOG_TAG = "HomeActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(0, -0), new LatLng(0, -0));
    User user;

    HomeAdapter adapter;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    int upload_image_code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("AgroPlus");

        data = new AppData(HomeActivity.this);
        general = new General(HomeActivity.this);
        user = data.getUser();
        productImage = (ImageView) findViewById(R.id.product_image);
        slideView = findViewById(R.id.slideView);
        dim = findViewById(R.id.content_home);
        slideUp = new SlideUp.Builder(slideView)
                .withStartState(SlideUp.State.HIDDEN)
                .withLoggingEnabled(true)
                .withStartGravity(Gravity.BOTTOM)
                .withListeners(new SlideUp.Listener() {
                    @Override
                    public void onSlide(float percent) {
                        //dim.setAlpha(1 - (percent / 100));
                    }

                    @Override
                    public void onVisibilityChanged(int visibility) {

                    }
                })
                .build();
        setUpAutoCompleteTextView();
        loading.smoothToShow();
        adapter = new HomeAdapter(HomeActivity.this,this);
        recylerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        recylerView.setAdapter(adapter);
        SyncingProducts();


    }

    private void SyncingProducts() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        GetProducts getProducts = new GetProducts(HomeActivity.this, this);
        //getProducts.GetAllProducts();

    }

    private void SyncingProducts2() {
        GetProducts getProducts = new GetProducts(HomeActivity.this, this);
       // getProducts.GetAllProducts();

    }

    private void setUpAutoCompleteTextView() {
        recylerView = (RecyclerView) findViewById(R.id.recycler_view);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .edit_location);
        mAutocompleteTextView.setThreshold(3);

        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);
        mAutocompleteTextView.setOnItemClickListener(mAutoCompleteClickListener);
    }

    private AdapterView.OnItemClickListener mAutoCompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();
        }
    };

    private void startCamera() {
        if (Environment.getExternalStorageDirectory().canWrite()) {
            File saveFolder = new File(Environment.getExternalStorageDirectory(), "Agroplus");
            if (!saveFolder.mkdirs())
                throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");
            new MaterialCamera(this)
                    /** all the previous methods can be called, but video ones would be ignored */
                    .stillShot() // launches the Camera in stillshot mode
                    .saveDir(saveFolder)
                    .start(CAMERA_RQ);
        } else {
            File saveFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Agroplus/");
            if (!saveFolder.mkdirs())
                throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");
            new MaterialCamera(this)
                    /** all the previous methods can be called, but video ones would be ignored */
                    .stillShot() // launches the Camera in stillshot mode
                    .saveDir(saveFolder)
                    .start(CAMERA_RQ);
        }

    }

    public void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void compressImage() {
        Compressor.getDefault(HomeActivity.this).compressToFile(new File(getPath(filePath)));
    }

    public void uploadMultipart2() {
        final String path = getPath(filePath);
        String filename = path.substring(path.lastIndexOf("/") + 1);
        image_path += filename;
        Log.e("Filename", "Filename = " + filename);
        new Thread(new Runnable() {
            @Override
            public void run() {
                upload_image_code = UploadImages.uploadFile(path, "upload_media.php");

            }
        }).start();
    }

    public void uploadMultipart() {

        //getting the actual path of the image
        String path = getPath(filePath);
        String filename = path.substring(path.lastIndexOf("/") + 1);
        image_path += filename;
        Log.e("Filename", "Filename = " + filename);

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();
            String url = AppConfig.WEB_URL + "upload_media.php";

            //Creating a multi part request
            new MultipartUploadRequest(HomeActivity.this, url)
                    .addFileToUpload(path, "uploaded_file") //Adding file
                    .addParameter("name", filename) //Adding text parameter to the request
                    // .addParameter("Connection", "Keep-Alive")
                    // .addParameter("ENCRYPT", "multipart/form-data")
                    // .addParameter("Content-Type", "multipart/form-data;boundary=*****")
                    // .setMethod("POST")
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(HomeActivity.this, exc.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Received recording or error from MaterialCamera
        slideUp.show();
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
                try {
                    filePath = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                productImage.setImageBitmap(bitmap);
                compressImage();
                uploadMultipart2();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                productImage.setImageBitmap(bitmap);
                compressImage();
                uploadMultipart2();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            new MaterialDialog.Builder(HomeActivity.this)
                    .items(R.array.media_options)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            if (which == 0) {
                                startCamera();
                            } else if (which == 1) {
                                getImage();
                            }
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    EditText edit_caption, edit_price;

    public void uploadProduct(View view) {
        edit_caption = (EditText) findViewById(R.id.edit_caption);
        edit_price = (EditText) findViewById(R.id.edit_price);

        String username = user.username;
        String uploaded_image = image_path;
        String location = mAutocompleteTextView.getText().toString();
        String caption = edit_caption.getText().toString();
        String price = edit_price.getText().toString();
        Date date = new Date();
        String time = date.toLocaleString();

        if (location.length() < 0 || caption.length() < 0 || price.length() < 0) {
            general.showAlert("Please all fields must be filled");
            return;
        }
        if (upload_image_code != 200) {
            uploadMultipart2();
        }
        //RegisterProduct registerProduct = new RegisterProduct(HomeActivity.this, slideUp, username, location, uploaded_image, caption, price, time);
        //registerProduct.Register();
        SyncingProducts2();
        edit_caption.setText("");
        edit_price.setText("");
        mAutocompleteTextView.setText("");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
        //Toast.makeText(HomeActivity.this, "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    public void Refresh(View view) {
        SyncingProducts();
    }

    @Override
    public void onProductCallback(ArrayList<Products> products) {
        if (products.isEmpty()) {
            loading.smoothToHide();
            iv.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText("There is no products available.");
        } else {
            //current = shops;
            loading.smoothToHide();
            iv.setVisibility(View.GONE);
            tv.setVisibility(View.GONE);
            adapter.LoadRecyclerView(products);
        }

    }

    @Override
    public void onHomeClick(View view, int position) {

    }
}
