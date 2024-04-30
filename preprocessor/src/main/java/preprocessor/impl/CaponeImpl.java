package preprocessor.impl;

import preprocessor.Base;
import preprocessor.StartStop;

public class CaponeImpl extends Base {
    public CaponeImpl(StartStop d)
    {
        super(d);
        this.fname = "capone.csv";
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
