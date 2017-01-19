package com.thathustudio.spage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Post  implements Parcelable{
    private int id;
    private int userId;
    private String content;
    private int rating;


    public Post(int id, int userId, String content, int rating) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    protected Post(Parcel in) {

        this.id = in.readInt();
        this.userId = in.readInt();
        this.content = in.readString();
        this.rating = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.userId);
        dest.writeString(this.content);
        dest.writeInt(this.rating);
    }
}
