package com.example.demo.utils.uidata;

import com.example.demo.domain.Checks;
import com.example.demo.domain.Label;
import com.example.demo.domain.Ledger;
import com.example.demo.dto.ui.AnnualRowDTO;
import com.example.demo.utils.Utils;
import com.example.demo.utils.idate.Idate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AnnualUI extends Base<AnnualRowDTO> {
    private static final Logger logger= LoggerFactory.getLogger(AnnualUI.class);

    public Object factory() { return new AnnualRowDTO(); }
    public void addStuff(List<AnnualRowDTO> l, Object data, String dstr) {
        AnnualRowDTO d = (AnnualRowDTO) data;
        d.setLabel(dstr);
        l.add(d);
    }

    public void apply(Idate ld, Object obj) {
        Ledger l = (Ledger) ld.getData().getObj();
        AnnualRowDTO data = (AnnualRowDTO) obj;
        Label lbl = l.getLabel();
        Checks c = l.getChecks();
        if (lbl == null) {
            return;
        }
        if (c != null) {
             int id = c.getPayee().getId();
             switch(id) {
                 case 51:
                     data.setAaa(Utils.convertDouble(data.getAaa() + l.getAmount()));
                     break;
                 case 59:
                     data.setBjs(Utils.convertDouble(data.getBjs() + l.getAmount()));
                     break;
                 case 67:
                     data.setEscrow(Utils.convertDouble(data.getEscrow() + l.getAmount()));
                     break;
                 case 76:
                     data.setExcise(Utils.convertDouble(data.getExcise() + l.getAmount()));
                     break;
                 case 79:
                     data.setFed(Utils.convertDouble(data.getFed() + l.getAmount()));
                     break;
                 case 83:
                     data.setGenerator(Utils.convertDouble(data.getGenerator() + l.getAmount()));
                     break;
                 case 96:
                     data.setMatax(Utils.convertDouble(data.getMatax() + l.getAmount()));
                     break;
                 case 103:
                     data.setOil(Utils.convertDouble(data.getOil() + l.getAmount()));
                     break;
                 case 104:
                     data.setOilservice(Utils.convertDouble(data.getOilservice() + l.getAmount()));
                     break;
                 case 110:
                     data.setPropane(Utils.convertDouble(data.getPropane() + l.getAmount()));
                     break;
                 case 112:
                     data.setRmv(Utils.convertDouble(data.getRmv() + l.getAmount()));
                     break;
                 case 117:
                     data.setSecurity(Utils.convertDouble(data.getSecurity() + l.getAmount()));
                     break;
                 case 124:
                     data.setTaxprep(Utils.convertDouble(data.getTaxprep() + l.getAmount()));
                     break;
                 case 154:
                     data.setTaxprep(Utils.convertDouble(data.getTaxprep() + l.getAmount()));
                     break;
             }
        } else {
            int id = lbl.getId();
            switch (id) {
                case 12551:
                case 12788:
                    data.setMatax(Utils.convertDouble(data.getMatax() + l.getAmount()));
                    break;
                case 12696:
                    data.setPropane(Utils.convertDouble(data.getPropane() + l.getAmount()));
                    break;
                case 12673:
                    data.setOil(Utils.convertDouble(data.getOil() + l.getAmount()));
                    break;
                case 12734:
                    data.setOilservice(Utils.convertDouble(data.getOilservice() + l.getAmount()));
                    break;
                case 12550:
                    data.setFed(Utils.convertDouble(data.getFed() + l.getAmount()));
                    break;
                case 10200:
                    data.setExcise(Utils.convertDouble(data.getExcise() + l.getAmount()));
                    break;
                case 11519:
                    data.setRmv(Utils.convertDouble(data.getRmv() + l.getAmount()));
                    break;
                case 12276:
                    data.setTaxprep(Utils.convertDouble(data.getTaxprep() + l.getAmount()));
                    break;
                case 12058:
                case 12508:
                    data.setCarins(Utils.convertDouble(data.getCarins() + l.getAmount()));
                    break;
                case 11546:
                    data.setSafetydep(Utils.convertDouble(data.getSafetydep() + l.getAmount()));
                    break;
                case 11235:
                    data.setEscrow(Utils.convertDouble(data.getEscrow() + l.getAmount()));
                    break;
                case 11883:
                    data.setTerminix(Utils.convertDouble(data.getTerminix() + l.getAmount()));
                    break;
                case 10027:
                    data.setAmazonprime(Utils.convertDouble(data.getAmazonprime() + l.getAmount()));
                    break;
                case 11297:
                    data.setNorton(Utils.convertDouble(data.getNorton() + l.getAmount()));
                    break;
            }
        }
    }
}
