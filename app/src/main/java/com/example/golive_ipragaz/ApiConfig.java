package com.example.golive_ipragaz;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

interface ApiConfig {
    @Multipart
    @POST("filepost")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("file") RequestBody name);

//    @Multipart
//    @POST("filepost")
//    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part file1,
//                                       @Part MultipartBody.Part file2);
}
