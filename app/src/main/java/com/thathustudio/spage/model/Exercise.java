package com.thathustudio.spage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Exercise implements Parcelable {
    public static final Parcelable.Creator<Exercise> CREATOR = new Parcelable.Creator<Exercise>() {
        @Override
        public Exercise createFromParcel(Parcel source) {
            return new Exercise(source);
        }

        @Override
        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };
    private int id;
    private String name;
    private String content;
    private String difficulty;
    private int subjectId;

    protected Exercise(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.content = in.readString();
        this.difficulty = in.readString();
        this.subjectId = in.readInt();
    }

    public Exercise() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.content);
        dest.writeString(this.difficulty);
        dest.writeInt(this.subjectId);
    }
}
