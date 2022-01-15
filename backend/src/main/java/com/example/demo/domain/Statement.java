package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import org.slf4j.Logger;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Statement {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statements")
    private Statements statements;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ltype")
    private Ltype ltype;

    private Double sbalance;
    private Double fbalance;
    private Double ina;
    private Double outa;
    private Boolean credit;
    private Double fee;

    public int getId() { return id; }
    public Statements getStatements() { return statements; }
    public Ltype getLtype() { return ltype; }
    public Double getSbalance() { return sbalance; }
    public Double getFbalance() { return fbalance; }
    public Double getIna() { return ina; }
    public Double getOuta() { return outa; }
    public Boolean getCredit() { return credit; }
    public Double getFee() { return fee; }

    public void setLtype(Ltype l) { ltype = l; }
    public void setSbalance(Double d) { sbalance = d; }
    public void setFbalance(Double d) { fbalance = d; }
    public void setIna(Double d) { ina = d; }
    public void setOuta(Double d) { outa = d; }
    public void setCredit(Boolean b) { credit = b; }
    public void  setFee(Double f) { fee = f; }
    public void setStatements(Statements s) { statements = s; }


    public void Print()
    {
        System.out.println("ID: " + id);
        if (statements != null)
            System.out.println("Statements: " + statements.getName());
        if (ltype != null)
            System.out.println("LType: " + ltype.getId());
        System.out.println("SBalance: " + sbalance);
        System.out.println("FBalance: " + fbalance);
        System.out.println("In: " + ina);
        System.out.println("Out:" + outa);
        System.out.println("Fee: " + fee);
        System.out.println("Credit: " + credit);
    }
    public void lPrint() { lPrint(null); }
    public void lPrint(Logger log)
    {
        String str = "ID: " + id +
                " Statements: " + ((statements == null) ? "NULL" : statements.getName()) +
                " LType: " + ((ltype == null) ? "null" : ltype.getId()) +
                " SBalance: " + sbalance +
                " FBalance: " + fbalance +
                " In: " + ina +
                " Out:" + outa +
                " Fee: " + fee +
                " Credit: " + credit;
        System.out.println(str);
        if (log != null)
            log.info(str);
    }
}
