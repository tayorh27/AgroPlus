package com.ap.agroplus;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class General {

    Context context;
    MaterialDialog materialDialog;

    public General(Context context) {
        this.context = context;
    }

    public void showProgress(String text) {
        materialDialog = new MaterialDialog.Builder(context)
                .content(text)
                .progress(true, 0)
                .show();
    }

    public void dismissProgress() {
        materialDialog.dismiss();
    }

    public void showAlert(String text) {
        new MaterialDialog.Builder(context)
                .title("AgroPlus")
                .content(text)
                .positiveText("OK")
                .show();
    }

    public void displayDialog(String text) {
        materialDialog = new MaterialDialog.Builder(context)
                .content(text)
                .canceledOnTouchOutside(false)
                .cancelable(false)
                .progress(true, 0)
                .show();
    }

    public void dismissDialog() {
        materialDialog.dismiss();
    }

    public void error(String text) {
        new MaterialDialog.Builder(context)
                .title("Error")
                .content(text)
                .positiveText("OK")
                .show();
    }

    public void success(String text) {
        new MaterialDialog.Builder(context)
                .title("Success")
                .titleColor(context.getResources().getColor(R.color.colorAccent))
                .content(text)
                .contentColor(context.getResources().getColor(R.color.colorAccent))
                .positiveText("OK")
                .positiveColor(context.getResources().getColor(R.color.colorAccent))
                .show();
    }
}
