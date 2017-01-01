package com.thathustudio.spage.model;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private int subject;

    public Exercise() {
    }

    public Exercise(int id, String name, String description, int subject) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.subject = subject;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }
}
