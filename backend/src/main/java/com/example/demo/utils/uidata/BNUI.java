package com.example.demo.utils.uidata;

import com.example.demo.domain.Budget;
import com.example.demo.dto.BudgetRowDTO;
import com.example.demo.dto.ui.BVNRowDTO;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idate.Idate;

import java.util.HashMap;
import java.util.List;

public class BNUI extends BBase {

    public BNUI(HashMap<String, Integer> m) {
        super(m);
    }
    public Object factory() { return new BVNRowDTO();}
    public void addStuff(List l, Object data, String dstr)
    {
        BVNRowDTO d = (BVNRowDTO) data;
        d.setDate(dstr);
        l.add(d);
    }
    public void apply(Idate l, Object obj)
    {
        Budget u = (Budget) l.getData().getObj();
        BVNRowDTO data = (BVNRowDTO) obj;

        if (u.getBid().getId() == map.get("Utils")) {
            data.setUtils(Utils.convertDouble(data.getUtils() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Pos")) {
            data.setPos(Utils.convertDouble(data.getPos() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Atm")) {
            data.setAtm(Utils.convertDouble(data.getAtm() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Emma")) {
            data.setEmma(Utils.convertDouble(data.getEmma() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Dog")) {
            data.setDog(Utils.convertDouble(data.getDog() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Usaa")) {
            data.setUsaa(Utils.convertDouble(data.getUsaa() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Capone")) {
            data.setCapone(Utils.convertDouble(data.getCapone() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Amazon")) {
            data.setAmazon(Utils.convertDouble(data.getAmazon() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Aaa")) {
            data.setAaa(Utils.convertDouble(data.getAaa() + u.getNet()));
            return;
        }
        if (u.getBid().getId() == map.get("Total")) {
            data.setTotal(Utils.convertDouble(data.getTotal() + u.getNet()));
            return;
        }
    }
}
