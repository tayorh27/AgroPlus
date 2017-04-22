package com.ap.agroplus;

import android.content.Context;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ap.agroplus.information.User;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static void SendNotification(User user, String title, String message, JSONArray jsonArray1) {
        JSONObject object = new JSONObject();

        JSONArray jsonArray;
        jsonArray = new JSONArray();
        try {
            jsonArray.put(0, "Active Users");
            jsonArray.put(1, "Inactive Users");
            jsonArray.put(2, "Engaged Users");

            JSONObject object_content = new JSONObject();
            object_content.put("en", "English Message");
            JSONObject object_data = new JSONObject();
            object_data.put(title, message);
            object.put("included_segments", jsonArray.toString());
            object.put("contents", object_content.toString());
            object.put("data", object_data.toString());
            object.put("small_icon", user.image_path);
            object.put("large_icon", user.image_path);
            object.put("big_picture", jsonArray.getString(0));
        } catch (JSONException e) {
            Log.e("push noti", e.toString());
        }
        OneSignal.postNotification(object, new OneSignal.PostNotificationResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.e("push success", "done");
            }

            @Override
            public void onFailure(JSONObject response) {
                Log.e("push fail", "failed");
            }
        });
    }
}
