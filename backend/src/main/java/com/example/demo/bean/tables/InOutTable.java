package com.example.demo.bean.tables;

import com.example.demo.bean.Ion;
import com.example.demo.bean.TableBeanl;
import com.example.demo.bean.TableRowData;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class InOutTable {

    private final TableBeanl data;

    public InOutTable()
    {
        List<String> order = new ArrayList<>();
        order.add("Name");
        order.add("In");
        order.add("Out");
        order.add("Net");
        order.add("Balance");

        data = new TableBeanl(order);
    }

    public void populateTable(List<Ion> ndata) {
        for (Ion d : ndata) {
            TableRowData nd = new TableRowData(this.data);
            nd.add(TableBeanl.LABEL, d.getLabel());
            nd.add("In", d.getIn());
            nd.add("Out", d.getOut());
            nd.add("Net", d.getNet());
            nd.add("Balance", d.getBalance());
            data.add(nd);
        }
    }
    public void Print(FileWriter w) {
        data.Print(w);
    }
}
