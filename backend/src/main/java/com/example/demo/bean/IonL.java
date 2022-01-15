package com.example.demo.bean;

import java.util.List;
import java.util.Vector;

public class IonL {
    private List<Ion> data = null;
    private int maxIn = 0;
    private int maxOut = 0;

    public IonL() {
        data = new Vector<Ion>();
    }

    public void add(Ion d) { data.add(d); calc();}

    public String getInLabel(Ion d)
    {
        String ret = String.valueOf(d.getIn());
        while (ret.length() < maxIn) {
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

    private void calc()
    {
        for (Ion I : data) {
            String dstrI = String.valueOf(I.getIn());
            if (dstrI.length() > maxIn)
                maxIn = dstrI.length();

            String dstrO = String.valueOf(I.getOut());
            if (dstrO.length() > maxOut)
                maxOut = dstrO.length();
        }

    }
}
