package com.example.demo.utils.idata;

import com.example.demo.utils.dvi.Dvi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ldvil<T> {
    private List<T> ldata = null;
    private List<Dvi> dviData = null;

    private HashMap<Dvi, T> map = null;
    public Ldvil(List<T> l, Dvi dvi) {
        map = new HashMap<Dvi, T>();
        this.ldata = l;
        dviData = new ArrayList<Dvi>();
        for (T obj : l) {
            Dvi lobj = dvi.factory(obj);
            map.put(lobj, obj);
            this.dviData.add(lobj);
        }
    }
    public void adjust(List<Dvi> death) {
        for (Dvi d : death) {
            T l = map.get(d);
            if (l != null) {
                this.ldata.remove(l);
            }
        }
    }
    public List<Dvi> getDviData() {
        return this.dviData;
    }
}
