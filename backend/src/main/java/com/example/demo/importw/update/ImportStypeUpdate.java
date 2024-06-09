package com.example.demo.importw.update;

import com.example.demo.domain.Stypemap;
import com.example.demo.domain.Label;
import com.example.demo.domain.Stype;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.StypemapRepository;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.StypeRepository;
import com.example.demo.state.importer.ImportDR;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class ImportStypeUpdate implements IUpdateAction{
    private static final Logger logger= LoggerFactory.getLogger(ImportStypeUpdate.class);

    public void performAction(Repos r, SessionUpdateDTO data, ImportDR dro, StatusDTO sts)
    {
        List<String> ir = dro.getDto().getIresults();
        String line = ir.get(0);

        String dlabel = data.getData();

        StypeRepository srepo = r.getStypeRepository();
        Optional<Stype> opt = srepo.findById(Integer.valueOf(dlabel));
        if (!opt.isPresent()) {
            return;
        }
        Stype stype = opt.get();

        LabelRepository lrepo = r.getLabelRepository();
        Label lbl = lrepo.findByName(line);

        Stypemap obj = new Stypemap();
        obj.setStype(stype);
        obj.setLabel(lbl);

        StypemapRepository dr = r.getStypemapRepository();
        dr.save(obj);

        ir.remove(0);
    }
}
