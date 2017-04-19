package com.ap.agroplus;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by sanniAdewale on 07/04/2017.
 */

public interface ApiConfig {

    @Multipart
    @POST("img_uploads.php")
    Call uploadFile(@Part MultipartBody.Part file, @Part("file") RequestBody name);

    @Multipart
    @POST("upload.php")
    Call<ServerResponse> uploadDisplayImage(@Part MultipartBody.Part file1);

    @Multipart
    @POST("img_uploads1.php")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1);

    @Multipart
    @POST("img_uploads2.php")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2);

    @Multipart
    @POST("img_uploads3.php")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
                                       @Part MultipartBody.Part file2,
                                       @Part MultipartBody.Part file3);


}
