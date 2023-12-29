package com.example.demo.utils.uidata;

import com.example.demo.chart.chartData;
import com.example.demo.bean.Lvd;
import com.example.demo.domain.Ledger;
import com.example.demo.utils.Utils;

import java.util.List;

public class ChartUI extends Base {
    private chartData chart;

    public ChartUI(chartData c)
    {
        this.chart = c;
    }

    public Object factory() { return new Lvd(); }
    public void addStuff(List l, Object data, String dstr) {
        Lvd d = (Lvd) data;
        d.setLabel(dstr);
        l.add(d);
    }

    public void apply(Idate ld, Object obj) {
        Ledger l = (Ledger) ld.getData();
        Lvd lv = (Lvd) obj;
        double nv = 0;
        double amt = (l.getAmount() * -1);

        if (this.chart != null) {
            if (this.chart.dontFlip())
                amt = l.getAmount();


        }

        if (lv.getValue() == null)
            nv = amt;
        else {
            double ov = Double.valueOf(lv.getValue());
            nv = Utils.convertDouble(amt + ov);
        }
        lv.setValue(Double.valueOf(nv));
    }
}