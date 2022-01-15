package com.example.demo.importer.update;

import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.importer.checkUtil;
import com.example.demo.importer.Repos;
import com.example.demo.state.importer.ImportDR;

import java.util.List;

public class ImportCheckUpdate implements IUpdateAction{

    public void performAction(Repos r, SessionUpdateDTO data, ImportDR dr, StatusDTO sts)
    {
        List<String> ir = dr.getDto().getIresults();
        String line = ir.get(0);

        int idx = line.indexOf(' ');
        String num = line.substring(idx+1);
        String payee = data.getData();

        checkUtil.getObj().updateCheck(num,payee);

        ir.remove(0);
    }
}
