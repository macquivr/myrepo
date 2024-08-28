package com.example.demo.actions;

import com.example.demo.domain.*;
import com.example.demo.dto.SessionDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.LedgerRepository;

import java.time.LocalDate;
import java.util.List;

public class UpdateLastUsed extends BaseAction implements ActionI {

    public UpdateLastUsed(Repos r) {
        super(r);

    }

    public boolean go(SessionDTO session) throws Exception {

        boolean b = updateLabels();
        if (!b)
            return false;

        List<Ledger> ldata = repos.getLedgerRepository().findAllByTransdateBeforeOrderByTransdateDesc(LocalDate.now());
        updateCountries(ldata);
        updateStates(ldata);
        return true;
    }

    private boolean updateLabels() {
        List<Label> labels = repos.getLabelRepository().findAll();
        System.out.println("Labels....");
        for (Label l : labels) {
            if (!updateLabel(l)) {
                return false;
            }
        }
        System.out.println("Ok.");
        System.out.println();
        return true;
    }

    private void updateCountries( List<Ledger> ldata ) {
        List<Location> locs = repos.getLocationRepository().findAllByType("COUNTRY");
        System.out.println("Countries....");
        for (Location l : locs) {
            if (l.getName().equals("NoCountry"))
                continue;
            if (!updateCountry(l,ldata)) {
                return;
            }
        }
        System.out.println("Ok.");
        System.out.println();
    }

    private void updateStates( List<Ledger> ldata ) {
        List<Location> locs = repos.getLocationRepository().findAllByType("STATE");
        System.out.println("States....");
        for (Location l : locs) {
            if (l.getName().equals("NoState"))
                continue;
            if (!updateState(l,ldata)) {
                return;
            }
        }
        System.out.println("Ok.");
    }

    private boolean updateCountry(Location loc,  List<Ledger> ldata ) {
        for (Ledger l : ldata) {
            if (l.getLtype().getId() > 14)
                continue;
           if (l.getLabel().getLocation().getCountry().getId() == loc.getId()) {
               if (!loc.getLastUsed().toString().equals(l.getTransdate().toString())) {
                   System.out.println("Updating Ledger: " + l.getId() + " Country: " + l.getId() + " Date: " + l.getTransdate().toString());
                   loc.setLastUsed(l.getTransdate());
                   repos.getLocationRepository().save(loc);
               } else {
                   System.out.println("Country " + l.getId() + " Already properly set.");
               }
               break;
           }
        }


        return true;

    }
    private boolean updateState(Location loc,  List<Ledger> ldata ) {
        for (Ledger l : ldata) {
            if (l.getLtype().getId() > 14)
                continue;
            if (l.getLabel().getLocation().getState().getId() == loc.getId()) {
                if (!loc.getLastUsed().toString().equals(l.getTransdate().toString())) {
                    System.out.println("Updating Ledger: " + l.getId() + " State: " + l.getId() + " Date: " + l.getTransdate().toString());
                    loc.setLastUsed(l.getTransdate());
                    repos.getLocationRepository().save(loc);
                } else {
                    System.out.println("State " + l.getId() + " Already properly set.");
                }
                break;
            }
        }

        return true;

    }

    private boolean  updateLabel(Label l) {
        LedgerRepository r = repos.getLedgerRepository();
        List<Ledger> lbls = r.findAllByLabelOrderByTransdateDesc(l);
        boolean ret = false;
        if (!lbls.isEmpty())  {
            Ledger data = lbls.get(0);
            if (!l.getLastUsed().toString().equals(data.getTransdate().toString())) {
                System.out.println("Updating Ledger: " + data.getId() + " Label: " + l.getId() + " Size: " + lbls.size() + " Date: " + data.getTransdate().toString());
                l.setLastUsed(data.getTransdate());
                repos.getLabelRepository().save(l);
            } else {
                System.out.println("Label " + data.getId() + " Already properly set.");
            }
            ret = true;
        } else {
            System.out.println("*** NO USES FOR " + l.getId() + " Found");
        }
        return ret;
    }

}

