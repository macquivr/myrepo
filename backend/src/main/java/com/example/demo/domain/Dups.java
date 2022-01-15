package com.example.demo.domain;

import javax.persistence.*;

@Entity
public class Dups {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String dupLabel;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "label")
    private Label label;

    public int getId() { return id; }
    public String getDupLabel() { return dupLabel; }
    public Label getLabel() { return label; }

    public void setDupLabel(String s) { dupLabel = s; }
    public void setLabel(Label l) { label = l; }

}
