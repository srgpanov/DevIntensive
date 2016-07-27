package com.softgesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.data.network.req.UserLoginRequest;
import com.softgesign.devintensive.data.network.res.UserListResponse;
import com.softgesign.devintensive.data.network.res.UserModelResponse;
import com.softgesign.devintensive.data.storage.models.Repository;
import com.softgesign.devintensive.data.storage.models.RepositoryDao;
import com.softgesign.devintensive.data.storage.models.User;
import com.softgesign.devintensive.data.storage.models.UserDao;
import com.softgesign.devintensive.utils.AppConfig;
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
    private RepositoryDao mRepositoryDao;
    private UserDao mUserDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();
        mUserDao=mDataManager.getDaoSession().getUserDao();
        mRepositoryDao=mDataManager.getDaoSession().getRepositoryDao();

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
        saveUserInDb();

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(LoginActivity.this,UserListActivity.class);
                startActivity(loginIntent);
            }
        }, AppConfig.START_DELAY);


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
    private void saveUserInDb(){
        Call<UserListResponse> call=mDataManager.getUserListFromNetwork();
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {

                    if(response.code()==200){
                        List<Repository> allRepositories = new ArrayList<Repository>();
                        List<User> allUsers=new ArrayList<User>();
                        for(UserListResponse.UserData userRes :response.body().getData()){
                            allRepositories.addAll(getRepoListFromUserRes(userRes));
                            allUsers.add(new User(userRes));
                        }
                        mRepositoryDao.insertOrReplaceInTx(allRepositories);
                        mUserDao.insertOrReplaceInTx(allUsers);
                    }
                    else
                    {
                        showSnackBar("Список пользователей не может быть получен");
                        Log.e(TAG,"onResponse: "+String.valueOf(response.errorBody().source()));
                 //       mUsers=response.body().getData();
                 //       mUsersAdapter=new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                 //           @Override
                 //           public void onUserClickListener(int position) {
                 //               showSnackBar("Пользователь с индексом "+ position);
                 //               UserDTO userDTO = new UserDTO(mUsers.get(position));
                 //               Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                 //               profileIntent.putExtra(ConstantManager.PARCELABLE_KEY,userDTO);
                 //               startActivity(profileIntent);
                 //           }
                 //       });
                 //       mRecyclerView.setAdapter(mUsersAdapter);
                 //  }else if(response.code()==401){
                 //      showSnackBar("401");
                 //  }else {
                 //      showSnackBar("Всё пропало, ошибка" + String.valueOf(response.code()));
                 //  }
                }

            };

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {

            }
        });
    }
    private List<Repository> getRepoListFromUserRes(UserListResponse.UserData userData){
        final  String userId=userData.getId();
        List<Repository> repositories=new ArrayList<>();
        for(UserModelResponse.Repo repo:userData.getRepositories().getRepositories()){
            repositories.add(new Repository(repo,userId));

        }
        return repositories;
    }



}
