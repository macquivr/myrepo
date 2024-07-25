package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Lmap {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int lid;
    private int gid;

    public int getId() { return id; }
    public int getLid()
    {
        return this.lid;
    }
    public int getGid() { return this.gid; }

    public void setLid(int lid) { this.lid = lid; }
    public void setGid(int gid) { this.gid = gid; }
}
