package com.example.demo.utils.uidata;

import com.example.demo.domain.Ledger;
import com.example.demo.dto.ui.CreditRowDTO;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idate.Idate;

import java.util.List;

public class CreditUI extends Base<CreditRowDTO> {
    private final boolean paid;

    public CreditUI(boolean p) {

        this.paid = p;

    }

    public Object factory() { return new CreditRowDTO();}
    public void addStuff(List<CreditRowDTO> l, Object data, String dstr)
    {
        CreditRowDTO d = (CreditRowDTO) data;
        d.setLabel(dstr);
        l.add(d);
    }
    public void apply(Idate l, Object obj)
    {
        Ledger ldata = (Ledger) l.getData().getObj();
        CreditRowDTO data = (CreditRowDTO) obj;

        int lbl = ldata.getLabel().getId();
        if (paid) {
            switch (lbl) {
                case 11209:
                case 11285:
                    data.setUsaa(Utils.convertDouble(data.getUsaa() + ldata.getAmount()));
                    break;
                case 10264:
                case 11449:
                    data.setCapone(Utils.convertDouble(data.getCapone() + ldata.getAmount()));
                    break;
                case 10178:
                case 11448:
                case 12933:
                    data.setAaa(Utils.convertDouble(data.getAaa() + ldata.getAmount()));
                    break;
                case 10019:
                case 11450:
                    data.setAmazon(Utils.convertDouble(data.getAmazon() + ldata.getAmount()));
                    break;
            }
        } else {
            int lt = ldata.getLtype().getId();
            switch(lt) {
                case 8:
                    data.setUsaa(Utils.convertDouble(data.getUsaa() + ldata.getAmount()));
                    break;
                case 7:
                    data.setCapone(Utils.convertDouble(data.getCapone() + ldata.getAmount()));
                    break;
                case 10:
                    data.setAaa(Utils.convertDouble(data.getAaa() + ldata.getAmount()));
                    break;
                case 9:
                    data.setAmazon(Utils.convertDouble(data.getAmazon() + ldata.getAmount()));
                    break;
            }

        }
    }
}
