package com.ap.agroplus.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.ap.agroplus.Activity.CategoryActivity;
import com.ap.agroplus.Activity.Home2Activity;
import com.ap.agroplus.Activity.UserAccountActivity;
import com.ap.agroplus.Activity.ViewImageActivity;
import com.ap.agroplus.information.Products;

/**
 * Created by sanniAdewale on 11/04/2017.
 */

public class Options {

    Context context;

    public Options(Context context) {
        this.context = context;
    }

    public void UserAccount(Products products) {
        Intent intent = new Intent(context, UserAccountActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", products.username);
        bundle.putString("mobile", products.mobile);
        bundle.putString("email", products.email);
        bundle.putString("dp", products.image_dp);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void Categories(Products products) {
        Intent intent = new Intent(context, CategoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("category", products.category);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void FullImage(Products products) {
        Intent intent = new Intent(context, ViewImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username", products.username);
        bundle.putString("caption", products.caption);
        bundle.putString("category", products.category);
        bundle.putString("dp", products.image_dp);
        bundle.putString("imgs", products.product_image);
        bundle.putString("price", products.price);
        bundle.putString("time", products.time);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public void CallUser(Products products, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + products.mobile));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 10);
            return;
        }
        context.startActivity(intent);
    }

    public void EmailUser(Products products) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{products.email});
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public void Share(Products products) {
        String text = "Check out this farm product from AgroPlus app." +
                "\n Download the app - http://www.google_play_store.com/agroplus" +
                "\n Click the link below to open the product on the app" +
                "\n " + products.product_link + "&id=" + products.id;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        context.startActivity(Intent.createChooser(intent, "Share"));


    }
}
