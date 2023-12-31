package com.example.demo.state.importer;

import com.example.demo.dto.ImportDTO;
import java.util.HashMap;
import com.example.demo.bean.NewLabelData;

public class ImportDR {
    private ImportData data;
    private ImportDTO dto;
    private NewData ndata;
    private final HashMap<String, NewLabelData> nmap;

    public ImportDR() {
        data = new ImportData();
        ndata = new NewData();
        nmap = new HashMap<>();
    }

    public void setData(ImportData d) { data = d; }
    public void setDto(ImportDTO d) { dto = d; }
    public void setNData(NewData d) { ndata = d; }
    public void putHMapEntry(String l, NewLabelData nd) { nmap.put(l,nd);}

    public ImportData getData() { return data; }
    public ImportDTO getDto() { return dto; }
    public NewData getNData() { return ndata; }
    public NewLabelData getHMapEntry(String l) { return nmap.get(l); }
}
