package com.softgesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.data.network.req.UserLoginRequest;
import com.softgesign.devintensive.data.network.res.UserModelResponse;
import com.softgesign.devintensive.utils.NetworkStatusCheker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.login_btn)Button mSignInBtn;
    @BindView(R.id.remember_txt)TextView mRememberPasswordTxt;
    @BindView(R.id.login_coordinator_layout)CoordinatorLayout mLoginCoordinatorLayout;
    @BindView(R.id.login_email_et)EditText mLogin;
    @BindView(R.id.login_password_et)EditText mPassword;
    private DataManager mDataManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        mSignInBtn.setOnClickListener(this);
        mRememberPasswordTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_btn:
                signIn();
                break;
            case R.id.remember_txt:
                rememberPassword();
                break;
        }
    }
    private void showSnackBar(String message){
        Snackbar.make(mLoginCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }
    private void rememberPassword(){
        Intent rememberIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }
    private void loginSuccess(UserModelResponse userModel){
        showSnackBar(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveAuthToken(userModel.getData().getToken());
        mDataManager.getPreferencesManager().saveUserId(userModel.getData().getUser().getId());
        saveUserInfoValues(userModel);
        saveUserFields(userModel);
        saveUserFieldsDrawHeader(userModel);

        Intent loginIntent = new Intent(this,MainActivity.class);
        startActivity(loginIntent);
    }
    private void signIn(){
        if(NetworkStatusCheker.isNetworkAvailable(this)){
        Call<UserModelResponse>call = mDataManager.loginUser(new Date().toString(),new UserLoginRequest(mLogin.getText().toString(),mPassword.getText().toString()));
        call.enqueue(new Callback<UserModelResponse>() {
            @Override
            public void onResponse(Call<UserModelResponse> call, Response<UserModelResponse> response) {
                if(response.code()==200){
                    loginSuccess(response.body());
                }else if(response.code()==404){
                    showSnackBar("Неверный логин или пароль");
                }else {
                    showSnackBar("Всё пропало");
                }
            }

            @Override
            public void onFailure(Call<UserModelResponse> call, Throwable t) {
                //TODO: обработать ошибки ретрофита
            }
        });
    }else {
            showSnackBar("Сеть на данный момент недоступна");
        }
    }
    private void saveUserInfoValues(UserModelResponse userModel){
        int[] userValuesInt = {
                userModel.getData().getUser().getProfileValues().getRating(),
                userModel.getData().getUser().getProfileValues().getLinesCode(),
                userModel.getData().getUser().getProfileValues().getProjects(),
        };
        mDataManager.getPreferencesManager().saveUserProfileValues(userValuesInt);

    }
    private void saveUserFields(UserModelResponse userModel){
        List<String> userData = new ArrayList<>();
        userData.add(userModel.getData().getUser().getContacts().getPhone());
        userData.add(userModel.getData().getUser().getContacts().getEmail());
        userData.add(userModel.getData().getUser().getContacts().getVk());
        userData.add(userModel.getData().getUser().getRepositories().getRepositories().get(0).getGit());
        userData.add(userModel.getData().getUser().getPublicInfo().getBio());;
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }
    private void saveUserFieldsDrawHeader(UserModelResponse userModel){
        List<String> userData = new ArrayList<>();
        userData.add(userModel.getData().getUser().getContacts().getEmail());
        userData.add(userModel.getData().getUser().getFirstName());
        userData.add(userModel.getData().getUser().getSecondName());
        mDataManager.getPreferencesManager().saveUserDrawerHeaderData(userData);
    }



}
