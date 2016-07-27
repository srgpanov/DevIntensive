package com.softgesign.devintensive.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.softgesign.devintensive.R;
import com.softgesign.devintensive.data.manegers.DataManager;
import com.softgesign.devintensive.data.storage.models.User;
import com.softgesign.devintensive.ui.views.AspectRatioImageView;
import com.softgesign.devintensive.utils.ConstantManager;
import com.softgesign.devintensive.utils.ScreenResolution;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;

import java.util.List;

/**
 * Created by Пан on 14.07.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> implements Filterable {
    private static final String TAG = ConstantManager.TAG_PREFIX +" UsersAdapter";
    private List<User> mUsers;
    private List<User> mClearUsers;
    private Context mContext;
    private UserViewHolder.CustomClickListener mCustomClickListener;
    private CustomFilter mFilter;


    public UsersAdapter(List<User> users, UserViewHolder.CustomClickListener customClickListener) {
        mUsers = users;
        mClearUsers=mUsers;
        this.mCustomClickListener=customClickListener;
        mFilter = new CustomFilter(UsersAdapter.this);
    }

    @Override
    public UsersAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list,parent,false);
        return new UserViewHolder(convertView, mCustomClickListener);
    }

    @Override
    public void onBindViewHolder(final UsersAdapter.UserViewHolder holder, int position) {
        final User user = mUsers.get(position);
        final String userPhoto;
        if(user.getPhoto().isEmpty()){
            userPhoto = "null";
            Log.e(TAG,"onBindViewHolder: user with name" + user.getFullName()+ "has empty name");
        }else {
            userPhoto=user.getPhoto();
        }
        try {
            DataManager.getInstance().getPicasso()
                    .load(userPhoto)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .resize(ScreenResolution.getWidthDisplay(mContext), getHeightForPhoto(ScreenResolution.getHeightDisplay(mContext)))
                    .centerCrop()
                    .onlyScaleDown()
                    .placeholder(holder.mDummy)
                    .error(holder.mDummy)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.userPhoto, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG," load from cache");
                        }

                        @Override
                        public void onError() {
                            DataManager.getInstance().getPicasso()
                                    .load(userPhoto)
                                    .placeholder(holder.mDummy)
                                    .resize(ScreenResolution.getWidthDisplay(mContext), getHeightForPhoto(ScreenResolution.getHeightDisplay(mContext)))
                                    .error(holder.mDummy)
                                    .into(holder.userPhoto, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.d(TAG, "could not fetch image");
                                        }
                                    });

                        }
                    });
        }
        catch (Exception e){
            Toast.makeText(mContext,"Erorr",Toast.LENGTH_LONG).show();
        }
        holder.mFullName.setText(user.getFullName());
        holder.mRating.setText(String.valueOf(user.getRating()));
        holder.mCodesLines.setText(String.valueOf(user.getCodeLines()));
        holder.mProject.setText(String.valueOf(user.getProjects()));
        if(user.getBio()==null||user.getBio().isEmpty()){
            holder.mBio.setVisibility(View.GONE);
        }else {
            holder.mBio.setVisibility(View.VISIBLE);
            holder.mBio.setText(user.getBio());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected AspectRatioImageView userPhoto;
        protected TextView mFullName,mCodesLines,mProject,mRating,mBio;
        protected Button mShowButton;
        private CustomClickListener mListener;
        protected Drawable mDummy;


        public UserViewHolder(View itemView, CustomClickListener customClickListener) {
            super(itemView);
            this.mListener=customClickListener;

            userPhoto=(AspectRatioImageView)itemView.findViewById(R.id.user_photo_asp_img);
            mFullName=(TextView)itemView.findViewById(R.id.user_full_name_txt);
            mCodesLines=(TextView)itemView.findViewById(R.id.user_codes_lines_txt);
            mRating=(TextView)itemView.findViewById(R.id.user_rating_txt);
            mProject=(TextView)itemView.findViewById(R.id.user_project_txt);
            mBio=(TextView)itemView.findViewById(R.id.bio_txt);
            mShowButton=(Button)itemView.findViewById(R.id.more_info_btn);
            mShowButton.setOnClickListener(this);
            mDummy=userPhoto.getContext().getResources().getDrawable(R.drawable.header_bg);
        }

        @Override
        public void onClick(View view) {
            if(mListener!=null){
                mListener.onUserClickListener(getAdapterPosition());
            }

        }

        public interface CustomClickListener{
            void onUserClickListener(int position);
        }


    }
    public class CustomFilter extends Filter{
        private UsersAdapter mAdapter;

        public CustomFilter(UsersAdapter adapter) {
            super();
            mAdapter = adapter;
        }

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
           // Toast.makeText(mContext,String.valueOf(mClearUsers.size()),Toast.LENGTH_LONG).show();
            mUsers.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                mUsers.addAll(mClearUsers);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (final User users : mClearUsers) {
                    if (users.getFullName().toLowerCase().startsWith(filterPattern)) {
                        mUsers.add(users);
                    }
                }
            }
            results.values = mUsers;
            results.count = mUsers.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private int getHeightForPhoto(int displayHeight){
        double newHeight=displayHeight/1.78;
        return (int)newHeight;
    }


  // public class CustomFilter extends Filter {
  //     private UsersAdapter mAdapter;

  //     public CustomFilter(UsersAdapter adapter) {
  //         super();
  //         this.mAdapter = adapter;
  //     }

  //     @Override
  //     protected FilterResults performFiltering(CharSequence charSequence) {
  //         return null;
  //     }

  //     @Override
  //     protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

  //     }
  // }

}
