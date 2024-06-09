package preprocessor.impl;

import preprocessor.Base;
import preprocessor.StartStop;

public class AmazonImpl extends Base {
    public AmazonImpl(StartStop d)
    {
        super(d);
        this.fname = "amazon.csv";
    }
    public boolean doIn() {
        return doInCsv();
    }
    public void doTransform() {
        String first = ilines.get(0);
        ilines.remove(first);

        int idx = 0;
        for (idx = ilines.size()-1;idx>=0;idx--) {
            if (checkRange(ilines.get(idx),0)) {
                olines.add(ilines.get(idx));
            }
        }
    }
}
