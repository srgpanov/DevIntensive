package com.softgesign.devintensive.data.manegers;

import android.content.SharedPreferences;

import com.softgesign.devintensive.utils.ConstantManeger;
import com.softgesign.devintensive.utils.DevIntensiveApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Пан on 29.06.2016.
 */
public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS={ConstantManeger.USER_PHONE_KEY,ConstantManeger.USER_MAIL_KEY,ConstantManeger.USER_VK_KEY,ConstantManeger.USER_GIT_KEY,ConstantManeger.USER_ABOUT_KEY};

    public PreferencesManager() {
        mSharedPreferences = DevIntensiveApplication.getSharedPreferences();
    }
    public void saveUserProfiledata(List<String> userFields){
        SharedPreferences.Editor editor =mSharedPreferences.edit();
        for(int i=0;i<USER_FIELDS.length;i++){
            editor.putString(USER_FIELDS[i],userFields.get(i));
        }
        editor.apply();
    }
    public List<String> loadUserDataProfile(){
        List<String> userFields=new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManeger.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManeger.USER_MAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManeger.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManeger.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManeger.USER_ABOUT_KEY, "null"));
        return userFields;
    }
}
