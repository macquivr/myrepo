package com.example.demo.utils.uidata;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.dto.ui.BillsRowDTO;
import com.example.demo.utils.Utils;
import java.util.List;

public class BillsUI extends Base {

    public Object factory() { return new BillsRowDTO(); }
    public void addStuff(List l, Object data, String dstr) {
        BillsRowDTO d = (BillsRowDTO) data;
        d.setLabel(dstr);
        l.add(d);
    }

    public void apply(Idate ld, Object obj) {
        Ledger l = (Ledger) ld.getData();
        BillsRowDTO data = (BillsRowDTO) obj;
        Label lbl = l.getLabel();
        Checks c = l.getChecks();
        if (c != null) {
             int id = c.getPayee().getId();
             switch(id) {
                 case 60:
                 case 64:
                 case 75:
                     data.setUtilities(Utils.convertDouble(data.getUtilities() + l.getAmount()));
                     break;
                 case 63:
                     data.setCar(Utils.convertDouble(data.getCar() + l.getAmount()));
                     break;
                 case 78:
                 case 90:
                 case 114:
                     data.setEmma(Utils.convertDouble(data.getEmma() + l.getAmount()));
                     break;
                 case 99:
                 case 102:
                     data.setMisc(Utils.convertDouble(data.getMisc() + l.getAmount()));
                     break;
             }
        } else {
            int id = lbl.getId();
            switch (id) {
                case 10064:
                case 11281:
                case 10344:
                    data.setUtilities(Utils.convertDouble(data.getUtilities() + l.getAmount()));
                    break;
                case 11182:
                case 10949:
                case 12712:
                    data.setMortg(Utils.convertDouble(data.getMortg() + l.getAmount()));
                    break;
                case 11456:
                case 11451:
                    data.setLife(Utils.convertDouble(data.getLife() + l.getAmount()));
                    break;
                case 10948:
                    data.setCar(Utils.convertDouble(data.getCar() + l.getAmount()));
                    break;
                case 10612:
                case 10973:
                case 11257:
                    data.setEmma(Utils.convertDouble(data.getEmma() + l.getAmount()));
                    break;
                case 9990:
                    data.setMisc(Utils.convertDouble(data.getMisc() + l.getAmount()));
                    break;
            }
        }
    }
}
