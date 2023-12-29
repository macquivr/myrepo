package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

@Entity
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Statements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private LocalDate created;
    private LocalDate stmtdate;

    @OneToMany(mappedBy = "statements", cascade = {
            CascadeType.ALL
    })
    private List<Statement> statement;

    public int getId() { return id; }

    public String getName() {
        //if (this.name == null) {
        //    this.name = "TEMP";
        //}
        return name;
    }
    public void setName(String n) { name = n; }

    public LocalDate getCreated() { return created; }
    public void setCreated(LocalDate d) { created = d; }

    public LocalDate getStmtdate() { return stmtdate; }
    public void setStmtdate(LocalDate d) { stmtdate = d; }

    public List<Statement> getStatement() { return statement; }
    public void addStatement(Statement stmt) {
        if (statement == null)
            statement = new ArrayList<Statement>();
        statement.add(stmt);
    }

}
