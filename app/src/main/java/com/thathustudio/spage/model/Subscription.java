package com.thathustudio.spage.model;

/**
 * Created by Phung on 18/01/2017.
 */

public class Subscription {
    private int id;
    private int userId;
    private int subjectId;

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
