package com.androidchatapp.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.androidchatapp.Model.UserModel;
import com.androidchatapp.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Admin on 14-07-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<UserModel> chatuserlist;
    private Context context;

    public RecyclerAdapter(Context context, ArrayList<UserModel> chatuserlist) {
        this.chatuserlist = chatuserlist;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_card_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int position) {
        UserModel item = chatuserlist.get(position);
        String username = item.getName();
        viewHolder.userProfile.setText(username);
        Glide.with(context).load(item.getPro_thumbnail()).into(viewHolder.imgThumbnail);
    }

    @Override
    public int getItemCount() {
        return chatuserlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgThumbnail;
        public TextView userProfile;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            userProfile = (TextView) itemView.findViewById(R.id.tv_movie);
        }
    }


}
