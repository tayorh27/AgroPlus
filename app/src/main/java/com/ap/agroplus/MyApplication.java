package com.ap.agroplus;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.devs.acr.AutoErrorReporter;
import com.onesignal.OneSignal;

import net.gotev.uploadservice.UploadService;

import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by sanniAdewale on 09/03/2017.
 */

public class MyApplication extends Application {

    private static MyApplication sInstance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        sInstance = this;
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        OkHttpClient client = new OkHttpClient(); // create your own OkHttp client
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("avenir_light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        OneSignal.startInit(this)
                .disableGmsMissingPrompt(true)
                .init();
//        AutoErrorReporter.get(this)
//                .setEmailAddresses(AppConfig.EMAIL_ADDRESS)
//                .setEmailSubject("Auto Crash Report")
//                .start();
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}
