package com.ap.agroplus.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ap.agroplus.Callbacks.ProductsListener;
import com.ap.agroplus.General;
import com.ap.agroplus.R;
import com.ap.agroplus.TimeUpdate;
import com.ap.agroplus.information.Products;
import com.ap.agroplus.network.GetProducts;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import in.myinnos.imagesliderwithswipeslibrary.SliderLayout;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.BaseSliderView;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.TextSliderView;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewFromOutsideActivity extends AppCompatActivity implements ProductsListener {

    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    TextView menu_textView;
    BootstrapCircleThumbnail imageDp;
    AwesomeTextView username, time, caption, category;
    BannerSlider productDp;
    JSONArray jsonArray;
    AppBarLayout appBarLayout;
    RelativeLayout relativeLayout1;
    int getId = 0;
    General general;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_from_outside);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        relativeLayout = (RelativeLayout) findViewById(R.id.layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.view_swipe);
        general = new General(ViewFromOutsideActivity.this);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relative1);

        imageDp = (BootstrapCircleThumbnail) findViewById(R.id.image_dp);
        username = (AwesomeTextView) findViewById(R.id.username);
        category = (AwesomeTextView) findViewById(R.id.tv_category);
        time = (AwesomeTextView) findViewById(R.id.tv_time);
        caption = (AwesomeTextView) findViewById(R.id.caption);
        productDp = (BannerSlider) findViewById(R.id.sample);

        productDp.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int position) {
                if (appBarLayout.getVisibility() == View.VISIBLE && relativeLayout.getVisibility() == View.VISIBLE) {
                    appBarLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    appBarLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Refresh();
            }
        });
        GetIntent();
    }

    private void Refresh() {
        GetProducts getProducts = new GetProducts(ViewFromOutsideActivity.this, this);
        getProducts.GetAllProductsById(getId, progressBar);
    }

    public void GetIntent() {
        Intent intent = getIntent();

        if (intent != null) {
            String action = intent.getAction();
            String scheme = intent.getScheme();
            if (Intent.ACTION_VIEW.equals(action)) {//&& scheme != null
                //if ("http://www.bistelint.com.ng".equals(scheme))
                handleSendText(intent);
            }
        }
    }

    private void handleSendText(Intent intent) {
        Uri data = intent.getData();
        Log.e("data", "handleSendText = " + data.getScheme() + "\n" + data.getHost() + "\n" + data.getPath());
        String sharedText = data.toString();//.getPath();
        if ((sharedText.lastIndexOf("=") + 1) > 0) {
            String getMainUrl = sharedText.substring(sharedText.lastIndexOf("=") + 1);
            //String get_id_from_url = getMainUrl.substring(getMainUrl.lastIndexOf("&") + 1);
            //int getValue = Integer.parseInt(getMainUrl.substring(get_id_from_url.indexOf("=") + 1));
            int getValue = Integer.parseInt(getMainUrl);
            getId = getValue;
            Log.e("main url", "sharedText = " + sharedText + "\ngetMainUrl = " + getMainUrl + "\ngetValue = " + getValue);
            GetProducts getProducts = new GetProducts(ViewFromOutsideActivity.this, this);
            getProducts.GetAllProductsById(getId, progressBar);
        } else {
            Toast.makeText(ViewFromOutsideActivity.this, "Invalid AgroPlus url", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_image, menu);
        menu_textView = (TextView) menu.findItem(R.id.action_price).getActionView();
        menu_textView.setPadding(10, 10, 10, 10);
        menu_textView.setBackgroundColor(getResources().getColor(R.color.bootstrap_brand_success));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        if (id == R.id.action_save) {
            try {
                if (productDp.getCurrentSlidePosition() > 0) {
                    String image_current_url = jsonArray.getString(productDp.getCurrentSlidePosition());
                    general.SaveImage(image_current_url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductCallback(ArrayList<Products> products) {
        if (!products.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            Products product = products.get(0);
            menu_textView.setText("₦" + product.price);
            username.setText(product.username);
            category.setText(product.category);
            caption.setText(product.caption);
            Date getTime = new Date(product.time);
            long milli = getTime.getTime();
            time.setText(TimeUpdate.setAgo(milli));
            Glide.with(ViewFromOutsideActivity.this).load(product.image_dp.replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).into(imageDp);


            try {
                jsonArray = new JSONArray(product.product_image);
                for (int i = 0; i < jsonArray.length(); i++) {
                    productDp.addBanner(new RemoteBanner(jsonArray.getString(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getSupportActionBar().setTitle(product.username);
        }
    }
}
