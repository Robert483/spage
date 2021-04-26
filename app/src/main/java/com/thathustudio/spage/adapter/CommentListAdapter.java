package com.thathustudio.spage.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thathustudio.spage.R;
import com.thathustudio.spage.activities.CommentsActivity;
import com.thathustudio.spage.model.Comment;
import com.thathustudio.spage.util.DateTimeUtil;
import com.thathustudio.spage.views.UserHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Phung on 18/12/2016.
 */

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private List<Comment> commentList = new ArrayList<>();
    private final CommentsActivity activity;



    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public UserHeader userHeader;
        public TextView tvContent;
        public TextView tvRating;
        public ImageButton btnRating;
        public ImageView ivCommentPhoto;

        public CommentViewHolder(View itemView) {
            super(itemView);
            userHeader = (UserHeader) itemView.findViewById(R.id.commentHeader);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            btnRating = (ImageButton) itemView.findViewById(R.id.btnRating);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            ivCommentPhoto = (ImageView) itemView.findViewById(R.id.ivCommentPhoto);

            btnRating.setOnClickListener(this);
        }

        private void updateRating(int currentRating) {
            Comment comment = CommentListAdapter.this.commentList.get(getAdapterPosition());
            comment.setRating(currentRating);
            CommentListAdapter.this.activity.updateComment(comment);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnRating:
                    int currentRating = Integer.parseInt(tvRating.getText().toString());
                    if(!btnRating.isSelected()){
                        currentRating++;
                    }
                    else{
                        currentRating--;
                    }
                    btnRating.setSelected(!btnRating.isSelected());
                    tvRating.setText(String.valueOf(currentRating));

                    //Update to database
                    updateRating(currentRating);
                    break;
            }
        }
    }

    public CommentListAdapter(List<Comment> commentList, CommentsActivity activity) {
        this.commentList = commentList;
        this.activity = activity;
    }

    public void addComment(Comment comment){
        commentList.add(comment);
        notifyItemInserted(commentList.size());
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_comment, parent, false);
        CommentViewHolder viewHolder = new CommentViewHolder(convertView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.userHeader.setUserName(commentList.get(position).getUsername());
        String date = DateTimeUtil.timestampToString(commentList.get(position).getDate());
        holder.userHeader.setTime(date);
        holder.tvContent.setText(commentList.get(position).getContent());
        holder.tvRating.setText(String.valueOf(commentList.get(position).getRating()));
        if(commentList.get(position).getImage() != null){
            holder.ivCommentPhoto.setVisibility(View.VISIBLE);
            Picasso.with(holder.ivCommentPhoto.getContext()).load(commentList.get(position).getImage()).fit().into(holder.ivCommentPhoto);
        }
    }

    @Override
    public int getItemCount() {

        return commentList.size();
    }
}
