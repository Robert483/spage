package com.thathustudio.spage.model;

public class User {
    private String name;
    private String userName;
    private String passWord;
    private int id;

    public User(int id, String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
