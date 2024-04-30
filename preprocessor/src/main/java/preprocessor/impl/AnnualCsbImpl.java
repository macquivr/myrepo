package preprocessor.impl;

import preprocessor.StartStop;

public class AnnualCsbImpl extends CsbImpl {
    public AnnualCsbImpl(StartStop d)
    {
        super(d);
        this.fname = "Annual.csv";
    }
}
