package preprocessor.impl;

import preprocessor.Base;
import preprocessor.StartStop;

import java.util.ArrayList;
import java.util.List;

public class CsbImpl extends Base {
    protected String[] headers;

    public CsbImpl(StartStop d) {
        super(d);
    }
    public void doTransform() {
        int idx = 0;
        int cnt = 0;
        for (idx=0;idx<3;idx++) {
            ilines.remove(ilines.get(0));
        }
        this.headers = ilines.get(0).split(",");
        ilines.remove(0);

        for (idx = ilines.size()-1;idx>=0;idx--) {
            if (checkRange(ilines.get(idx),1)) {
                olines.add(ilines.get(idx));
            }
        }
    }
}
