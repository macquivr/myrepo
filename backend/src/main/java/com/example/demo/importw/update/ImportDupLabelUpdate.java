package com.example.demo.importw.update;

import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.repository.DupsRepository;
import com.example.demo.repository.LabelRepository;
import com.example.demo.importer.Repos;
import com.example.demo.domain.Label;
import com.example.demo.domain.Dups;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.state.importer.ImportDR;

import java.util.List;

public class ImportDupLabelUpdate implements IUpdateAction{
    private static final Logger logger= LoggerFactory.getLogger(ImportDupLabelUpdate.class);

    public void performAction(Repos r, SessionUpdateDTO data, ImportDR dro, StatusDTO sts)
    {
        List<String> ir = dro.getDto().getIresults();
        String line = ir.get(0);
        String dlabel = data.getData();

        LabelRepository lrepo = r.getLabelRepository();
        Label lbl = lrepo.findByName(dlabel);
        if (lbl == null) {
            System.out.println("Could not find " + dlabel);
            return;
        }
        Dups d = new Dups();
        d.setDupLabel(line);
        d.setLabel(lbl);

        DupsRepository dr = r.getDupsRepository();
        dr.save(d);

        ir.remove(0);

        logger.info("SAVED: LABEL " + line + " DUP " + lbl.getId());
    }
}
