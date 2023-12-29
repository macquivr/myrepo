package com.example.demo.bean;

import java.util.List;
import java.util.Vector;

public class IonL {
    private final List<Ion> data;
    private final Ion total;

    private int maxIn = 0;
    private int maxOut = 0;

    private int maxTnet = 0;

    private int maxTin = 0;

    private int maxTout = 0;

    public IonL() {
        data = new Vector<>();
        total = new Ion();
    }

    public void add(Ion d) {
        data.add(d);
        total.setIn(total.getIn() + d.getIn());
        total.setOut(total.getOut() + d.getOut());
        total.setTIn(total.getTIn() + d.getTIn());
        total.setTOut(total.getTOut() + d.getTOut());
        doCalc();
    }

    public Ion getTotal() { return total; }
    public List<Ion> getData() { return data; }

    public String getColumnTInLabel()
    {
        String ret = "TIn";

        while (ret.length() < maxTin) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getTInLabel(Ion d)
    {
        String ret = String.valueOf(d.getTIn());
        while (ret.length() < maxTin) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getColumnTOutLabel()
    {
        String ret = "TOut";

        while (ret.length() < maxTout) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getTOutLabel(Ion d)
    {
        String ret = String.valueOf(d.getTOut());
        while (ret.length() < maxTout) {
            ret = ret.concat(" ");
        }
        return ret;
    }
    public String getColumnTnetLabel()
    {
        String ret = "Net";

        while (ret.length() < maxTnet) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getTnetLabel(Ion d)
    {
        String ret = String.valueOf(d.getTNet());
        while (ret.length() < maxTnet) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getColumInLabel()
    {
        String ret = "In";

        while (ret.length() < maxIn) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getInLabel(Ion d)
    {
        String ret = String.valueOf(d.getIn());
        while (ret.length() < maxIn) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    public String getColumOutLabel()
    {
        String ret = "Out";

        while (ret.length() < maxOut) {
            ret = ret.concat(" ");
        }
        return ret;
    }
    public String getOutLabel(Ion d)
    {
        String ret = String.valueOf(d.getOut());
        while (ret.length() < maxOut) {
            ret = ret.concat(" ");
        }
        return ret;
    }

    private void doCalc() {
        for (Ion I : data) {
            calc(I);
        }
        calc(total);
    }

    private void calc(Ion I)
    {
        String dstrI = String.valueOf(I.getIn());
        if (dstrI.length() > maxIn)
            maxIn = dstrI.length();

        String dstrO = String.valueOf(I.getOut());
        if (dstrO.length() > maxOut)
            maxOut = dstrO.length();

        String dstrt = String.valueOf(I.getTNet());
        if (dstrt.length() > maxTnet)
            maxTnet = dstrt.length();

        String dstrtin = String.valueOf(I.getTIn());
        if (dstrtin.length() > maxTin)
            maxTin = dstrtin.length();

        String dstrtout = String.valueOf(I.getTOut());
        if (dstrtout.length() > maxTout)
            maxTout = dstrtout.length();
    }
}
