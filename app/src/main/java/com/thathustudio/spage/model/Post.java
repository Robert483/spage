package com.thathustudio.spage.model;

import java.io.Serializable;

/**
 * Created by apple on 1/4/17.
 */

public class Post implements Serializable {
    int id;
    int subjectId;
    int userId;
    int commentCount;
    String content;
    String username;
    int date;
    String image;


    public Post clone(){
        Post p =  new Post();
        p.setId(id);
        p.setUsername(username);
        p.setCommentCount(commentCount);
        p.setSubjectId(subjectId);
        p.setContent(content);
        p.setDate(date);
        p.setImage(image);
        p.setRating(rating);
        p.setUserId(userId);
        return p;
    }

    public void clone(Post p){
        id = p.getId();
        userId = p.getUserId();
        username = p.getUsername();
        commentCount = p.getCommentCount();
        subjectId = p.getSubjectId();
        content = p.getContent();
        date = p.getDate();
        image = p.getImage();
        rating = p.getRating();

    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    int rating;

    public Post(int id, int subjectId, int userId, int comments, String content) {
        this.id = id;
        this.subjectId = subjectId;
        this.userId = userId;
        this.commentCount = comments;
        this.content = content;
    }
    public Post(int subjectId, int userId, int comments, String content) {
        this.subjectId = subjectId;
        this.userId = userId;
        this.commentCount = comments;
        this.content = content;
    }

    public  Post(){

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
