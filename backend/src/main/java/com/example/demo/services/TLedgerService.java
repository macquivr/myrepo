package com.example.demo.services;

import com.example.demo.domain.*;
import com.example.demo.repository.*;
import com.example.demo.state.Sessions;
import com.example.demo.dto.*;
import com.example.demo.utils.TLData;
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
public class TLedgerService {
    private static final Logger logger=LoggerFactory.getLogger(TLedgerService.class);

    @Autowired
    private TLedgerRepository repository;

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

    public List<TLedger> findAll() {
        return repository.findAll();
    }

    public List<TLedger> getData(String sessionId) {
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

        List<TLedger> df;
        int ltype = filter.getLtype();

        Ltype ltypeo = null;
        if (ltype > 0) {
            Optional<Ltype> o = ltrepository.findById(ltype);
            if (o.isPresent()) {
                ltypeo = o.get();
            }
        }

        TLData ld = new TLData(repository);
        df = ld.filterByDate(filter,ltypeo);
        if (ltype == -1)
            ld.filterBundle(df);

        List<TLedger> ret;

        if (!filter.getNlc().equals("NONE")) {
           ret = filterByNlc(filter,df);
        }
        else
            ret = df;

        if (ret.isEmpty())
            return ret;

        double total = 0;
        TLedger last = ret.get(ret.size()-1);
        for (TLedger l : ret)
            total = total + l.getAmount();
        TLedger tl = new TLedger();

        tl.setTdate(last.getTdate());
        tl.setLabel(last.getLabel());
        tl.setLtype(last.getLtype());

        tl.setAmount(Utils.convertDouble(total));
        ret.add(tl);

        return ret;
    }

    private List<TLedger> filterByNlc(SessionDTO filter,List<TLedger> data)
    {
        List<TLedger> ret = new Vector<>();
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

        for (TLedger l : data) {
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
