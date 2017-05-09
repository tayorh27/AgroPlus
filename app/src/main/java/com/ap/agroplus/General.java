package com.ap.agroplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ap.agroplus.information.User;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;
import java.util.Scanner;

import id.zelory.compressor.Compressor;

/**
 * Created by sanniAdewale on 10/03/2017.
 */

public class General {

    Context context;
    MaterialDialog materialDialog;

    public General(Context context) {
        this.context = context;
    }

    public static void SendNotification(User user, String title, String message, JSONArray jsonArray1) {
        final JSONObject object = new JSONObject();

        JSONArray jsonArray;
        jsonArray = new JSONArray();
        try {
            jsonArray.put(0, "All");
            //jsonArray.put(1, "Inactive Users");
            //jsonArray.put(2, "Engaged Users");

            JSONObject object_content = new JSONObject();
            object_content.put("en", "English Message");
            JSONObject object_data = new JSONObject();
            object_data.put(title, message);
            object.put("app_id", AppConfig.APP_ID);
            object.put("included_segments", jsonArray.toString());
            object.put("contents", object_content.toString());
            object.put("data", object_data.toString());
            object.put("small_icon", user.image_path);
            object.put("large_icon", user.image_path);
            object.put("big_picture", jsonArray1.getString(0));
        } catch (JSONException e) {
            Log.e("push noti", e.toString());
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonResponse;

                    URL url = new URL("https://onesignal.com/api/v1/notifications");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setUseCaches(false);
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestProperty("Authorization", "Basic OTJlZTVkODEtN2QzMy00MjE1LWI4NzItZDczYTJhNjE1YTgy");
                    con.setRequestMethod("POST");

                    String strJsonBody = object.toString();


                    Log.e("Notification", "strJsonBody:\n" + strJsonBody);

                    byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                    con.setFixedLengthStreamingMode(sendBytes.length);

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(sendBytes);

                    int httpResponse = con.getResponseCode();
                    Log.e("Notification", "httpResponse: " + httpResponse);

                    if (httpResponse >= HttpURLConnection.HTTP_OK
                            && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                        Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                        scanner.close();
                    } else {
                        Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                        scanner.close();
                    }
                    Log.e("Notification", "jsonResponse:\n" + jsonResponse);

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }).start();
    }

    public static String CopyTo(String file, String username) {
        String new_file_path = "";
        FileOutputStream fileOutputStream = null;
        try {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File myDir1 = new File(root + "/AgroPlus/");
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            File new_image_file = new File(myDir1.toString() + "/img_" + username + "_" + day + "" + (month + 1) + "" + year + "_" + new Random().nextInt(93564517) + ".png");

            String dest = new_image_file.getPath();
            new_file_path = dest;
            if (new_image_file.exists()) {
                new_image_file.delete();
            }
            fileOutputStream = new FileOutputStream(dest);
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Log.e("normal exception", ex.toString());
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new_file_path;
    }

    public static String CopyToAgro(String file, String username) {
        String new_file_path = "";
        FileOutputStream fileOutputStream = null;
        try {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AgroPlus/").toString();
            File myDir1 = new File(root);
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);
            File new_image_file = new File(myDir1.toString() + "/img_product_" + username + "_" + day + "" + (month + 1) + "" + year + "_" + new Random().nextInt(93564517) + ".png");

            String dest = new_image_file.getPath();
            new_file_path = dest;
            if (new_image_file.exists()) {
                new_image_file.delete();
            }
            fileOutputStream = new FileOutputStream(dest);
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);


        } catch (FileNotFoundException e) {
            Log.e("file not found", e.toString());
        } catch (Exception ex) {
            Log.e("normal exception", ex.toString());
        }finally {
            if(fileOutputStream != null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new_file_path;
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

    public String CompressImageForDp(String file_path) {
        String returnedPath = "";
        File comFile = new File(file_path);
        File al = new Compressor.Builder(context)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES + "/AgroPlus/Dp").getAbsolutePath())
                .build()
                .compressToFile(comFile);
        returnedPath = al.getPath();
        return returnedPath;
    }

    public String CompressImageForProduct(String file_path) {
        String returnedPath = "";
        File comFile = new File(file_path);
        File al = new Compressor.Builder(context)
                .setMaxWidth(640)
                .setMaxHeight(480)
                .setQuality(100)
                .setCompressFormat(Bitmap.CompressFormat.PNG)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES + "/AgroPlus/Products").getAbsolutePath())
                .build()
                .compressToFile(comFile);
        returnedPath = al.getPath();
        return returnedPath;
    }

    public void SaveImage(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/AgroPlus/Products/meee.png").toString();

                try {
                    URL url = new URL(path);
                    InputStream inputStream = url.openStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(root);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
