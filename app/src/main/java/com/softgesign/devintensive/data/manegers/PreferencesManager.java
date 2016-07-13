package com.softgesign.devintensive.data.manegers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softgesign.devintensive.utils.ConstantManager;
import com.softgesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Пан on 29.06.2016.
 */
public class PreferencesManager {//класс для управления пользовательскими данными
    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS={
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_MAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_ABOUT_KEY
    };

    private static final String[] USER_VALUES_INT ={
            ConstantManager.USER_RATING_VALUE,
            ConstantManager.USER_CODE_LINES_VALUE,
            ConstantManager.USER_PROJECT_VALUE,
    };


    public PreferencesManager() {
        mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }
    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        for(int i=0;i<USER_FIELDS.length;i++){
            editor.putString(USER_FIELDS[i],userFields.get(i));
        }
        editor.apply();
    }
    public void saveUserDrawerHeaderData(List<String> userFields){
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_MAIL_KEY,userFields.get(0));
        editor.putString(ConstantManager.FIRST_NAME_KEY,userFields.get(1));
        editor.putString(ConstantManager.SECOND_NAME_KEY,userFields.get(2));
        editor.apply();
    }

    public List<String> loadUserDrawerHeaderData(){
        List<String> userValues=new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.FIRST_NAME_KEY,"1"));
        userValues.add(mSharedPreferences.getString(ConstantManager.SECOND_NAME_KEY,"2"));
        return userValues;
    }

    public List<String> loadUserDataProfile(){
        List<String> userFields=new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "0"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, "0"));
        return userFields;
    }
    public List<String> loadUserProfileValues(){
        List<String> userValues=new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_VALUE,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_VALUE,"0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_VALUE,"0"));
        return userValues;
    }
    public void saveUserProfileValues(int[] userValues){
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        for(int i = 0; i< USER_VALUES_INT.length; i++){
            editor.putString(USER_VALUES_INT[i],String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public void saveUserPhoto (Uri uri){
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY,uri.toString());
        editor.apply();
    }
    public Uri  loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softgesign.devintensive/drawable/user_photo.JPG"));
    }
    public void saveAuthToken(String authToken){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOKEN_KEY,authToken);
        editor.apply();
    }
    public String getAuthToken (){
        return mSharedPreferences.getString(ConstantManager.AUTH_TOKEN_KEY,"null");
    }
    public void saveUserId (String userId){
        SharedPreferences.Editor editor=mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY,userId);
        editor.apply();
    }
    public String getUserId (){
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY,"null");
    }

}
