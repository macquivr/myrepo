package com.example.demo.bean;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class TableBeanl {
    public static final String LABEL = "Name";

    private final List<TableRowData> data;
    private final TableBeant tabs;
    private final TableRowData total;

    private final List<String> order;
    public TableBeanl(List<String> order) {
        this.order = order;
        this.data = new ArrayList<>();
        this.tabs = new TableBeant(order);
        this.total = new TableRowData(this);
    }
    public List<TableRowData> getData() { return data; }
    public TableBeant getTabs() { return tabs; }


    public void Print(FileWriter w) {
        TableRowData headers = new TableRowData(this);
        headers.printHeaders(w,this.order);
        for (TableRowData d : data) {
            d.Print(w,order);
        }
        total.Print(w,order);
    }

    public void add(TableRowData b) {
        data.add(b);
        total.update(b);
    }
}
