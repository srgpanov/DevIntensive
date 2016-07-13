package com.softgesign.devintensive.data.network.req;

import android.net.Uri;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Пан on 13.07.2016.
 */
public class UploadPhoto {
    public MultipartBody.Part uploadPhoto(Uri uri) {

        File file = new File(uri.getPath());

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        return body;
    }
}
