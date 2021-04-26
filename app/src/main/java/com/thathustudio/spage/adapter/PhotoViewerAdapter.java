package com.thathustudio.spage.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.thathustudio.spage.R;
import com.thathustudio.spage.interfaces.OnRemovePhotoListener;

import java.util.List;

/**
 * Created by apple on 1/5/17.
 */

public class PhotoViewerAdapter extends RecyclerView.Adapter<PhotoViewerAdapter.MyViewHolder> {

    private List<Uri> lstPhoto;
    private Context mContext;

    public void setOnRemovePhotoListener(OnRemovePhotoListener onRemovePhotoListener) {
        this.onRemovePhotoListener = onRemovePhotoListener;
    }

    OnRemovePhotoListener onRemovePhotoListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public AppCompatImageButton btnClose;
        public AppCompatImageView imvPhoto;

        public MyViewHolder(View view) {
            super(view);
            btnClose = (AppCompatImageButton)view.findViewById(R.id.btnClose);
            imvPhoto = (AppCompatImageView)view.findViewById(R.id.imvPhoto);

        }
    }





    public PhotoViewerAdapter(List<Uri> lstPhoto,Context context) {
        this.lstPhoto = lstPhoto;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_viewer, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Uri uri = lstPhoto.get(position);
//        Picasso.with(mContext).load(uri).into(holder.imvPhoto);
        Glide.with(mContext).load(uri)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imvPhoto);
        holder.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onRemovePhotoListener!=null){
                    Log.d("Thai","Remove item at index "+position);
                    onRemovePhotoListener.onRemovePhoto(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstPhoto.size();
    }
}
