package com.ap.agroplus.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ap.agroplus.Callbacks.ClickCallback;
import com.ap.agroplus.Callbacks.ProductsListener;
import com.ap.agroplus.Adapter.HomeAdapter;
import com.ap.agroplus.Adapter.PlaceArrayAdapter;
import com.ap.agroplus.AppConfig;
import com.ap.agroplus.General;
import com.ap.agroplus.R;
import com.ap.agroplus.Utility.Options;
import com.ap.agroplus.database.AppData;
import com.ap.agroplus.information.Products;
import com.ap.agroplus.information.User;
import com.ap.agroplus.network.FileUpload;
import com.ap.agroplus.network.GetProducts;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import id.zelory.compressor.Compressor;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Home2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ProductsListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ClickCallback {

    //Image request code
    private int FILTER_PRODUCT_REQUEST = 2017;
    private final static int CAMERA_RQ = 6969;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    General general;
    Options options;

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
    ArrayList<Products> current = new ArrayList<>();
    SwipeRefreshLayout swipe;
    JSONArray jsonArray, jsonArray1;
    FileUpload fileUpload;

    ProgressBar progressBar;
    AwesomeTextView tv1;
    ImageView iv1, retry;
    RelativeLayout relativeLayout;
    EditText edit_caption, edit_price;
    Spinner spinner;
    BootstrapCircleThumbnail thumbnail;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        First_Initial();

    }

    public void First_Initial() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        options = new Options(Home2Activity.this);
        data = new AppData(Home2Activity.this);
        general = new General(Home2Activity.this);
        user = data.getUser();
        productImage = (ImageView) findViewById(R.id.product_image);
        slideView = findViewById(R.id.slideView);
        InitializeFields();
        dim = findViewById(R.id.content_home);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
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
        adapter = new HomeAdapter(Home2Activity.this, this);
        recylerView.setLayoutManager(new LinearLayoutManager(Home2Activity.this));
        recylerView.setAdapter(adapter);

        CheckDatabaseForExistingUser();

        SyncingProducts();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        thumbnail = (BootstrapCircleThumbnail) navigationView.getHeaderView(0).findViewById(R.id.userDp);
        TextView tv_1 = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);
        TextView tv_2 = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userEmail);
        tv_1.setText(user.username);
        tv_2.setText(user.email);
        File file = new File(user.local_path);
        if (file.exists()) {
            thumbnail.setImageDrawable(BitmapDrawable.createFromPath(user.local_path));
        } else {
            String url = user.image_path;
            Glide.with(Home2Activity.this).load(url.replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(thumbnail);
        }

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncingProducts2();
            }
        });
        GetIntents();
    }

    private void InitializeFields() {
        progressBar = (ProgressBar) findViewById(R.id.uploading);
        tv1 = (AwesomeTextView) findViewById(R.id.tv_upload_text);
        iv1 = (ImageView) findViewById(R.id.close);
        retry = (ImageView) findViewById(R.id.retry);
        relativeLayout = (RelativeLayout) findViewById(R.id.part1);
        edit_caption = (EditText) findViewById(R.id.edit_caption);
        edit_price = (EditText) findViewById(R.id.edit_price);
        spinner = (Spinner) findViewById(R.id.edit_category);
    }

    private void GetIntents() {
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if (type.startsWith("image/")) {
                    slideView.setVisibility(View.VISIBLE);
                    handleSendImage(intent);
                }
            } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
                if (type.startsWith("image/")) {
                    slideView.setVisibility(View.VISIBLE);
                    handleMultipleImages(intent);
                }
            }
        }
    }

    private void SyncingProducts() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Loading all products");
        GetProducts getProducts = new GetProducts(Home2Activity.this, this);
        getProducts.GetAllProducts(loading, iv, tv);

    }

    public void SyncingProducts2() {
        GetProducts getProducts = new GetProducts(Home2Activity.this, this);
        getProducts.GetAllProducts(loading, iv, tv);

    }

    private void setUpAutoCompleteTextView() {
        recylerView = (RecyclerView) findViewById(R.id.recycler_view);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        mGoogleApiClient = new GoogleApiClient.Builder(Home2Activity.this)
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

    private void CheckDatabaseForExistingUser() {
        Set<String> getData = data.getHomeResults();
        Log.e("search", getData.toString());
        ArrayList<Products> customData = new ArrayList<>();
        try {
            JSONArray _jsonArray = new JSONArray(getData.toString());
            if (_jsonArray.length() > 0) {
                for (int i = 0; i < _jsonArray.length(); i++) {
                    JSONObject object = _jsonArray.getJSONObject(i);
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
                current = customData;
                adapter.LoadRecyclerView(customData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SaveResultLoad(ArrayList<Products> _products) {
        Set<String> currentSet = data.getHomeResults();
        currentSet.clear();
        for (int i = 0; i < _products.size(); i++) {
            Products products = _products.get(i);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("username", products.username);
                jsonObject.put("location", products.location);
                jsonObject.put("image_path", products.image_dp);
                jsonObject.put("uploaded_image", products.product_image);
                jsonObject.put("email", products.email);
                jsonObject.put("mobile", products.mobile);
                jsonObject.put("time", products.time);
                jsonObject.put("category", products.category);
                jsonObject.put("caption", products.caption);
                jsonObject.put("price", products.price);
                jsonObject.put("product_link", products.product_link);
                currentSet.add(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        data.setHomeResults(currentSet);
    }

    public void Refresh(View view) {
        SyncingProducts();
    }

    public void Close(View view) {
        slideUp.hide();
    }


    private void startCamera() {
        File saveFolder = new File(Environment.getExternalStorageDirectory(), "Agroplus");
        saveFolder.mkdir();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        String img_name = "IMG_" + day + "" + (month + 1) + "" + year + new Random().nextInt(23041) + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.withAppendedPath(Uri.parse(saveFolder.getAbsolutePath()), img_name));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_RQ);
        }


//        if (Environment.getExternalStorageDirectory().canWrite()) {
//            File saveFolder = new File(Environment.getExternalStorageDirectory(), "Agroplus");
//            if (!saveFolder.mkdirs())
//                throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");
//            new MaterialCamera(this)
//                    /** all the previous methods can be called, but video ones would be ignored */
//                    .stillShot() // launches the Camera in stillshot mode
//                    .saveDir(saveFolder)
//                    .start(CAMERA_RQ);
//        } else {
//            File saveFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Agroplus/");
//            if (!saveFolder.mkdirs())
//                throw new RuntimeException("Unable to create save directory, make sure WRITE_EXTERNAL_STORAGE permission is granted.");
//            new MaterialCamera(this)
//                    /** all the previous methods can be called, but video ones would be ignored */
//                    .stillShot() // launches the Camera in stillshot mode
//                    .saveDir(saveFolder)
//                    .start(CAMERA_RQ);
//        }

    }

    public void getImage() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 5
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private boolean ExamineAll() {
        boolean check = true;
        String location = mAutocompleteTextView.getText().toString();
        String caption = edit_caption.getText().toString();
        String price = edit_price.getText().toString();
        if (location.isEmpty() || caption.isEmpty() || price.isEmpty()) {
            check = false;
            general.showAlert("Please all fields must be filled.");
        } else if (spinner.getSelectedItemPosition() == 0) {
            check = false;
            general.showAlert("Please select a valid category.");
        }

        return check;
    }

    public void uploadImages(View view) {
        if (!ExamineAll()) {
            return;
        }
        slideUp.hide();
        relativeLayout.setVisibility(View.VISIBLE);
        tv1.setText("Uploading your product");
        int count = jsonArray1.length();
        ArrayList<String> pictures = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            try {
                pictures.add(jsonArray1.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String[] spins = getResources().getStringArray(R.array.categories);
        String cat_ = spins[spinner.getSelectedItemPosition()];
        fileUpload = new FileUpload(count, Home2Activity.this, relativeLayout, progressBar, tv1, iv1, retry, edit_caption, edit_price, spinner, mAutocompleteTextView, jsonArray, cat_);
        if (count == 1) {
            File comFile = new File(pictures.get(0));
            File al = Compressor.getDefault(Home2Activity.this).compressToFile(comFile);
            fileUpload.SetPaths(al.getPath(), "", "");
            fileUpload.Upload_one(al.getPath());
        } else if (count == 2) {
            File comFile = new File(pictures.get(0));
            File comFile1 = new File(pictures.get(1));
            File al = Compressor.getDefault(Home2Activity.this).compressToFile(comFile);
            File al1 = Compressor.getDefault(Home2Activity.this).compressToFile(comFile1);
            fileUpload.SetPaths(al.getPath(), al1.getPath(), "");
            fileUpload.Upload_two(al.getPath(), al1.getPath());
        } else if (count == 3) {
            File comFile = new File(pictures.get(0));
            File comFile1 = new File(pictures.get(1));
            File comFile2 = new File(pictures.get(2));
            File al = Compressor.getDefault(Home2Activity.this).compressToFile(comFile);
            File al1 = Compressor.getDefault(Home2Activity.this).compressToFile(comFile1);
            File al2 = Compressor.getDefault(Home2Activity.this).compressToFile(comFile2);
            fileUpload.SetPaths(al.getPath(), al1.getPath(), al2.getPath());
            fileUpload.Upload_three(al.getPath(), al1.getPath(), al2.getPath());
        }

    }

    private void CompressImage(String file_path, String file_path1, String file_path2) {
        if (!file_path.isEmpty()) {
            File comFile = new File(file_path);
            File al = Compressor.getDefault(Home2Activity.this).compressToFile(comFile);
            Log.e("Compress1", "File compressed to " + al.length());
        }
        if (!file_path1.isEmpty()) {
            File comFile1 = new File(file_path1);
            File al1 = Compressor.getDefault(Home2Activity.this).compressToFile(comFile1);
            Log.e("Compress1", "File compressed to " + al1.length());
        }
        if (!file_path2.isEmpty()) {
            File comFile2 = new File(file_path2);
            File al2 = Compressor.getDefault(Home2Activity.this).compressToFile(comFile2);
            Log.e("Compress1", "File compressed to " + al2.length());
        }
    }

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
        if (requestCode == CAMERA_RQ && resultCode == RESULT_OK && data != null) {
            slideUp.show();
            Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
            try {
                filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            productImage.setImageBitmap(bitmap);
            String path = getPath(filePath);
            String filename = path.substring(path.lastIndexOf("/") + 1);
            jsonArray = new JSONArray();
            jsonArray1 = new JSONArray();
            String webLink = AppConfig.WEB_URL + "product_images/" + filename;
            try {
                jsonArray.put(0, webLink);
                jsonArray1.put(0, path);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("filename", "camera = " + filename);
        }
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            slideUp.show();
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            Image img = images.get(0);
            productImage.setImageDrawable(BitmapDrawable.createFromPath(img.path));
            jsonArray = new JSONArray();
            jsonArray1 = new JSONArray();

            for (int i = 0; i < images.size(); i++) {
                String webLink = AppConfig.WEB_URL + "product_images/" + images.get(i).name;
                try {
                    jsonArray.put(i, webLink);
                    jsonArray1.put(i, images.get(i).path);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            for (int j = 0; j < jsonArray.length(); j++) {
                try {
                    Log.e("array" + j, "jsonArray = " + jsonArray.getString(j));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.e("array_all", "jsonArray = " + jsonArray.toString());

        }
        if (requestCode == FILTER_PRODUCT_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            String category = bundle.getString("category");
            String price = bundle.getString("price");
            GetProducts getProducts = new GetProducts(Home2Activity.this, this);
            getProducts.GetAllProductFilter(category, price, loading, iv, tv);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            new MaterialDialog.Builder(Home2Activity.this)
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

        if (id == R.id.action_filter) {
            startActivityForResult(new Intent(Home2Activity.this, FilterActivity.class), FILTER_PRODUCT_REQUEST);
        }
        if (id == R.id.action_search) {
            startActivity(new Intent(Home2Activity.this, SearchUserActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_contact) {
            startActivity(new Intent(Home2Activity.this, ContactActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(Home2Activity.this, ProfileActivity.class));
        } else if (id == R.id.nav_account) {
            Intent intent = new Intent(Home2Activity.this, UserAccountActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", user.username);
            bundle.putString("mobile", user.mobile);
            bundle.putString("email", user.email);
            bundle.putString("dp", user.image_path);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.nav_log_out) {
            new MaterialDialog.Builder(Home2Activity.this)
                    .title("Logout")
                    .content("Are you sure you want to logout?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            data.LogOut();
                            startActivity(new Intent(Home2Activity.this, LoginActivity.class));
                            finish();
                        }
                    }).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onProductCallback(ArrayList<Products> products) {
        current.clear();
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
            if (!products.isEmpty()) {
                SaveResultLoad(products);
                current = products;
                adapter.LoadRecyclerView(products);
                loading.smoothToHide();
                iv.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
            } else {
                if (current.size() > 0) {
                    loading.smoothToHide();
                    iv.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }
                Toast.makeText(this, "Failed to get products", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (products.isEmpty()) {
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("There is no products available.");
                }
                Toast.makeText(this, "Failed to get products", Toast.LENGTH_SHORT).show();
            } else {
                SaveResultLoad(products);
                current = products;
                if (loading != null && iv != null && tv != null) {
                    loading.smoothToHide();
                    iv.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                }
                adapter.LoadRecyclerView(products);
            }
            swipe.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        User myUser = data.getUser();
        File file = new File(myUser.local_path);
        if (file.exists()) {
            thumbnail.setImageDrawable(BitmapDrawable.createFromPath(myUser.local_path));
        } else {
            String url = myUser.image_path;
            Glide.with(Home2Activity.this).load(url.replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(thumbnail);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

//        Toast.makeText(this,
//                "Google Places API connection failed with error code:" +
//                        connectionResult.getErrorCode(),
//                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onHomeClick(View view, int position) {
        int id = view.getId();
        Products get_product = current.get(position);
        if (id == R.id.username) {
            options.UserAccount(get_product);
        }
        if (id == R.id.category) {
            options.Categories(get_product);
        }
        if (id == R.id.full_image) {
            options.FullImage(get_product);
        }
        if (id == R.id.btn_call) {
            options.CallUser(get_product, Home2Activity.this);
        }
        if (id == R.id.btn_email) {
            options.EmailUser(get_product);
        }
        if (id == R.id.btn_share) {
            options.Share(get_product);
        }
        if (id == R.id.image_dp) {
            options.UserAccount(get_product);
        }
    }


    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        Log.e("handleSendImage", "b4 imageUri != null");
        if (imageUri != null) {
            Log.e("handleSendImage", "after imageUri != null");
            slideUp.show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            productImage.setImageBitmap(bitmap);
            //String path = getPath(imageUri);
            String path = imageUri.getPath();
            String filename = path.substring(path.lastIndexOf("/") + 1);
            jsonArray = new JSONArray();
            jsonArray1 = new JSONArray();
            String webLink = AppConfig.WEB_URL + "product_images/" + filename;
            try {
                jsonArray.put(0, webLink);
                jsonArray1.put(0, path);

                Log.e("filename", "camera = " + filename);
            } catch (JSONException e) {
                Toast.makeText(this, "File path not found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris.size() > 3) {
            Toast.makeText(this, "Images should not be more than 3", Toast.LENGTH_LONG).show();
            return;
        }
        Log.e("handleMultipleImages", "b4 imageUri != null");
        if (imageUris != null) {
            Log.e("handleMultipleImages", "after imageUri != null");
            slideUp.show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUris.get(0));
                productImage.setImageBitmap(bitmap);
                for (int i = 0; i < imageUris.size(); i++) {

                    //String path = getPath(imageUris.get(i));
                    String path = imageUris.get(i).getPath();
                    String filename = path.substring(path.lastIndexOf("/") + 1);
                    jsonArray = new JSONArray();
                    jsonArray1 = new JSONArray();
                    String webLink = AppConfig.WEB_URL + "product_images/" + filename;
                    try {
                        jsonArray.put(i, webLink);
                        jsonArray1.put(i, path);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("filename", "camera = " + filename);
                }
            } catch (IOException e) {
                Toast.makeText(this, "File path not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
