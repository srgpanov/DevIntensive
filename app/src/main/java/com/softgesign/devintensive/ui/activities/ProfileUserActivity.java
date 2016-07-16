package com.softgesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.storage.models.UserDTO;
import com.softgesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softgesign.devintensive.utils.ConstantManager;
import com.softgesign.devintensive.utils.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends  BaseActivity {
    @BindView(R.id.toolbar)Toolbar mToolbar;
    @BindView(R.id.user_photo_img)ImageView mProfileImage;
    @BindView(R.id.about_et)TextView mUserBio;
    @BindView(R.id.rating_value_txt)TextView mUserRating;
    @BindView(R.id.code_lines_value_txt)TextView mUserLineCodes;
    @BindView(R.id.project_value_txt)TextView mUserProject;
    @BindView(R.id.repozitories_list)ListView mRepoListView;
    @BindView(R.id.collapsing_toolbar)CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_coordinator_container)CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);

        setupToolbar();
        initProfileData();
        Utility.setListViewHeightBasedOnChildren(mRepoListView);
    }
    private void setupToolbar(){
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }
    private void initProfileData(){
        UserDTO userDTO=getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);
        final List<String> repositories=userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter= new RepositoriesAdapter(this,repositories);
        mRepoListView.setAdapter(repositoriesAdapter);
        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView uri =(TextView)view.findViewById(R.id.repozitoriy_item_txt);
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+uri.getText().toString()));
                startActivity(browseIntent);
            }
        });



        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserLineCodes.setText(userDTO.getCodeLines());
        mUserProject.setText(userDTO.getProjects());
        mCollapsingToolbarLayout.setTitle(userDTO.getFullName());
        Picasso.with(this)
                .load(userDTO.getPhoto())
                .error(R.drawable.header_bg)
                .placeholder(R.drawable.header_bg)
                .into(mProfileImage);
    }
}
