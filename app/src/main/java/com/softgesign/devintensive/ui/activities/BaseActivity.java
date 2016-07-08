package com.softgesign.devintensive.ui.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.utils.ConstantManager;

public class BaseActivity extends AppCompatActivity{
    static final String TAG = ConstantManager.TAG_PREFIX+"BaseActivity";
    protected ProgressDialog mProgressDialog;

    public void showProgress(){
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(this, R.style.custom_dialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }else {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_splash);
        }
    }
    public void hideProgress(){
        if(mProgressDialog!=null){
            if (mProgressDialog.isShowing()){
                mProgressDialog.hide();
            }
        }
    }
    public void showEror(String message, Exception error){
        showToast(message);
        Log.e(TAG, String.valueOf(error));
    }
    public void showToast(String message){
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}