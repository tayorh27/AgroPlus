package com.ap.agroplus.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.agroplus.Adapter.HomeAdapter;
import com.ap.agroplus.Callbacks.ClickCallback;
import com.ap.agroplus.Callbacks.ProductsListener;
import com.ap.agroplus.R;
import com.ap.agroplus.Utility.Options;
import com.ap.agroplus.information.Products;
import com.ap.agroplus.network.GetProducts;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class UserAccountActivity extends AppCompatActivity implements ProductsListener, ClickCallback {

    String g_username, g_mobile, g_email, g_dp;
    BootstrapCircleThumbnail profile_image;
    AwesomeTextView user, number_of_posts, tv_posts;
    TextView tvCall, tvEmail;

    HomeAdapter adapter;
    RecyclerView recylerView;
    AVLoadingIndicatorView loading;
    ImageView iv;
    TextView tv;
    ArrayList<Products> current = new ArrayList<>();
    SwipeRefreshLayout swipe;
    Options options;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        options = new Options(UserAccountActivity.this);

        profile_image = (BootstrapCircleThumbnail) findViewById(R.id.image_dp);
        user = (AwesomeTextView) findViewById(R.id.username);
        number_of_posts = (AwesomeTextView) findViewById(R.id.numberPosts);
        tv_posts = (AwesomeTextView) findViewById(R.id.posts);
        tvCall = (TextView) findViewById(R.id.tv_call);
        tvEmail = (TextView) findViewById(R.id.tv_email);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            g_dp = bundle.getString("dp");
            g_email = bundle.getString("email");
            g_mobile = bundle.getString("mobile");
            g_username = bundle.getString("username");
        }

        getSupportActionBar().setTitle(g_username);

        Glide.with(UserAccountActivity.this).load(g_dp.replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(profile_image);
        user.setText(g_username);
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + g_mobile));
                if (ActivityCompat.checkSelfPermission(UserAccountActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UserAccountActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 10);
                    return;
                }
                startActivity(intent);
            }
        });
        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{g_email});
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        recylerView = (RecyclerView) findViewById(R.id.recycler_view);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        swipe = (SwipeRefreshLayout) findViewById(R.id.mySwipe);
        loading.smoothToShow();
        adapter = new HomeAdapter(UserAccountActivity.this, this);
        recylerView.setLayoutManager(new LinearLayoutManager(UserAccountActivity.this));
        recylerView.setAdapter(adapter);
        SyncingProducts();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncingProducts2();
            }
        });

    }

    private void SyncingProducts() {
        loading.smoothToShow();
        iv.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        tv.setText("Loading all products");
        GetProducts getProducts = new GetProducts(UserAccountActivity.this, this);
        getProducts.GetAllProductsByUsername(g_username, loading, iv, tv);

    }

    public void SyncingProducts2() {
        GetProducts getProducts = new GetProducts(UserAccountActivity.this, this);
        getProducts.GetAllProductsByUsername(g_username, loading, iv, tv);

    }

    public void Refresh(View view) {
        SyncingProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.view_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductCallback(ArrayList<Products> products) {
        current.clear();
        if (swipe != null && swipe.isRefreshing()) {
            swipe.setRefreshing(false);
            if (!products.isEmpty()) {
                current = products;
                number_of_posts.setText(products.size() + "");
                if (products.size() == 1) {
                    tv_posts.setText("Post");
                } else if (products.size() > 1) {
                    tv_posts.setText("Posts");
                }
                loading.smoothToHide();
                iv.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                adapter.LoadRecyclerView(products);
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
                number_of_posts.setText("0");
                Toast.makeText(this, "Failed to get products", Toast.LENGTH_SHORT).show();
            } else {
                current = products;
                number_of_posts.setText(products.size() + "");
                if (products.size() == 1) {
                    tv_posts.setText("Post");
                } else if (products.size() > 1) {
                    tv_posts.setText("Posts");
                }
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
            options.CallUser(get_product, UserAccountActivity.this);
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
}
