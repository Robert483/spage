package com.thathustudio.spage.model;

import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class Question {
    public static int NO_CHOICE_SELECTED = RecyclerView.NO_POSITION;

    private String content;
    private List<String> choices;
    private int userChoice;
    private String answer;

    public Question() {
    }

    public Question(String content, List<String> choices) {
        this.content = content;
        setChoices(choices);
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
        answer = choices.get(0);
        Collections.shuffle(choices);
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
}
