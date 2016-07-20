package com.softgesign.devintensive.data.manegers;


import android.content.Context;

import com.softgesign.devintensive.data.network.PicassoCache;
import com.softgesign.devintensive.data.network.RestService;
import com.softgesign.devintensive.data.network.ServiceGenerator;
import com.softgesign.devintensive.data.network.req.UserLoginRequest;
import com.softgesign.devintensive.data.network.res.UserListResponse;
import com.softgesign.devintensive.data.network.res.UserModelResponse;
import com.softgesign.devintensive.data.storage.models.DaoSession;
import com.softgesign.devintensive.data.storage.models.User;
import com.softgesign.devintensive.data.storage.models.UserDao;
import com.softgesign.devintensive.utils.DevIntensiveApplication;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class DataManager {
    private static DataManager INSTANCE=null;
    private PreferencesManager mPreferencesManager;
    private RestService mRestService;
    private Picasso mPicasso;
    private Context mContext;
    private DaoSession mDaoSession;

    public Picasso getPicasso() {
        return mPicasso;
    }

    public DataManager() {
        this.mPreferencesManager = new PreferencesManager();
        this.mRestService= ServiceGenerator.createService(RestService.class);
        this.mContext= DevIntensiveApplication.getContext();
        this.mPicasso=new PicassoCache(mContext).getPicassoInstance();
        this.mDaoSession=DevIntensiveApplication.getDaoSession();
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
    public Call<UserListResponse> getUserListFromNetwork(){
        return mRestService.getUserList();
    }
 //   public Call<UserModelResponse> uploadPhoto(String userId, MultipartBody.Part file){
  //      return mRestService.uploadPhoto(userId,file);
 //   }
    //endregion

    public List<User> getUserListFromDb(){
        List<User> userList =new ArrayList<>();
        try {
            userList=mDaoSession.queryBuilder(User.class)
                    .where(UserDao.Properties.CodeLines.gt(0))
                    .orderDesc(UserDao.Properties.CodeLines)
                    .build()
                    .list();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return userList;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
