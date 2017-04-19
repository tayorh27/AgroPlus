package com.ap.agroplus.extras;

import android.util.Log;

import com.ap.agroplus.AppConfig;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by sanniAdewale on 11/03/2017.
 */

public class UploadImages {

    public static int uploadFile(String picturePath, String php_file) {

        int serverResponseCode = 0;
        String upLoadServerUri = AppConfig.WEB_URL + php_file;
        String filename = picturePath;

        HttpURLConnection conn;
        DataOutputStream dos;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(picturePath);
        if (!sourceFile.isFile()) {
            Log.d("uploadFile", "Source File does not exist");
            return 0;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(picturePath);
            URL url = new URL(upLoadServerUri);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCRYPT", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploaded_file", filename);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + filename + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();
            Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return serverResponseCode;
    }

    public static void Upload(String name, String php_file) {
        String upLoadServerUri = AppConfig.WEB_URL + php_file;
        StringBuilder buffer = new StringBuilder();

        try {
            URL url = new URL(upLoadServerUri);
            HttpURLConnection con = (HttpURLConnection) (url).openConnection();
            con.setRequestMethod("POST");

            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
            con.getOutputStream().write(("uploaded_file=" + name).getBytes());
            InputStream is = con.getInputStream();
            byte[] b = new byte[1024];
            while (is.read(b) != -1)
                buffer.append(new String(b));
            con.disconnect();
            Log.e("UploadImages", buffer.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
