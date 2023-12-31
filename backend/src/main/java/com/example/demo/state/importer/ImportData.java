package com.example.demo.state.importer;

import java.util.HashMap;

public class ImportData {
    private final HashMap<String, String> fdata;

    public ImportData()
    {
        this.fdata = new HashMap<>();
    }

    public String getData(String fname)
    {
        return fdata.get(fname);
    }

    public void setData(String fname, String parsedText)
    {
        fdata.put(fname,parsedText);
    }
}
