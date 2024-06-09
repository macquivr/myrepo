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
