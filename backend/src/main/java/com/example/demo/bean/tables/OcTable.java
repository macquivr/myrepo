package com.example.demo.bean.tables;

import com.example.demo.bean.TableBeanl;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.bean.TableRowData;
import com.example.demo.dto.ui.CStatusRowDTO;
import com.example.demo.dto.ui.CStatusTableDTO;
public class OcTable {

    private final TableBeanl data;

    public OcTable()
    {
        List<String> order = new ArrayList<>();
        order.add("Name");
        order.add("Under");
        order.add("Over");
        order.add("Dr");
        order.add("NetFree");

        data = new TableBeanl(order);
    }

    public void populateTable(CStatusTableDTO tdata) {
        List<CStatusRowDTO> ndata = tdata.getCstatus();

        for (CStatusRowDTO d : ndata) {
            if (d.getName().equals("Total"))
                continue;
            TableRowData nd = new TableRowData(this.data);
            nd.add(TableBeanl.LABEL, d.getName());
            nd.add("Under", d.getUnder());
            nd.add("Over", d.getOver());
            nd.add("Dr", d.getDr());
            nd.add("NetFree",d.getNetfree());
            data.add(nd);
        }
    }
    public void Print(FileWriter w) {
        data.Print(w);
    }
}
