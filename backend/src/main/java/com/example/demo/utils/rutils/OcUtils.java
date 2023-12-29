package com.example.demo.utils.rutils;

import com.example.demo.domain.Oc;
import com.example.demo.dto.ui.CStatusRowDTO;
import com.example.demo.dto.ui.CStatusTableDTO;
import com.example.demo.importer.Repos;
import com.example.demo.utils.Utils;

import java.time.LocalDate;
import java.util.List;

public class OcUtils {
    private Repos repos = null;

    public OcUtils(Repos repos) {
        this.repos = repos;
    }

    public CStatusTableDTO makeTableData(LocalDate dt) {
        CStatusTableDTO ret = new CStatusTableDTO();

        populateCStatus(dt, ret);
        ret.mtotal();
        return ret;
    }

    private void populateCStatus(LocalDate dt, CStatusTableDTO data) {
        List<Oc> odata = this.repos.getOcRepository().findAllBySdate(dt);

        for (Oc o : odata) {
            if (o.getLtype().getId() == 8) {
                CStatusRowDTO usaaObj = makeCs("Usaa",o);
                data.add(usaaObj);
            }
            if (o.getLtype().getId() == 7) {
                CStatusRowDTO usaaObj = makeCs("Capone",o);
                data.add(usaaObj);
            }
            if (o.getLtype().getId() == 9) {
                CStatusRowDTO usaaObj = makeCs("Amazon",o);
                data.add(usaaObj);
            }
            if (o.getLtype().getId() == 10) {
                CStatusRowDTO usaaObj = makeCs("AAA",o);
                data.add(usaaObj);
            }
        }
    }

    private CStatusRowDTO makeCs(String name, Oc data) {
        CStatusRowDTO ret = new CStatusRowDTO();
        ret.setName(name);
        ret.setOver(data.getOverv());
        ret.setUnder(data.getUnder());
        ret.setDr(data.getDr());
        double free = data.getFree();
        double fee = data.getFee();
        ret.setNetfree(Utils.convertDouble(free - fee));

        return ret;
    }
}
