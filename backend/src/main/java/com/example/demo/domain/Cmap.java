package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Cmap {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int cid;
    private int gid;

    public int getId() { return id; }
    public int getCid() { return this.cid; }
    public int getGid() { return this.gid; }

    public void setCid(int cid) { this.cid = cid; }
    public void setGid(int gid) { this.gid = gid; }
}
