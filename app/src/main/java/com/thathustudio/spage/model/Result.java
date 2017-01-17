package com.thathustudio.spage.model;

public class Result {
    private int userId;
    private int exerciseId;
    private int score;
    private String userName;

    public Result(String userName) {
        this.userName = userName;
    }

    public Result(int userId, int exerciseId, int score) {
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.score = score;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
