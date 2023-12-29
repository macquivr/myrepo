package com.example.demo.bean;

import java.util.HashMap;
import java.util.List;

public class TableBeant {
    private final HashMap<String,Integer> data;

    public TableBeant(List<String> order)
    {
        this.data = new HashMap<>();
        for (String s : order) {
            update(s,s);
        }
    }

    public Integer getValue(String key)
    {
        return data.get(key);
    }
    public void update(String key, String value) {
        int len = value.length();
        Integer I = data.get(key);
        if ((I == null) || (len > I)){
            data.put(key,len);
        }
    }
}
