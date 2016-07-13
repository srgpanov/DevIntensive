package com.softgesign.devintensive.data.manegers;


import com.softgesign.devintensive.data.network.RestService;
import com.softgesign.devintensive.data.network.ServiceGenerator;
import com.softgesign.devintensive.data.network.req.UserLoginRequest;
import com.softgesign.devintensive.data.network.res.UserModelResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE=null;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService= ServiceGenerator.createService(RestService.class);
    }

    public static DataManager getInstance(){
        if(INSTANCE==null){INSTANCE=new DataManager();}
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {

        return mPreferencesManager;
    }

    //region ============ Network=============
    public Call <UserModelResponse> loginUser(String lastModified,UserLoginRequest userLoginRequest){
        return mRestService.loginUser(lastModified,userLoginRequest);
    }
    public Call<UserModelResponse> uploadPhoto(String userId, MultipartBody.Part file){
        return mRestService.uploadPhoto(userId,file);
    }
    //endregion
}
