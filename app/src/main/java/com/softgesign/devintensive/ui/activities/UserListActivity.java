package com.softgesign.devintensive.ui.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.data.storage.models.User;
import com.softgesign.devintensive.data.storage.models.UserDTO;
import com.softgesign.devintensive.ui.adapters.UsersAdapter;
import com.softgesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @BindView(R.id.main_coordinator_container)CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.toolbar)Toolbar mToolbar;
    @BindView(R.id.navigation_drawer)DrawerLayout mNavigationView;
    @BindView(R.id.user_list)RecyclerView mRecyclerView;

    private DataManager mDataManager;
    private UsersAdapter mUsersAdapter;
    private List<User> mUsers;
    private static final String TAG= ConstantManager.TAG_PREFIX+" UserListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        mDataManager = DataManager.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        setupToolbar();
        setupDrawer();
        loadUsersFromDb();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem searchItem=menu.findItem(R.id.search);
        SearchView searchView= (SearchView)MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, UserListActivity.class)));
        searchView.setIconifiedByDefault(false);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            mNavigationView.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
      //  showSnackBar("Текст введён");
        mUsersAdapter.getFilter().filter(newText);
        return false;
    }

    private void showSnackBar(String message){
        Snackbar.make(mCoordinatorLayout,message,Snackbar.LENGTH_LONG).show();
    }

    private void loadUsersFromDb() {
        if(mUsers.size()==0){
            showSnackBar("Список пользователей не может быть загуржен");
        }else {
        mUsers= mDataManager.getUserListFromDb();
        mUsersAdapter=new UsersAdapter(mUsers, new UsersAdapter.UserViewHolder.CustomClickListener() {
                              @Override
                              public void onUserClickListener(int position) {
                                  showSnackBar("Пользователь с индексом "+ position);
                                  UserDTO userDTO = new UserDTO(mUsers.get(position));
                                  Intent profileIntent = new Intent(UserListActivity.this, ProfileUserActivity.class);
                                  profileIntent.putExtra(ConstantManager.PARCELABLE_KEY,userDTO);
                                  startActivity(profileIntent);
                              }
                          });
        mRecyclerView.setAdapter(mUsersAdapter);

  //
    }}

    private void setupDrawer() {
        //TODO: реализовать переход в другую активити при клике по элементу меню
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
