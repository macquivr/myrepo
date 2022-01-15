package com.example.demo.importer;

import com.example.demo.repository.*;

public class Repos {
    private NamesRepository namesRepository;
    private PayeeRepository payeeRepository;
    private LabelRepository labelRepository;
    private CategoryRepository categoryRepository;
    private LocationRepository locationRepository;
    private DupsRepository dupsRepository;
    private StatementsRepository statementsRepository;
    private StatementRepository statementRepository;
    private LtypeRepository ltypeRepository;
    private StypemapRepository stypemapRepository;
    private StypeRepository stypeRepository;
    private CsbTypeRepository csbTypeRepository;
    private MltypeRepository mltypeRepository;
    private LedgerRepository ledgerRepository;
    private ChecksRepository checkRepository;
    private UtilitiesRepository utilRepository;

    public Repos(PayeeRepository p,
                 LabelRepository l,
                 DupsRepository d,
                 StatementsRepository sts,
                 StatementRepository st,
                 LtypeRepository lt,
                 StypemapRepository sm,
                 StypeRepository stp,
                 CsbTypeRepository c,
                 MltypeRepository m,
                 NamesRepository n,
                 CategoryRepository cat,
                 LocationRepository loc,
                 LedgerRepository led,
                 ChecksRepository cs,
                 UtilitiesRepository u
                 ) {
        payeeRepository = p;
        labelRepository = l;
        dupsRepository = d;
        statementsRepository = sts;
        statementRepository = st;
        ltypeRepository = lt;
        stypemapRepository = sm;
        stypeRepository = stp;
        csbTypeRepository = c;
        mltypeRepository = m;
        namesRepository = n;
        categoryRepository = cat;
        locationRepository = loc;
        ledgerRepository = led;
        checkRepository = cs;
        utilRepository = u;
    }

    public PayeeRepository getPayeeRepository() { return payeeRepository; }
    public LabelRepository getLabelRepository() { return labelRepository; }
    public DupsRepository getDupsRepository() { return dupsRepository; }
    public StatementsRepository getStatementsRepository() { return statementsRepository; }
    public StatementRepository getStatementRepository() { return statementRepository; }
    public LtypeRepository getLtypeRepository() { return ltypeRepository; }
    public StypemapRepository getStypemapRepository() { return stypemapRepository; }
    public StypeRepository getStypeRepository() { return stypeRepository; }
    public CsbTypeRepository getCsbTypeRepository() { return csbTypeRepository; }
    public MltypeRepository getMlTypeRepository() { return mltypeRepository; }
    public NamesRepository getNamesRepository() { return namesRepository; }
    public CategoryRepository getCategoryRepository() { return categoryRepository; }
    public LocationRepository getLocationRepository() { return locationRepository; }
    public LedgerRepository getLedgerRepository() { return ledgerRepository; }
    public ChecksRepository getChecksRepository() { return checkRepository; }
    public UtilitiesRepository getUtilitiesRepository() { return utilRepository; }
}
