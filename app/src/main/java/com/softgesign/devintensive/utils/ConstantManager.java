package com.softgesign.devintensive.utils;

//константы используемые в приложении
public interface ConstantManager {
    String TAG_PREFIX="DEV";
    String TAG =TAG_PREFIX + "Main Activity";
    String EDIT_MODE_KEY = "EDIT_MODE_KEY";

    String USER_PHONE_KEY="USER_KEY_1";
    String USER_MAIL_KEY="USER_KEY_2";
    String USER_VK_KEY="USER_KEY_3";
    String USER_GIT_KEY="USER_KEY_4";
    String USER_ABOUT_KEY="USER_KEY_5";
    String USER_PHOTO_KEY="USER_PHOTO_KEY";

    int LOAD_PROFILE_PHOTO=1;
    int REQUEST_CAMERA_PICTURE=99;
    int REQUEST_GALLERY_PICTURE=100;
    int PERMISSION_REQUEST_SETTINGS_CODE= 101;
    int CAMERA_PERMISSION_REQUEST_CODE=102;
    int REQUEST_CALL_PHONE = 103;
}