package com.thathustudio.spage.model;

import java.util.Collections;
import java.util.List;

public class Question {
    private String content;
    private List<String> choices;
    private int userChoice;
    private String answer;

    public Question() {
    }

    public Question(String content, List<String> choices) {
        this.content = content;
        setChoices(choices);
        this.userChoice = 0;
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

    public String getAnswer() {
        return answer;
    }

    public int getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(int userChoice) {
        this.userChoice = userChoice;
    }
}
