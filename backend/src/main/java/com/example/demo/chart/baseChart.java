package com.example.demo.chart;

import com.example.demo.state.Consolidate;
import com.example.demo.utils.dvi.Dvi;
import com.example.demo.utils.idata.Ldvil;
import com.example.demo.utils.idata.baseIData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class baseChart<T> implements chartData<T>
{
    private boolean dflip = false;
    private Double netMod = null;

    protected baseIData<T> bidata;

    public abstract List<T> getChartData(List<T> base);

    public baseIData<T> getIData() {
        baseIData<T> ret = this.bidata;
        ret.initialize(this);
        return ret;
    }
    private int transQuarter(Consolidate c) {
        switch (c) {
            case Q1:
                return 1;
            case Q2:
                return 2;
            case Q3:
                return 3;
            case Q4:
                return 4;
        }
        return -1;
    }

    private int transMonth(Consolidate c) {
        switch (c) {
            case JANUARY:
                return 1;
            case FEBRUARY:
                return 2;
            case MARCH:
                return 3;
            case APRIL:
                return 4;
            case MAY:
                return 5;
            case JUNE:
                return 6;
            case JULY:
                return 7;
            case AUGUST:
                return 8;
            case SEPTEMBER:
                return 9;
            case OCTOBER:
                return 10;
            case NOVEMBER:
                return 11;
            case DECEMBER:
                return 12;
            default:
                return -1;
        }
    }

    private void filterSpecificMonth(int cm, Ldvil<T> data) {
        List<Dvi> death = new ArrayList<>();

        List<Dvi> olist = data.getDviData();
        for (Dvi d : olist) {
            LocalDate dt = d.dValue();
            int m = dt.getMonthValue();
            if (m != cm) {
                death.add(d);
            }
        }
        data.adjust(death);
    }

    private void filterSpecificQuarter(int cq, Ldvil<T> data) {
        List<Dvi> death = new ArrayList<>();

        List<Dvi> olist = data.getDviData();
        for (Dvi d : olist) {
            LocalDate dt = d.dValue();
            int m = dt.getMonthValue();
            switch(cq) {
                case 1:
                    if (m > 3) {
                        death.add(d);
                    }
                    break;
                case 2:
                    if ((m < 4) || (m > 6)) {
                        death.add(d);
                    }
                    break;
                case 3:
                    if ((m < 7) || (m > 9)) {
                        death.add(d);
                    }
                    break;
                case 4:
                    if (m < 10) {
                        death.add(d);
                    }
                    break;
            }
        }
        data.adjust(death);
    }

    public void filterSpecific(Consolidate type, Ldvil<T> data) {
        if (type == null)
            return;
        int cm = transMonth(type);
        if (cm == -1) {
            int cq = transQuarter(type);
            if (cq != -1) {
                filterSpecificQuarter(cq,data);
            }
        } else {
            filterSpecificMonth(cm,data);
        }
    }

    public Double getNetMod() { return this.netMod; }
    public void setNetMod(Double d) { this.netMod = d; }
    public void setDontFlip() { this.dflip = true; }
    public boolean dontFlip() { return this.dflip; }
}
