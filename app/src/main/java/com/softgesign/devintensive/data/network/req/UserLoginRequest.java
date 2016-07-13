package com.softgesign.devintensive.data.network.req;

/**
 * Created by Пан on 12.07.2016.
 */
public class UserLoginRequest {
    private String email;
    private String password;

    public UserLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
