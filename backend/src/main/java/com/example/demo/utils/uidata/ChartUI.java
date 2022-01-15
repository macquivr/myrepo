package com.example.demo.utils.uidata;

import com.example.demo.bean.Lv ;
import com.example.demo.domain.Ledger;
import com.example.demo.utils.Utils;

import java.util.List;

public class ChartUI extends Base {
    public Object factory() { return new Lv(); }
    public void addStuff(List l, Object data, String dstr) {
        Lv d = (Lv) data;
        d.setLabel(dstr);
        l.add(d);
    }

    public void apply(Idate ld, Object obj) {
        Ledger l = (Ledger) ld.getData();
        Lv lv = (Lv) obj;
        double amt = l.getAmount() * -1;
        double nv = 0;
        if (lv.getValue() == null)
            nv = amt;
        else {
            double ov = Double.valueOf(lv.getValue());
            nv = Utils.convertDouble(amt + ov);
        }
        lv.setValue(String.valueOf(nv));
    }
}
