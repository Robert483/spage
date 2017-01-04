package com.thathustudio.spage.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class Question implements Parcelable {
    public final static int NO_CHOICE_SELECTED = RecyclerView.NO_POSITION;
    public static final Parcelable.Creator<Question> CREATOR = new Parcelable.Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
    private String content;
    private List<String> choices;
    private int userChoice;
    private String answer;
    public String a;
    public String b;
    public String c;
    public String d;

    protected Question(Parcel in) {
        this.content = in.readString();
        this.choices = in.createStringArrayList();
        this.userChoice = in.readInt();
        this.answer = in.readString();
    }

    public Question() {
        this.userChoice = NO_CHOICE_SELECTED;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public boolean isCorrect() {
        return userChoice != NO_CHOICE_SELECTED && choices.get(userChoice).equals(answer);
    }

    public int getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(int userChoice) {
        this.userChoice = userChoice;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeStringList(this.choices);
        dest.writeInt(this.userChoice);
        dest.writeString(this.answer);
    }
}
