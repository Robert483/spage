package com.thathustudio.spage.model;

public class Room {
    private String owner;
    private int nPlayers;

    public Room(String owner, int nPlayers) {
        this.owner = owner;
        this.nPlayers = nPlayers;
    }

    public Room() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getNPlayers() {
        return nPlayers;
    }

    public void setNPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }
}
