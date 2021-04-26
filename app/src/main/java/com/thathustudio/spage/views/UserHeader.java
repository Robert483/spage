package com.thathustudio.spage.views;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thathustudio.spage.R;

/**
 * Created by Phung on 18/12/2016.
 */

public class UserHeader extends LinearLayout {

    private ImageView ivPhoto;
    private TextView tvUserName;
    private TextView tvTime;

    public UserHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setImage(String url){
        Glide.with(getContext()).load(url).into(ivPhoto);
    }
    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.UserHeader, 0, 0);
        try {
            //Inflate the root ViewGroup
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.custom_user_header, this);
            ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
            tvUserName = (TextView) findViewById(R.id.tvUserName);
            tvTime = (TextView) findViewById(R.id.tvTime);

            //Set user's photo
            Drawable drawable = typedArray.getDrawable(R.styleable.UserHeader_photo);
            if (drawable != null) {
                ivPhoto.setBackground(drawable);
            }

            //Set user's name
            String userName = typedArray.getString(R.styleable.UserHeader_username);
            if(userName != null) {
                tvUserName.setText(userName);
            }

            //Set header's time
            String time = typedArray.getString(R.styleable.UserHeader_time);
            if(time != null) {
                tvTime.setText(time);
            }
        }
        finally {
            typedArray.recycle();
        }
    }

    //region Getters/Setters
    public void setPhoto(Drawable drawable){
        ivPhoto.setBackground(drawable);
    }

    public String getUserName(){
        return tvUserName.getText().toString();
    }

    public void setUserName(String userName){
        tvUserName.setText(userName);
    }

    public String getTime(){
        return tvTime.getText().toString();
    }

    public void setTime(String time){
        tvTime.setText(time);
    }
    //endregion
}
