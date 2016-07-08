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
    private static final String[] USER_FIELDS={ConstantManager.USER_PHONE_KEY, ConstantManager.USER_MAIL_KEY, ConstantManager.USER_VK_KEY, ConstantManager.USER_GIT_KEY, ConstantManager.USER_ABOUT_KEY};

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
    public List<String> loadUserDataProfile(){
        List<String> userFields=new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "+79288439703"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_MAIL_KEY, "srgpanov@yandex.ru"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "vk.com/id370865420"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "github.com/srgpanov"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, "null"));
        return userFields;
    }
    public void saveUserPhoto (Uri uri){
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY,uri.toString());
        editor.apply();
    }
    public Uri  loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softgesign.devintensive/drawable/user_photo.JPG"));
    }
}
