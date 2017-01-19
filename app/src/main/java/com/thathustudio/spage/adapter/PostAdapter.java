package com.thathustudio.spage.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.thathustudio.spage.R;
import com.thathustudio.spage.firebase.Storage;
import com.thathustudio.spage.fragments.PostsFragment;
import com.thathustudio.spage.interfaces.OnClickImageListener;
import com.thathustudio.spage.interfaces.OnCommentClickListener;
import com.thathustudio.spage.interfaces.OnCreatePostClickListener;
import com.thathustudio.spage.interfaces.OnLikeClickListener;
import com.thathustudio.spage.interfaces.OnLoadmorePostListener;
import com.thathustudio.spage.interfaces.OnMorePostItemClickListener;
import com.thathustudio.spage.interfaces.OnRemovePhotoListener;
import com.thathustudio.spage.interfaces.OnUserProfileClickListener;
import com.thathustudio.spage.model.Post;
import com.thathustudio.spage.model.User;
import com.thathustudio.spage.util.DateTimeUtil;
import com.thathustudio.spage.util.LikeUtil;

import java.util.List;

/**
 * Created by apple on 1/5/17.
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> lstPost;
    private Context mContext;


    final int HEADER = 0;
    final int ROW = 1;




    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        public AppCompatImageButton btnClose;
        public AppCompatImageView imvProfileIcon,imvImage,imvLike;
        AppCompatTextView tvUserName,tvTime,tvContent,tvComment,tvLike;
        AppCompatImageButton btnMore;
        ProgressBar prbLoadingLike;

        public MyViewHolder(View view) {
            super(view);
            tvComment = (AppCompatTextView)view.findViewById(R.id.tvComment);
            tvLike = (AppCompatTextView)view.findViewById(R.id.tvLike);
            tvUserName = (AppCompatTextView)view.findViewById(R.id.tvUserName);
            tvContent = (AppCompatTextView)view.findViewById(R.id.tvContent);
            tvTime = (AppCompatTextView)view.findViewById(R.id.tvTime);
            imvProfileIcon = (AppCompatImageView)view.findViewById(R.id.imvProfileIcon);
            imvLike = (AppCompatImageView)view.findViewById(R.id.imvLike);
            imvImage = (AppCompatImageView)view.findViewById(R.id.imvImage);
            btnMore = (AppCompatImageButton)view.findViewById(R.id.btnMore);
            prbLoadingLike = (ProgressBar)view.findViewById(R.id.prbLoadingLike);


            tvComment.setOnClickListener(this);
            tvUserName.setOnClickListener(this);
            imvProfileIcon.setOnClickListener(this);
            btnMore.setOnClickListener(this);
            imvImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickImageListener!=null)
                        onClickImageListener.onClickImage(lstPost.get(getAdapterPosition()-1).getImage());
                }
            });

            imvLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onLikeClickListener!=null){
                        Post post = lstPost.get(getAdapterPosition()-1);
                        boolean liked = LikeUtil.isLikedPost(mContext,post.getUserId(),post.getId());
                        onLikeClickListener.onLikeClick(prbLoadingLike,view,getAdapterPosition()-1,!liked);
                    }
                }
            });



        }




        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tvComment:
                {
                    if(onCommentClickListener!=null){
                        onCommentClickListener.onCommentClick(lstPost.get(getAdapterPosition()-1));
                    }
                    break;
                }

                case R.id.tvUserName:
                case R.id.imvProfileIcon:
                {
                    if(onUserProfileClickListener!=null){
                        onUserProfileClickListener.onUserProfileClick(lstPost.get(getAdapterPosition()-1).getUserId());
                    }
                    break;
                }

                case R.id.btnMore:{
                    if(onMorePostItemClickListener!=null){
                        onMorePostItemClickListener.onMorePostItemClick(view,lstPost.get(getAdapterPosition()-1),getAdapterPosition()-1);
                    }
                    break;
                }
            }
        }
    }


    public void setOnClickImageListener(OnClickImageListener onClickImageListener) {
        this.onClickImageListener = onClickImageListener;
    }

    OnClickImageListener onClickImageListener;


    public void setOnUserProfileClickListener(OnUserProfileClickListener onUserProfileClickListener) {
        this.onUserProfileClickListener = onUserProfileClickListener;
    }

    OnUserProfileClickListener onUserProfileClickListener;


    public void setOnCommentClickListener(OnCommentClickListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }

    OnCommentClickListener onCommentClickListener;


    public class MyHeaderViewHolder extends RecyclerView.ViewHolder {
        public AppCompatEditText edtPost;
//        public AppCompatImageView imvPhoto;

        public MyHeaderViewHolder(View view) {
            super(view);
//            btnClose = (AppCompatImageButton)view.findViewById(R.id.btnClose);
//            imvPhoto = (AppCompatImageView)view.findViewById(R.id.imvPhoto);
            edtPost = (AppCompatEditText)view.findViewById(R.id.edtPost);
            edtPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onCreatePostClickListener!=null){
                        onCreatePostClickListener.onCreatePostClick(view);
                    }
                }
            });

        }
    }

    public void setOnMorePostItemClickListener(OnMorePostItemClickListener onMorePostItemClickListener) {
        this.onMorePostItemClickListener = onMorePostItemClickListener;
    }

    OnMorePostItemClickListener onMorePostItemClickListener;



    public PostAdapter(List<Post> lstPost, Context context,boolean isHideHeader) {
        this.lstPost = lstPost;
        this.isHideHeader = isHideHeader;
        mContext = context;
    }

    public void setOnCreatePostClickListener(OnCreatePostClickListener onCreatePostClickListener) {
        this.onCreatePostClickListener = onCreatePostClickListener;
    }

    boolean isHideHeader;

    OnCreatePostClickListener onCreatePostClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType==HEADER){
            final View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.content_create_post, parent, false);
            if(isHideHeader){
                Log.d("Thai","isHideHeader");
                v.setVisibility(View.GONE);
            }

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onCreatePostClickListener!=null){
                        onCreatePostClickListener.onCreatePostClick(v);
                    }
                }
            });

            return new MyHeaderViewHolder(v);
        }else{
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false));
        }
    }



    public void setOnLoadmorePostListener(OnLoadmorePostListener onLoadmorePostListener) {
        this.onLoadmorePostListener = onLoadmorePostListener;
    }

    OnLoadmorePostListener onLoadmorePostListener;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position==HEADER)
            return;

        Post post = lstPost.get(position-1);
//        post.setImage("https://firebasestorage.googleapis.com/v0/b/spage-1f633.appspot.com/o/images%2Fpost%2F0?alt=media&token=bd0be943-79ad-45b4-b92d-758c63d026c4");

        MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.tvLike.setText(post.getRating()+" Like");
        myViewHolder.tvComment.setText(post.getCommentCount()+" Comment");
        myViewHolder.tvUserName.setText(post.getUsername());
        Glide.with(mContext).load(post.getImage()).into(myViewHolder.imvImage);
        if(LikeUtil.isLikedPost(mContext,post.getUserId(),post.getId())){
            myViewHolder.imvLike.setImageResource(R.drawable.liked);
        }else{
            myViewHolder.imvLike.setImageResource(R.drawable.like);
        }

        try
        {
            Storage.displayImage(mContext,myViewHolder.imvProfileIcon,"user/"+post.getUserId());
        }
        catch (Exception e)
        {
            myViewHolder.imvProfileIcon.setImageResource(R.drawable.ic_profile);
            Log.e("Thai", e.getMessage());
        }


        myViewHolder.tvContent.setText(post.getContent());
        myViewHolder.tvTime.setText(formatTime(post.getDate()));



    }

    public void setOnLikeClickListener(OnLikeClickListener onLikeClickListener) {
        this.onLikeClickListener = onLikeClickListener;
    }

    OnLikeClickListener onLikeClickListener;

    String formatTime(int time)
    {
        return DateTimeUtil.timestampToString(time);
    }

    @Override
    public int getItemViewType(int position) {
//        logger.i("getItemViewType position: " + position);
        if (position == HEADER)
            return HEADER;
        else
            return ROW;
    }

    @Override
    public int getItemCount() {
        return lstPost.size()+1;
    }
}
