package com.ap.agroplus;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ap.agroplus.Activity.LoginActivity;
import com.ap.agroplus.Activity.RegisterActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    Shimmer shimmer;
    ShimmerTextView stAgro, stPlus;
    File[] deleteFiles = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                File myDir = new File(root + "/AgroPlus");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                    deleteFiles = myDir.listFiles();
                }
                File myDir1 = new File(root + "/AgroPlus/Dp");
                if (!myDir1.exists()) {
                    myDir1.mkdirs();
                }
                File myDir2 = new File(root + "/AgroPlus/Products");
                if (!myDir2.exists()) {
                    myDir2.mkdirs();
                }
                if (deleteFiles == null) {
                    //cant do shit
                    int count = 0;
                } else {
//                    if (deleteFiles.length > 0) {
//                        for (File item : deleteFiles) {
//                            item.delete();
//                        }
//                    }
                }
            }
        } catch (Exception e) {
            Log.e("MainActivityLog", e.toString());
        }


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
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}
