package com.example.demo.services;

import com.example.demo.domain.*;
import com.example.demo.repository.*;
import com.example.demo.state.Sessions;
import com.example.demo.dto.*;
import com.example.demo.utils.Utils;
import com.example.demo.utils.LData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Vector;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LedgerService {
    private static final Logger logger=LoggerFactory.getLogger(LedgerService.class);

    @Autowired
    private LedgerRepository repository;

    @Autowired
    private NamesRepository nrepository;

    @Autowired
    private LocationRepository lrepository;

    @Autowired
    private CategoryRepository crepository;

    @Autowired
    private StypeRepository srepository;

    @Autowired
    private LtypeRepository ltrepository;

    public List<Ledger> findAll() {
        return repository.findAll();
    }

    public List<Ledger> getData(String sessionId) {
        if (sessionId == null) {
            logger.error("No Session.");
            return new Vector<>();
        }
        if (sessionId.isEmpty()) {
            logger.error("Empty Session.");
            return new Vector<>();
        }

        SessionDTO filter = Sessions.getObj().getSession(sessionId);
        if (filter == null) {
            logger.error("COULDN'T find session.");
            return new Vector<>();
        }

        List<Ledger> df;
        int ltype = filter.getLtype();
        int stype = filter.getStype();

        Ltype ltypeo = null;
        if (ltype > 0) {
            Optional<Ltype> o = ltrepository.findById(ltype);
            if (o.isPresent()) {
                ltypeo = o.get();
            }
        }

        Stype stypeo = null;
        if (stype > 0) {
             Optional<Stype> o = srepository.findById(stype);
             if (o.isPresent())
                 stypeo = o.get();
        }

        LData ld = new LData(repository);
        df = ld.filterByDate(filter,stypeo,ltypeo);
        if (ltype == -1)
            ld.filterBundle(df);

        List<Ledger> ret;

        if (!filter.getNlc().equals("NONE")) {
           ret = filterByNlc(filter,df);
        }
        else
            ret = df;

        if (ret.isEmpty())
            return ret;

        double total = 0;
        Ledger last = ret.get(ret.size()-1);
        for (Ledger l : ret)
            total = total + l.getAmount();
        Ledger tl = new Ledger();

        tl.setTransdate(last.getTransdate());
        tl.setLabel(last.getLabel());
        tl.setLtype(last.getLtype());
        tl.setStype(last.getStype());

        tl.setAmount(Utils.convertDouble(total));
        ret.add(tl);

        return ret;
    }

    private List<Ledger> filterByNlc(SessionDTO filter,List<Ledger> data)
    {
        List<Ledger> ret = new Vector<>();
        String name = filter.getNlcv();
        if (name == null)
            return data;

        int id = -1;
        if (filter.getNlc().equals("NAME")) {
            Names n = nrepository.findByName(name);
            id = n.getId();
        }
        if (filter.getNlc().equals("LOCATION")) {
            Location l = lrepository.findByName(name);
            id = l.getId();
        }
        if (filter.getNlc().equals("CATEGORY")) {
            Category c = crepository.findByName(name);
            id = c.getId();
        }
        if (id < 0)
            return data;

        for (Ledger l : data) {
            if ((filter.getNlc().equals("NAME")) && (l.getLabel().getNames().getId() == id))
                ret.add(l);
            if ((filter.getNlc().equals("LOCATION")) && (l.getLabel().getLocation().getId() == id))
                ret.add(l);
            if ((filter.getNlc().equals("CATEGORY")) && (l.getLabel().getCategory().getId() == id))
                ret.add(l);
        }
        return ret;
    }


}
