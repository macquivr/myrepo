package preprocessor.impl;

import preprocessor.Base;
import preprocessor.StartStop;

import java.util.ArrayList;
import java.util.List;

public class MLImpl extends Base {
    public MLImpl(StartStop d)
    {
        super(d);
        this.fname = "ml.csv";
    }
    public boolean doIn() {
        return doInCsv();
    }
    public void doTransform() {
        int idx = 0;
        int cnt = 0;
        for (idx=0;idx<7;idx++) {
            ilines.remove(ilines.get(0));
        }
        for (idx=0;idx<7;idx++) {
            ilines.remove(ilines.get(ilines.size()-1));
        }

        List<String> nlines = new ArrayList<String>();

        for (String s : ilines) {
            idx = s.indexOf('$');
            if (idx == -1) {
                System.out.println("BAD $ " + s);
                return;
            }
            String start = s.substring(0,idx);
            String rest = s.substring(idx+1);
            idx = rest.indexOf('$');
            if (idx != -1) {
                System.out.println("2 $ " + s);
                return;
            }
            int last = start.length()-1;
            char l = start.charAt(last);
            if (l == ',') {
                nlines.add(start + "+$" + rest);
            } else {
                nlines.add(s);
            }
        }
        for (idx = nlines.size()-1;idx>=0;idx--) {
            if (checkRange(nlines.get(idx),0)) {
                olines.add(nlines.get(idx));
            }
        }
    }
}
