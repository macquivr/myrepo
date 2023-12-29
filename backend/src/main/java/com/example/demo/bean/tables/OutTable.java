package com.example.demo.bean.tables;

import com.example.demo.bean.TableBeanl;
import com.example.demo.bean.TableRowData;
import com.example.demo.bean.OBean;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class OutTable {

    private final TableBeanl data;

    public OutTable()
    {
        List<String> order = new ArrayList<>();
        order.add("Name");
        order.add("Amount");
        order.add("Percent");

        data = new TableBeanl(order);
    }

    public void populateTable(List<OBean> tdata) {
        for (OBean d : tdata) {
            TableRowData nd = new TableRowData(this.data);
            nd.add(TableBeanl.LABEL, d.getLabel());
            nd.add("Amount", d.getAmount());
            nd.add("Percent", d.getPercent());
            data.add(nd);
        }
    }
    public void Print(FileWriter w) {
        data.Print(w);
    }
}
