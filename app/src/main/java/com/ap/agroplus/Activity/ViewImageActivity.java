package com.ap.agroplus.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ap.agroplus.R;
import com.ap.agroplus.TimeUpdate;
import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Date;

import in.myinnos.imagesliderwithswipeslibrary.Animations.DescriptionAnimation;
import in.myinnos.imagesliderwithswipeslibrary.SliderLayout;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.BaseSliderView;
import in.myinnos.imagesliderwithswipeslibrary.SliderTypes.TextSliderView;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.views.BannerSlider;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ViewImageActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {

    TextView menu_textView;
    BootstrapCircleThumbnail imageDp;
    AwesomeTextView username, time, caption, category;
    SliderLayout productDp;
    JSONArray jsonArray;
    AppBarLayout appBarLayout;
    RelativeLayout relativeLayout;
    String price = "";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative1);

        imageDp = (BootstrapCircleThumbnail) findViewById(R.id.image_dp);
        username = (AwesomeTextView) findViewById(R.id.username);
        category = (AwesomeTextView) findViewById(R.id.tv_category);
        time = (AwesomeTextView) findViewById(R.id.tv_time);
        caption = (AwesomeTextView) findViewById(R.id.caption);
        productDp = (SliderLayout) findViewById(R.id.sample);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            price = intent.getString("price");
            username.setText(intent.getString("username"));
            category.setText(intent.getString("category"));
            caption.setText(intent.getString("caption"));
            Date getTime = new Date(intent.getString("time"));
            long milli = getTime.getTime();
            time.setText(TimeUpdate.setAgo(milli));
            String getDp = intent.getString("dp");
            String use_link = getDp.replace(" ", "%20");
            Glide.with(ViewImageActivity.this).load(use_link).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(imageDp);


            try {
                jsonArray = new JSONArray(intent.getString("imgs"));
                for (int i = 0; i < jsonArray.length(); i++) {
                    //productDp.addBanner(new RemoteBanner(jsonArray.getString(i)));
                    TextSliderView textSliderView = new TextSliderView(ViewImageActivity.this);
                    // initialize a SliderLayout
                    textSliderView
                            .image(jsonArray.getString(i))
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                            .setOnSliderClickListener(ViewImageActivity.this);

                    //add your extra information
                    //textSliderView.bundle(new Bundle());
                    //textSliderView.getBundle().putString("extra", name);

                    productDp.addSlider(textSliderView);
                }
                //Glide.with(ViewImageActivity.this).load(jsonArray.getString(1).replace(" ", "%20")).fitCenter().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(productDp);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            productDp.setPresetTransformer(SliderLayout.Transformer.Stack);
            productDp.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
            productDp.setCustomAnimation(new DescriptionAnimation());
            productDp.setDuration(4000);

            productDp.setPresetTransformer("Stack");
            getSupportActionBar().setTitle(intent.getString("username"));

        }

        productDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appBarLayout.getVisibility() == View.VISIBLE && relativeLayout.getVisibility() == View.VISIBLE) {
                    appBarLayout.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.GONE);
                } else {
                    appBarLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        productDp.stopAutoCycle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_image, menu);
        menu_textView = (TextView) menu.findItem(R.id.action_price).getActionView();
        menu_textView.setText("₦" + price);
        menu_textView.setPadding(10, 10, 10, 10);
        menu_textView.setBackgroundColor(getResources().getColor(R.color.bootstrap_brand_success));
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
    public void onSliderClick(BaseSliderView slider) {

    }
}
