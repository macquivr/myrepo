package com.example.demo.bean;

import com.example.demo.domain.*;

import java.util.List;

public class Data {
    private String label;
    private Ltype ltype;
    private List<Ledger> ldata;
    private Statement stmt;

    public void setLtype(Ltype l) { ltype = l; adjustLabel(); }
    public void setLdata(List<Ledger> d) { ldata = d; }
    public void setStatement(Statement s) { stmt = s; }

    public Ltype getLtype() { return ltype; }
    public List<Ledger> getLdata() { return ldata; }
    public Statement getStmt() { return stmt; }
    public String getLabel() { return label; }

    private void adjustLabel() {
        label = ltype.getName().replaceAll("Account","");
        while (label.length() < 14) {
            label = label.concat(" ");
        }
    }

}
