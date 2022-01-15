package com.example.demo.state.importer;

import java.util.HashMap;

public class ImportData {
    private HashMap<String, String> fdata = null;

    public ImportData()
    {
        fdata = new HashMap<String,String>();
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
