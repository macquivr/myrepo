package preprocessor.impl;

import preprocessor.Base;
import preprocessor.StartStop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AaaImpl extends Base {
    public AaaImpl(StartStop d)
    {
        super(d);
        this.fname = "aaa.csv";
    }

    public boolean doIn() {
        return doInCsv();
    }

    public void doTransform() {
        String first = ilines.get(0);
        ilines.remove(first);

        List<String> nlines = new ArrayList<String>();
        Iterator<String> iter = ilines.iterator();
        while (iter.hasNext()) {
            String line = iter.next();
            String[] data = line.split(",");

            String dstr = makeDstr(data[0]);

            nlines.add(dstr + "," + data[3] + ",X," + data[1] + "," + data[2]);
        }

        int idx = 0;
        for (idx = nlines.size()-1;idx>=0;idx--) {
            if (checkRange(nlines.get(idx),0)) {
                olines.add(nlines.get(idx));
            }
        }
    }

    private String makeDstr(String ds) {
        String ret = "";
        String[] data = ds.split("/");

        Integer m = Integer.valueOf(data[0]);
        Integer d = Integer.valueOf(data[1]);
        String mstr = "";
        String dstr = "";
        if (m < 10) {
            mstr = "0" + m;
        } else {
            mstr = String.valueOf(m);
        }

        if (d < 10) {
            dstr = "0" + m;
        } else {
            dstr = String.valueOf(d);
        }

        ret = mstr + "/" + dstr + "/" + data[2];
        return ret;
    }
    public void doTransform2() {
        String first = ilines.get(0);
        ilines.remove(first);
        List<String> nlines = new ArrayList<String>();
        boolean on = false;
        String tmp = null;
        Iterator<String> iter = ilines.iterator();
        while (iter.hasNext()) {
            String line = iter.next();
            if (line.endsWith(",")) {
                String nline = iter.next();
                int idx = nline.length()-1;
                boolean done = false;
                do {
                    String s = nline.substring(idx);
                    try {
                        Double.valueOf(s);
                    } catch (NumberFormatException ex) {
                        done = true;
                    }
                    if (done) {
                        idx++;
                    } else {
                        idx--;
                    }
                } while (!done);
                int sidx = idx;
                String s = nline.substring(idx);
                done = false;
                do {
                    char c = nline.charAt(sidx);
                    if (c != ' ')
                        sidx--;
                    else {
                        done = true;
                    }
                } while (!done);
                String t = nline.substring(sidx+1,idx);
                String v = nline.substring(idx);
                String nl = line.concat("X," + t + "," + v);
                nlines.add(nl);
            } else {
                nlines.add(line);
            }
        }

        int idx = 0;
        for (idx = nlines.size()-1;idx>=0;idx--) {
            if (checkRange(nlines.get(idx),0)) {
                olines.add(nlines.get(idx));
            }
        }
    }
}
