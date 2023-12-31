package com.example.demo.utils.uidata;

import com.example.demo.domain.Budget;
import com.example.demo.dto.ui.BVNRowDTO;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idate.Idate;

import java.util.HashMap;
import java.util.List;

public class BVUI extends BBase<BVNRowDTO> {

    public BVUI(HashMap<String, Integer> m) {
        super(m);
    }
    public Object factory() { return new BVNRowDTO();}
    public void addStuff(List<BVNRowDTO> l, Object data, String dstr)
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
            data.setUtils(Utils.convertDouble(data.getUtils() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Pos")) {
            data.setPos(Utils.convertDouble(data.getPos() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Atm")) {
            data.setAtm(Utils.convertDouble(data.getAtm() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Emma")) {
            data.setEmma(Utils.convertDouble(data.getEmma() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Dog")) {
            data.setDog(Utils.convertDouble(data.getDog() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Usaa")) {
            data.setUsaa(Utils.convertDouble(data.getUsaa() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Capone")) {
            data.setCapone(Utils.convertDouble(data.getCapone() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Amazon")) {
            data.setAmazon(Utils.convertDouble(data.getAmazon() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Aaa")) {
            data.setAaa(Utils.convertDouble(data.getAaa() + u.getValue()));
        }
        if (u.getBid().getId() == map.get("Total")) {
            data.setTotal(Utils.convertDouble(data.getTotal() + u.getValue()));
        }
    }
}
