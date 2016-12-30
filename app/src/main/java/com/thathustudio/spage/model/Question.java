package com.thathustudio.spage.model;

import java.util.Collections;
import java.util.List;

public class Question {
    private String content;
    private List<String> answers;
    private String correctAnswer;

    public Question() {
    }

    public Question(String content, List<String> answers) {
        this.content = content;
        this.answers = answers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        correctAnswer = answers.get(0);
        Collections.shuffle(answers);
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
