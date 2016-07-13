package com.softgesign.devintensive.data.network;

import com.softgesign.devintensive.data.network.req.UserLoginRequest;
import com.softgesign.devintensive.data.network.res.UserModelResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Пан on 12.07.2016.
 */
public interface RestService {
    @POST("login")
    Call<UserModelResponse> loginUser(@Header("Last-Modified") String lastMod, @Body UserLoginRequest req);


    @Multipart
    @POST("user/{userId}/publicValues/profilePhoto")
    Call<UserModelResponse> uploadPhoto(@Path("userId")String userId, @Part MultipartBody.Part file);

}
