package com.example.demo.utils.uidata;

import java.util.List;

import com.example.demo.domain.Utilities;
import com.example.demo.dto.ui.UtilsRowDTO;
import com.example.demo.utils.Utils;

public class UtilsUI extends Base {

    public Object factory() { return new UtilsRowDTO();}
    public void addStuff(List l, Object data, String dstr)
    {
        UtilsRowDTO d = (UtilsRowDTO) data;
        d.setLabel(dstr);
        l.add(d);
    }
    public void apply(Idate l, Object obj)
    {
        Utilities u = (Utilities) l.getData();
        UtilsRowDTO data = (UtilsRowDTO) obj;

        data.setCable(Utils.convertDouble(data.getCable() + u.getCable()));
        data.setCell(Utils.convertDouble(data.getCell() + u.getCell()));
        data.setElectric(Utils.convertDouble(data.getElectric() + u.getElectric()));
    }
}
