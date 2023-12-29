package com.example.demo.bean;

import com.example.demo.utils.Utils;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

public class TableRowData {
    private final HashMap<String,Object> data;

    private final TableBeanl parent;

    public TableRowData(TableBeanl p) {
        data = new HashMap<>();
        this.parent = p;
    }

    public HashMap<String,Object> getData()
    {
        return data;
    }

    public void update(TableRowData obj)
    {
        HashMap<String,Object> odata = obj.getData();
        Set<String> keys = odata.keySet();
        for (String key : keys) {
            Object value = odata.get(key);
            if (value != null) {
                Object lvalue = data.get(key);
                if (lvalue == null) {
                    if (key.equals(TableBeanl.LABEL))  {
                        data.put(key, "Total");
                    } else {
                        data.put(key, value);
                    }
                } else {
                    if ((value instanceof Double) &&
                            (lvalue instanceof Double)) {
                        Double od = (Double) value;
                        Double ld = (Double) lvalue;
                        double nvalue = Utils.convertDouble(od + ld);
                        data.put(key, nvalue);
                    }
                }
            }
        }
    }
    public void add(String key, Object value) {
        TableBeant t = parent.getTabs();

        data.put(key,value);
        t.update(key,value.toString());
    }

    public void printHeaders(FileWriter w,List<String> headers)
    {
        try {
            TableBeant t = parent.getTabs();
            for (String header : headers) {
                w.write(header);
                Integer tv = t.getValue(header);
                if (tv == null)
                    continue;
                ptabs(w, t.getValue(header), header.length());
            }
            w.write("\n");
        } catch (Exception ex) {
            // ignore
        }
    }
    public void Print(FileWriter w,List<String> order) {
        TableBeant t = parent.getTabs();

        try {
            for (String key : order) {
                Object value = data.get(key);
                w.write(value.toString());
                Integer tv = t.getValue(key);
                if (tv == null)
                    continue;
                ptabs(w,t.getValue(key), value.toString().length());
            }

            w.write("\n");
        } catch (Exception ex) {
            // ignore
        }
    }

    private void ptabs(FileWriter w, int max, int len) throws Exception
    {
        int mt = (max / 8);
        int ll = (len / 8);
        int pt = (mt - ll) + 1;
        for (int i = 0;i<pt;i++)
            w.write("\t");
    }
}
