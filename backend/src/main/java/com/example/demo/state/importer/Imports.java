package com.example.demo.state.importer;

import com.example.demo.dto.ImportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import com.example.demo.importer.update.*;

public class Imports {
    private static final Logger logger=LoggerFactory.getLogger(Imports.class);
    private final HashMap<String,ImportDR> data;
    private final HashMap<ImportUpdate, IUpdateAction> updateActions;

    private static Imports obj;

    private Imports()
    {
        this.data = new HashMap<>();
        this.updateActions = new HashMap<>();
        initActions();
    }

    private void initActions()
    {
        updateActions.put(ImportUpdate.CHECK,new ImportCheckUpdate());
        updateActions.put(ImportUpdate.DUP,new ImportDupLabelUpdate());
        updateActions.put(ImportUpdate.NEWL,new ImportNewLabelUpdate());
        updateActions.put(ImportUpdate.STYPE,new ImportStypeUpdate());
    }

    public static Imports getObj() {
        if (obj == null)
            obj = new Imports();
        return obj;
    }

    public IUpdateAction getImportAction(ImportUpdate key)
    {
        return updateActions.get(key);
    }

    public ImportDTO makeNewImport(String sessionId) {
        ImportDTO newImport = new ImportDTO(sessionId);

        ImportDR dr = new ImportDR();
        dr.setDto(newImport);
        data.put(sessionId,dr);

        logger.info("New Import: " + sessionId + " " + data.size());

        return newImport;
    }

    public boolean findImport(String session) {
        return (data.get(session) != null);
    }

    public ImportDR getImportObj(String session)
    {
        return data.get(session);
    }

    public ImportData getData(String session)
    {
        ImportDR dr = data.get(session);
        if (dr == null)
            return null;

        return dr.getData();
    }

    public ImportDTO getImport(String session) {
        ImportDR dr = data.get(session);
        if (dr == null)
            return null;
        return dr.getDto();
    }
}
