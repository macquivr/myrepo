package com.example.demo.utils.uidata;

import com.example.demo.chart.chartData;
import com.example.demo.bean.Lvd;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idate.Idate;

import java.util.List;

public class ChartUI extends Base<Lvd> {
    private final chartData chart;

    public ChartUI(chartData c)
    {
        this.chart = c;
    }

    public Object factory() { return new Lvd(); }
    public void addStuff(List<Lvd> l, Object data, String dstr) {
        Lvd d = (Lvd) data;
        d.setLabel(dstr);
        l.add(d);
    }

    public void apply(Idate ld, Object obj) {
        Dvi l = ld.getData();
        Lvd lv = (Lvd) obj;
        double nv;
        double amt = (l.aValue() * -1);

        if (this.chart != null) {
            if (this.chart.dontFlip())
                amt = l.aValue();
        }

        if (lv.getValue() == null)
            nv = amt;
        else {
            double ov = lv.getValue();
            nv = Utils.convertDouble(amt + ov);
        }
        lv.setValue(nv);
    }
}
