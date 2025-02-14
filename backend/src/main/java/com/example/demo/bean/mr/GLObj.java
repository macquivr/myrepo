package com.example.demo.bean.mr;

import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GLObj {
    private HashMap<String, Integer> max;
    private List<String> order;
    private List<Object> data;

    public GLObj(List<String> o)
    {
        this.max = new HashMap<String,Integer>();
        this.order = o;
        this.data = new ArrayList<Object>();
    }

    public void setOrder(List<String> o) {
        this.order = o;
    }

    public List<Object> getData() {
        return this.data;
    }
    public HashMap<String,Integer> getMap() {
        return this.max;
    }

    public void add(Object obj) {
        data.add(obj);
        Method[] ms = obj.getClass().getMethods();
        int i;
        for (String name : order) {
            for (i=0;i<ms.length;i++) {
                if (!ms[i].getName().startsWith("get"))
                    continue;
                String n = ms[i].getName().substring(3);
                if (n.equals("Class"))
                    continue;
                if (n.equals(name)) {
                    try {
                        Object invoke = ms[i].invoke(obj);
                        Integer m = this.max.get(n);
                        int l = invoke.toString().length();
                        if (m == null) {
                            this.max.put(n,l);
                        } else {
                            if (l > m) {
                                this.max.put(n,l);
                            }
                        }
                    } catch (Exception ex) {
                        // ignore
                    }
                }
            }
        }
    }

    public void Print(FileWriter w) {
        try {
            for (Object obj : data) {
                Method[] ms = obj.getClass().getMethods();
                int i;
                for (String name : order) {
                    for (i = 0; i < ms.length; i++) {
                        if (!ms[i].getName().startsWith("get"))
                            continue;
                        String n = ms[i].getName().substring(3);
                        if (n.equals("Class"))
                            continue;
                        if (n.equals(name)) {
                            Object invoke = ms[i].invoke(obj);
                            String str = invoke.toString();
                            int len = str.length();
                            Integer max = this.max.get(n);
                            w.write(str);
                            ptabs(w, max, len);
                        }
                    }
                }
                w.write("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
