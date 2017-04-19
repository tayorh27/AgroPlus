package com.ap.agroplus;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class AppConfig {

    public static String WEB_URL = "http://www.bistelint.com.ng/agroplus/";
    public static String USERNAME = "gisanrinadetayo@gmail.com";
    public static String PASSKEY = "gafarmariam1234$";
    public static String PHONE_NUMBER = "08100865962";
    public static String EMAIL_ADDRESS = "info@agroplus.com.ng";

    public static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(AppConfig.WEB_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

}
