package com.softgesign.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.utils.ConstantManager;
import com.softgesign.devintensive.utils.Validator;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private boolean mCurrentEditMode = false;

    private DataManager mDataManager;
    //иницлизируем вьюхи используя  баттеркнайф
    @BindView(R.id.main_coordinator_container)CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)DrawerLayout mDrawerLayout;
    @BindView(R.id.fab)FloatingActionButton mFab;
    @BindView(R.id.profile_placeholder)RelativeLayout mProfilePlaceholder;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.appbar_layout)AppBarLayout mAppBarLayout;
    @BindView(R.id.user_photo_img)ImageView mProfileImage;
    @BindView(R.id.make_call_img)ImageView mMakeCall;
    @BindView(R.id.send_mail_img)ImageView mSendMail;
    @BindView(R.id.go_vk_img)ImageView mGoVk;
    @BindView(R.id.go_git_img)ImageView mGoGit;
    @BindView(R.id.phone_et)EditText mUserPhone;
    @BindView(R.id.mail_et)EditText mUserEmail;
    @BindView(R.id.vk_et)EditText mUserVk;
    @BindView(R.id.repozitoriy_et)EditText mUserGit;
    @BindView(R.id.about_et)EditText mUserAbout;
    @BindView(R.id.rating_value_txt)TextView mUserRatingValue;
    @BindView(R.id.code_lines_value_txt)TextView mUserCodeLines;
    @BindView(R.id.project_value_txt)TextView mUserProjectValue;
    @BindViews({R.id.rating_value_txt,R.id.code_lines_value_txt,R.id.project_value_txt})
    List<TextView>mUserValuesViews;
    TextView mDrawerLayoutUserName;
    TextView mDrawerLayoutEmail;


    @BindViews({R.id.phone_et, R.id.mail_et,R.id.vk_et,R.id.repozitoriy_et,R.id.about_et})
    List<EditText> mUserInfoViews;

    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private AppBarLayout.LayoutParams mAppBarParams = null;

    @Override//временно сохраняем введённые данные
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }
    @Override// основной метод активити,
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        ButterKnife.bind(this);


        mFab.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);
        mMakeCall.setOnClickListener(this);
        mSendMail.setOnClickListener(this);
        mGoVk.setOnClickListener(this);
        mGoGit.setOnClickListener(this);

        mDataManager = DataManager.getInstance();

        if (savedInstanceState == null) {
        } else {
            mCurrentEditMode = savedInstanceState.getBoolean(ConstantManager.EDIT_MODE_KEY, false);
            changeEditMode(mCurrentEditMode);
        }
        setupToolbar();
        setupDrawer();
        initUserFields();// загружаем пользовательские данные
        initUserInfoValue();

        Picasso.with(this)// загружаем картинку в колапсинг тулбар
                .load(mDataManager.getPreferencesManager().loadUserPhoto())
                .placeholder(R.drawable.user_photo)
                .into(mProfileImage);
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
        if (validateUserInfoValues(false)) {
            saveUserFields();
        }
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

    @Override//обрабатывеаем нажатия кнопок
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
              if (!mCurrentEditMode) {
                  changeEditMode(true);
                  mCurrentEditMode = true;
              } else {
                  if (validateUserInfoValues(true)){
                    changeEditMode(false);
                    mCurrentEditMode = false;
                  }
              }
                break;
            case R.id.profile_placeholder:
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case  R.id.make_call_img:
                makePhoneCall();
                break;
            case R.id.send_mail_img:
                sendMail();
                break;
            case R.id.go_vk_img:
                goVk();
                break;
            case R.id.go_git_img:
                goGit();
                break;
        }
    }
    @Override//обрабатываем нажатие кнопки "бутерброда" в тулбаре
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override//закрываем дровер лэйаут ри нажатии клавиши назад
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override//обрабатываем ответы при запросе к еамере и галлерее
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if (resultCode == RESULT_OK && data != null) {
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if (resultCode == RESULT_OK && mPhotoFile != null) {
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
        }
    }
    @Override//создаём диалог выбора загрузки изображения в колапсинг тулбар
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {getString(R.string.user_profile_dialog_gallery), getString(R.string.user_profile_dialog_camera), getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.user_profile_dialog_title);
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int choiceItem) {
                        switch (choiceItem) {
                            case 0:
                                loadPhotoFromGallery();
                                //                                   showSnackBar("Загрузить из галлереи");
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                //                                   showSnackBar("Сделать снимок");
                                break;
                            case 2:
                                dialogInterface.cancel();
                                //                                  showSnackBar("Отмена");
                                break;
                        }
                    }
                });
                return builder.create();

            default:
                return null;
        }
    }
    @Override//обрабатываем полученные разрешения
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstantManager.CAMERA_PERMISSION_REQUEST_CODE && grantResults.length == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO: обработать разрешения
            }
            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                //TODO: обработать разрешения
            }
        }
    }
    private void insertProfileImage(Uri selectedImage) {//метод вставки картинки
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getPreferencesManager().saveUserPhoto(selectedImage);
    }
    public void showSnackBar(String message) {//показать снэк бар
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }
    private void setupToolbar() {//иницилизируем тулбар
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mAppBarParams = (AppBarLayout.LayoutParams) mCollapsingToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void setupDrawer() {//иницилизируем навигэйшн дровер
        final NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    showSnackBar(item.getTitle().toString());
                    item.setChecked(true);
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
            View header=mNavigationView.getHeaderView(0);

            mDrawerLayoutUserName=(TextView) header.findViewById(R.id.user_name_txt);
            mDrawerLayoutEmail=(TextView)header.findViewById(R.id.user_name_txt);
            List<String> userData = mDataManager.getPreferencesManager().loadUserDrawerHeaderData();
            mDrawerLayoutEmail.setText(userData.get(0));
            String userName=userData.get(2)+" "+userData.get(1);
            mDrawerLayoutUserName.setText(userName);
        }
    }
    private void changeEditMode(boolean mode) {//меняем режим редактирования, параметр mode - включение/выключение режима
        if (mode) {
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
            }
            mUserPhone.requestFocus();//переводим фокус на поле телефона при включении режима редактирорвания
            mUserPhone.setSelection(mUserPhone.getText().length());//устанавливаем курсор ввода в конец строки
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,1);//показываем клавиатуру
            mFab.setImageResource(R.drawable.ic_check_black_24dp);
            showProfilePlaceholder();
            lockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
        } else {
            for (EditText userValue : mUserInfoViews) {
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
            }
            mFab.setImageResource(R.drawable.ic_create_black_24dp);
            saveUserFields();
            hideProfilePlaceholder();
            unlockToolbar();
            mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
        }
    }
    private void initUserFields() {//загружаем пользовательские данные
        List<String> userData = mDataManager.getPreferencesManager().loadUserDataProfile();
        for (int i = 0; i < userData.size(); i++) {
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }
    private void saveUserFields() {//сохраняем пользовательские данные
        List<String> userData = new ArrayList<>();
        for (EditText userFieldsView : mUserInfoViews) {
            userData.add(userFieldsView.getText().toString());
        }
        mDataManager.getPreferencesManager().saveUserProfileData(userData);
    }
    private void initUserInfoValue(){
        List<String>userData=mDataManager.getPreferencesManager().loadUserProfileValues();
        for (int i = 0; i <userData.size() ; i++) {
            mUserValuesViews.get(i).setText(userData.get(i));
        }
    }
        private void loadPhotoFromGallery() {//загружаем фото из галлереи
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }
    private void loadPhotoFromCamera() {//делаем фото с камеры
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                //TODO:обработать ошибку
            }
            if (mPhotoFile != null) {
                //TODO: передать файл в интент}
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
            }, ConstantManager.CAMERA_PERMISSION_REQUEST_CODE);
            Snackbar.make(mCollapsingToolbarLayout, "Для корректной работы, необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }
    private void hideProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.GONE);
    }
    private void showProfilePlaceholder() {
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }
    private void lockToolbar() {//блокируем тулбар
        mAppBarLayout.setExpanded(true, true);
        mAppBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }
    private void unlockToolbar() {//разблокируем тулбар
        mAppBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mAppBarParams);
    }
    private File createImageFile() throws IOException {// создаем пустой файл для записи битмапы с фотокамеры
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return image;
    }
    public void openApplicationSettings() {//открываем настройки приложения
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }
    public void makePhoneCall() {//метод совершения звонка
        Intent makeCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mUserPhone.getText().toString()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(makeCall);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, ConstantManager.REQUEST_CALL_PHONE);
            Snackbar.make(mCollapsingToolbarLayout, "Для корректной работы, необходимо дать требуемые разрешения", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openApplicationSettings();
                        }
                    }).show();
        }
    }
    public void sendMail(){//метод дотправки емэила
        Intent sendMailIntent = new Intent(Intent.ACTION_SENDTO);
        sendMailIntent.setData(Uri.parse("mailto:"+mUserEmail.getText().toString()));
        startActivity(Intent.createChooser(sendMailIntent,"Отправить через") );
    }
    private void openBrowser (String url){//метод перехода  по ссылке
        Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+url));
        startActivity(browseIntent);
    }
    public void goVk(){
        openBrowser(mUserVk.getText().toString());
    }
    public void goGit(){
        openBrowser(mUserGit.getText().toString());
    }
    private boolean validateUserInfoValues(boolean showMsg) {//валидация введенных данных

        String userPhone = Validator.getValidatedPhone(mUserPhone.getText().toString());
        if (userPhone != null) {
            mUserPhone.setText(Validator.formatRegularPhone(userPhone));
        } else {
            if (showMsg) {
                mUserPhone.requestFocus();
                mUserPhone.setError(getString(R.string.validation_msg_phone));
            }
            return false;
        }

        String userMail = Validator.getValidatedEmail(mUserEmail.getText().toString());
        if (userMail != null) {
            mUserEmail.setText(userMail);
        } else {
            if (showMsg) {
                mUserEmail.requestFocus();
                mUserEmail.setError(getString(R.string.validation_msg_email));
            }
            return false;
        }

        String userVk = Validator.getValidatedVkUrl(mUserVk.getText().toString());
        if (userVk != null) {
            mUserVk.setText(userVk);
        } else {
            if (showMsg) {
                mUserVk.requestFocus();
                mUserVk.setError(getString(R.string.validation_msg_vk));
            }
            return false;
        }

        String userGit = Validator.getValidatedGitUrl(mUserGit.getText().toString());
        if (userGit != null) {
            mUserGit.setText(userGit);
        } else {
            if (showMsg) {
                mUserGit.requestFocus();
                mUserGit.setError(getString(R.string.validation_msg_git));
            }
            return false;
        }
        return true;
    }
}