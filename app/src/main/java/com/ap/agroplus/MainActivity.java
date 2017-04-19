package com.ap.agroplus;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ap.agroplus.Activity.LoginActivity;
import com.ap.agroplus.Activity.RegisterActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Shimmer shimmer;
    ShimmerTextView stAgro, stPlus;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stAgro = (ShimmerTextView) findViewById(R.id.agro);
        stPlus = (ShimmerTextView) findViewById(R.id.plus);

        shimmer = new Shimmer();
        shimmer.start(stAgro);
        shimmer.start(stPlus);

        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //shimmer.cancel();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
