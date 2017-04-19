package com.ap.agroplus.Activity;

import android.content.Context;
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
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CategoryActivity extends AppCompatActivity implements ProductsListener, ClickCallback {

    String category;
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
        setContentView(R.layout.activity_category);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        options = new Options(CategoryActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
            getSupportActionBar().setTitle(category);
        }

        recylerView = (RecyclerView) findViewById(R.id.recycler_view);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading);
        iv = (ImageView) findViewById(R.id.click_refresh);
        tv = (TextView) findViewById(R.id.tv_info);
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        loading.smoothToShow();
        adapter = new HomeAdapter(CategoryActivity.this, this);
        recylerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
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
        GetProducts getProducts = new GetProducts(CategoryActivity.this, this);
        getProducts.GetAllProducts(category, loading, iv, tv);

    }

    public void SyncingProducts2() {
        GetProducts getProducts = new GetProducts(CategoryActivity.this, this);
        getProducts.GetAllProducts(category, loading, iv, tv);

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
        if (swipe.isRefreshing()) {
            swipe.setRefreshing(false);
            if (!products.isEmpty()) {
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
            options.CallUser(get_product, CategoryActivity.this);
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
