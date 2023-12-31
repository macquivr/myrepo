package com.example.demo.utils.uidata;

import com.example.demo.domain.Category;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.domain.Checks;
import com.example.demo.bean.Ion;
import com.example.demo.utils.idate.Idate;

import java.util.List;

public class InOutNetUI extends Base<Ion> {
    private final Category transferc;

    public InOutNetUI(Category t)
    {
        this.transferc = t;
    }

    public Object factory() { return new Ion(); }
    public void addStuff(List<Ion> l, Object data, String dstr) {
        Ion d = (Ion) data;
        d.setLabel(dstr);
        d.setNet(d.getIn() + d.getOut());
        l.add(d);
    }

    public void apply(Idate ld, Object obj) {
        Ledger l = (Ledger) ld.getData().getObj();
        Ion data = (Ion) obj;
        Label lbl = l.getLabel();
        Category c = lbl.getCategory();
        if (c.getId() != transferc.getId()) {
            if (!mltransfer(l)) {
                if (l.getAmount() > 0) {
                    data.setIn(data.getIn() + l.getAmount());
                }
                if (l.getAmount() < 0) {
                    data.setOut(data.getOut() + l.getAmount());
                }
            }
        }
    }

    private boolean mltransfer(Ledger l) {
        Checks c = l.getChecks();
        if (c == null)
            return false;
        return (c.getPayee().getId() == 98);
    }
}
