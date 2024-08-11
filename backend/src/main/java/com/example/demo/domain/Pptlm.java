package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Pptlm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int tlid;
    private int lid;

    public int getId() {
        return this.id;
    }

    public int getTlid() {
        return this.tlid;
    }

    public int getLid() {
        return this.lid;
    }

    public void setTlid(int t) {
        this.tlid = t;
    }

    public void setLid(int t) {
        this.lid = t;
    }
}


