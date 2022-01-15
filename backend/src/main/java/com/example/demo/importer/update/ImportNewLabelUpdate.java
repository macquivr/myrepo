package com.example.demo.importer.update;

import com.example.demo.domain.Label;
import com.example.demo.domain.Names;
import com.example.demo.domain.Location;
import com.example.demo.domain.Category;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LabelRepository;
import com.example.demo.repository.NamesRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.LocationRepository;
import com.example.demo.state.importer.ImportDR;
import com.example.demo.state.importer.NewData;
import com.example.demo.state.importer.NewDataE;
import com.example.demo.bean.NewLabelData;
import java.time.LocalDate;

import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ImportNewLabelUpdate implements IUpdateAction{
    private static final Logger logger= LoggerFactory.getLogger(ImportNewLabelUpdate.class);

    public void performAction(Repos r, SessionUpdateDTO data, ImportDR dro, StatusDTO sts) {
        List<String> ir = dro.getDto().getIresults();
        String line = ir.get(0);
        NewData nd = dro.getNData();
        String nlabel = data.getData();


        nd.update(nlabel,r);

        if (nd.getState() == NewDataE.DONE) {
            persist(r, dro, line);
            ir.remove(0);
        }
        sts.setMessage(nd.getState().toString());
    }

    private void persist(Repos r, ImportDR dro, String line) {
        NewData data = dro.getNData();
        NewLabelData nld = dro.getHMapEntry(line);
        Label lbl = null;
        Location loc = null;
        Category cat = null;
        Names nm = null;

        LabelRepository lrepo = r.getLabelRepository();
        NamesRepository nrepo = r.getNamesRepository();
        LocationRepository locRepo = r.getLocationRepository();
        CategoryRepository catRepo = r.getCategoryRepository();

        if (!data.isNewName())
            nm = nrepo.findByName(data.getName());
        else {
            nm = new Names();
            nm.setName(data.getName());
            nm.setCreated(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
            nm.setLastUsed(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
            nrepo.save(nm);
        }

        if (!data.isNewLocation())
            loc = locRepo.findByName(data.getLocation());
        else {
            Location c = locRepo.findByName("USA");
            Location s = locRepo.findByName("MA");
            loc = new Location();
            loc.setName(data.getName());
            loc.setCreated(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
            loc.setLastUsed(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
            loc.setType("REG");
            loc.setCountry(c);
            loc.setState(s);
            locRepo.save(loc);
        }

        if (!data.isNewCategory())
            cat = catRepo.findByName(data.getCategory());
        else {
            cat = new Category();
            cat.setName(data.getName());
            cat.setCreated(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
            cat.setLastUsed(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
            catRepo.save(cat);
        }

        lbl = new Label();
        lbl.setName(line);
        lbl.setCreated(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
        lbl.setLastUsed(DUtil.getDate(nld.getDate(),DUtil.MMDDYYYY));
        lbl.setNames(nm);
        lbl.setLocation(loc);
        lbl.setCategory(cat);

        lrepo.save(lbl);

    }


}
