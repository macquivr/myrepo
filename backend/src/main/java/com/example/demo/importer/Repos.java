package com.example.demo.importer;

import com.example.demo.repository.*;

public class Repos {
    private final BudgetRepository budgetRepository;

    private final BudgetsRepository budgetsRepository;
    private final BudgetValuesRepository budgetValuesRepository;

    private final NamesRepository namesRepository;
    private final PayeeRepository payeeRepository;
    private final LabelRepository labelRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final DupsRepository dupsRepository;
    private final StatementsRepository statementsRepository;
    private final StatementRepository statementRepository;
    private final LtypeRepository ltypeRepository;
    private final StypemapRepository stypemapRepository;
    private final StypeRepository stypeRepository;
    private final CsbTypeRepository csbTypeRepository;
    private final MltypeRepository mltypeRepository;
    private final LedgerRepository ledgerRepository;
    private final TLedgerRepository tledgerRepository;
    private final ChecksRepository checkRepository;
    private final UtilitiesRepository utilRepository;

    private final OcRepository ocRepository;

    private PayperiodRepository payperiodRepository;
    private WdatamapRepository wdatamapRepository;
    private KvpRepository kvpRepository;
    private GscatRepository gscatRepository;
    private LmapRepository lmapRepository;
    private CmapRepository cmapRepository;
    private InmapRepository inmapRepository;

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
                 TLedgerRepository tled,
                 ChecksRepository cs,
                 UtilitiesRepository u,
                 BudgetRepository b,
                 BudgetsRepository bs,
                 BudgetValuesRepository bv,
                 OcRepository oc
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
        tledgerRepository = tled;
        checkRepository = cs;
        utilRepository = u;
        budgetRepository = b;
        budgetsRepository = bs;
        budgetValuesRepository = bv;
        ocRepository = oc;
    }

    public BudgetRepository getBudgetRepository() { return this.budgetRepository; }

    public BudgetsRepository getBudgetsRepository() { return this.budgetsRepository; }
    public BudgetValuesRepository getBudgetValuesRepository() { return this.budgetValuesRepository; }
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
    public TLedgerRepository getTLedgerRepository() { return tledgerRepository; }
    public ChecksRepository getChecksRepository() { return checkRepository; }
    public UtilitiesRepository getUtilitiesRepository() { return utilRepository; }

    public OcRepository getOcRepository() { return ocRepository; }

    public PayperiodRepository getPayPeriod() { return this.payperiodRepository; }
    public WdatamapRepository getPayWdatamap() { return this.wdatamapRepository; }
    public KvpRepository getKvp() { return this.kvpRepository; }

    public GscatRepository getGscat() { return this.gscatRepository; }
    public LmapRepository getLmap() { return this.lmapRepository; }
    public CmapRepository getCmap() { return this.cmapRepository; }
    public InmapRepository getInmap() { return this.inmapRepository; }


    public void setPayPeriod(PayperiodRepository p) { this.payperiodRepository = p; }
    public void setWdatamap(WdatamapRepository w) { this.wdatamapRepository = w; }
    public void setKvp(KvpRepository k) { this.kvpRepository = k; }

    public void setGscat(GscatRepository r) {
        this.gscatRepository = r;
    }
    public void setLmap(LmapRepository r) {
        this.lmapRepository = r;
    }
    public void setCmap(CmapRepository r) {
        this.cmapRepository = r;
    }
    public void setInmap(InmapRepository r) {
        this.inmapRepository = r;
    }
}
