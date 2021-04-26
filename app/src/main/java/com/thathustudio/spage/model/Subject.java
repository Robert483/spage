package com.thathustudio.spage.model;

import java.io.Serializable;

/**
 * Created by Phung on 18/01/2017.
 */

public class Subject implements Serializable{
    private int id;
    private String name;

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

    public Subject(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public Subject() {
    }
}
