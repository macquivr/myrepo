package com.example.demo.services;


import com.example.demo.domain.Names;
import com.example.demo.domain.Location;
import com.example.demo.domain.Category;
import com.example.demo.dto.ImportDTO;
import com.example.demo.dto.StatusDTO;
import com.example.demo.dto.NameLocCatDTO;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.state.importer.Imports;
import com.example.demo.state.importer.ImportState;
import com.example.demo.importer.doImport;
import com.example.demo.dto.SessionUpdateDTO;
import com.example.demo.state.importer.NewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
import java.util.List;

import com.example.demo.importer.update.ImportUpdate;
import com.example.demo.importer.update.IUpdateAction;
import com.example.demo.state.importer.ImportDR;
import com.example.demo.bean.NewLabelData;
@Service
public class ImportService {
    private static final Logger logger= LoggerFactory.getLogger(ImportService.class);

    private Repos repos = null;

    @Autowired
    private OcRepository ocRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetsRepository budgetsRepository;

    @Autowired
    private BudgetValuesRepository budgetvaluesRepository;
    @Autowired
    private PayeeRepository payeeRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private DupsRepository dupsRepository;

    @Autowired
    private StatementsRepository statementsRepository;

    @Autowired
    private StatementRepository statementRepository;

    @Autowired
    private LtypeRepository ltypeRepository;

    @Autowired
    private StypemapRepository stypemapRepository;

    @Autowired
    private StypeRepository stypeRepository;

    @Autowired
    private CsbTypeRepository csbTypeRepository;

    @Autowired
    private MltypeRepository mltypeRepository;

    @Autowired
    private NamesRepository namesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private ChecksRepository checkRepository;

    @Autowired
    private UtilitiesRepository utilRepository;

    private void init()
    {
        logger.info("INITIALIZING REPO....");
        repos = new Repos(payeeRepository,
                labelRepository,
                dupsRepository,
                statementsRepository,
                statementRepository,
                ltypeRepository,
                stypemapRepository,
                stypeRepository,
                csbTypeRepository,
                mltypeRepository,
                namesRepository,
                categoryRepository,
                locationRepository,
                ledgerRepository,
                checkRepository,
                utilRepository,
                budgetRepository,
                budgetsRepository,
                budgetvaluesRepository,
                ocRepository);

        CsbTypeRepository test = repos.getCsbTypeRepository();
        if (test == null) {
            logger.info("EEK!");
        } else {
            logger.info("Ok.");
        }
    }

    public NameLocCatDTO getNameLocCat(String sessionId) {
        ImportDR dr = Imports.getObj().getImportObj(sessionId);
        NewData nd = dr.getNData();
        String state = nd.getState().toString();

        List<Names> names = repos.getNamesRepository().findAll();
        List<Location> locations = repos.getLocationRepository().findAll();
        List<Category> categories = repos.getCategoryRepository().findAll();

        NameLocCatDTO ret = new NameLocCatDTO(names,locations,categories);
        ret.setState(state);

        return ret;
    }

    public StatusDTO update(SessionUpdateDTO data) {
        StatusDTO ret = new StatusDTO();
        ImportDTO dto = Imports.getObj().getImport(data.getSession());
        if (dto == null) {
            logger.error("DTO not found for session " + data.getSession());
            ret.setMessage("No session.");
            ret.setStatus(false);
            return ret;
        }

        if (dto.getIresults() == null) {
            logger.error("No iresult....");
            ret.setMessage("No iresult.");
            ret.setStatus(false);
            return ret;
        }

        try {
            ImportDR dr = Imports.getObj().getImportObj(data.getSession());
            ImportUpdate obj = null;

            try {
                obj = ImportUpdate.valueOf(data.getType());
            } catch (Exception ex) {
                // ignore
            }

            if (obj == null) {
                ret.setMessage("bad type " + data.getType());
                ret.setStatus(false);
            } else {
                IUpdateAction action = Imports.getObj().getImportAction(obj);
                action.performAction(repos, data, dr, ret);
                ret.setStatus(true);
            }

            //IUpdateAction action = Imports.getObj().getImportAction(obj);


        } catch(Exception ex) {
            ret.setStatus(false);
            ret.setMessage((ex.getMessage() == null) ? ex.toString() : ex.getMessage());
        }
        return ret;
    }

    public ImportDTO getNextData(String session) {
        ImportDTO dto = Imports.getObj().getImport(session);

        if (dto.getIresults().size() == 0)
            dto.setMdata("Done.");
        else
            dto.setMdata(dto.getIresults().get(0));

        return dto;
    }

    public ImportDTO getNextLabel(String session) {
        ImportDTO dto = Imports.getObj().getImport(session);
        ImportDR dr = Imports.getObj().getImportObj(session);

        if (dto.getIresults().size() == 0)
            dto.setMdata("Done.");
        else {
            NewLabelData nd = dr.getHMapEntry(dto.getIresults().get(0));
            dto.setMdata(nd.getLabel());
        }

        NewData nd = dr.getNData();
        nd.reset();
        return dto;
    }

    public ImportDTO importStatus(String session) {
        ImportDTO ret = null;
        UUID u = null;

        if (repos == null)
            init();
        
        if (!Imports.getObj().findImport(session))
            ret = Imports.getObj().makeNewImport(session);
        else
            ret = Imports.getObj().getImport(session);

        try {
            u = UUID.fromString(session);
        } catch (Exception ex) {
            ret.setError(true);
            ret.setErrMessage("Invalid session " + session);
            return ret;
        }

        doImport iobj = new doImport(u,repos,ret);
        List<String> iresults = null;

        try {
            iresults = iobj.go();
        } catch(Exception ex) {
            logger.error("ERROR",ex);
            ret.setError(true);
            ret.setErrMessage((ex.getMessage() == null) ? ex.toString() : ex.getMessage());
            return ret;
        }

        if (ret.getImportState() == ImportState.MISSING_CHECKS) {
            ret.setIresults(iresults);
            ret.setMdata(iresults.get(0));
            return ret;
        }

        if (ret.getImportState() == ImportState.MISSING_LABELS) {
            ret.setIresults(iresults);
            String line = iresults.get(0);
            ImportDR dr = Imports.getObj().getImportObj(session);
            if (dr == null) {
                ret.setError(true);
                ret.setErrMessage("Invalid dr obj " + session);
                return ret;
            }
            NewLabelData nd = dr.getHMapEntry(line);
            ret.setMdata(nd.getLabel());
            return ret;
        }

        if (ret.getImportState() == ImportState.MISSING_STYPES) {
            if (iresults.size() > 0) {
                ret.setIresults(iresults);
                ret.setMdata(iresults.get(0));
                return ret;
            }
        }

        if (iresults.size() == 1) {
            ret.setError(true);
            ret.setErrMessage(iresults.get(0));
        }

        return ret;
    }

    @Transactional
    public StatusDTO finalizeImport(String session) {
        // commit import to disk
        return null;
    }
}
