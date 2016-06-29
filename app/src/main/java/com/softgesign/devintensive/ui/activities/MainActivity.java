package com.softgesign.devintensive.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.utils.ConstantManeger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private DataManager mDataManager;
    private int mCurrentEditMode=0;
    private static final String TAG = ConstantManeger.TAG_PREFIX+"Main Activity";
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private FloatingActionButton mFab;
    private EditText mUserPhone,mUserEmail, mUserVk, mUserGit, mUserAbout;

    private List<EditText> mUserInfoViews;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ConstantManeger.EDIT_MODE_KEY,mCurrentEditMode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.navigation_drawer);
        mFab=(FloatingActionButton)findViewById(R.id.fab);
        mUserPhone=(EditText)findViewById(R.id.phone_et);
        mUserVk=(EditText)findViewById(R.id.vk_et);
        mUserEmail=(EditText)findViewById(R.id.mail_et);
        mUserGit=(EditText)findViewById(R.id.repozitoriy_et);
        mUserAbout=(EditText)findViewById(R.id.about_et);
        mFab.setOnClickListener(this);
        mDataManager=DataManager.getInstance();


        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserVk);
        mUserInfoViews.add(mUserGit);
        mUserInfoViews.add(mUserEmail);
        mUserInfoViews.add(mUserAbout);

         if (savedInstanceState==null){
 //            showSnackBar("активити запускается впервые");
         }else{
//           showSnackBar("активити уже запускалось");
          mCurrentEditMode=savedInstanceState.getInt(ConstantManeger.EDIT_MODE_KEY,0);
          changeEditMode(mCurrentEditMode);
         }
        setupToolbar();
        setupDrawer();
        loadUserInfoValue();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                showSnackBar("click");
                if(mCurrentEditMode==0){
                    changeEditMode(1);
                    mCurrentEditMode =1;
                }else {
                    changeEditMode(0);
                    mCurrentEditMode =0;
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }


    public void showSnackBar(String message){
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_SHORT).show();
    }
    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setupDrawer() {
        final NavigationView mNavigationView = (NavigationView)findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }
    private void changeEditMode(int mode){
        if(mode == 1){
            for(EditText userValue:mUserInfoViews){
            userValue.setEnabled(true);
            userValue.setFocusable(true);
            userValue.setFocusableInTouchMode(true);}
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
        }else {
            for(EditText userValue:mUserInfoViews){
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
        }}
    private void loadUserInfoValue(){
        List<String> userData=mDataManager.getPreferencesManager().loadUserDataProfile();
        for (int i = 0;i<userData.size();i++){
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }
    private void saveUserInfoValue(){
        List<String>userData = new ArrayList<>();
        for (EditText  userFieldsView: mUserInfoViews) {
            userData.add(userFieldsView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfiledata(userData);
    }
}
