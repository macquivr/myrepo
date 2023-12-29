package com.example.demo.utils.runner;

import com.example.demo.domain.*;
import com.example.demo.importer.Repos;
import com.example.demo.repository.*;
import com.example.demo.utils.Utils;

import java.util.*;

public class OcMaintenance {
    private static final Integer[] USAA_FREE = { 10288, 10428, 11490 };
    private static final Integer[] CAPONE_FREE = { 10288, 10428, 11490 };
    private static final Integer[] AAA_FREE = { 13149, 11769 };
    private static final Integer[] AMAZON_FREE = { 0 };

    private OcRepository ocr;

    private Repos repos;

    public OcMaintenance(Repos r, OcRepository ocr) {
        this.repos = r;
        this.ocr = ocr;
    }

    public boolean  go(int stmt) {
        boolean r = true;

        r = doUsaa(stmt);
        if (!r)
            return r;

        r = doCapOne(stmt);
        if (!r)
            return r;

        r = doAaa(stmt);
        if (!r)
            return r;

        return doAmazon(stmt);
    }

    private boolean doUsaa(int stmt) {
        if (stmt != -1) {
            return one(8, 12057, USAA_FREE, stmt);
        }
        return all(8,12057, USAA_FREE);
    }

    private boolean doCapOne(int stmt) {
        if (stmt != -1) {
            return one(7, 10265, CAPONE_FREE,stmt);
        }
        return all(7,10265, CAPONE_FREE);
    }

    private boolean doAaa(int stmt) {
        if (stmt != -1) {
            return one(10, 10076, AAA_FREE,stmt);
        }
        return all(10,10076,AAA_FREE);
    }

    private boolean doAmazon(int stmt) {
        if (stmt != -1) {
            return one(9,10304,AMAZON_FREE,stmt);
        }
        return all(9,10304,AMAZON_FREE);
    }

    private double getPdl(List<Ledger> data, int pdl) {
        double ret = 0;
        List<Ledger> death = new ArrayList<Ledger>();

        for (Ledger l : data) {
            if (l.getLabel().getId() == pdl) {
                ret += l.getAmount();
                death.add(l);
            }
        }
        data.removeAll(death);
        return Utils.convertDouble(ret);
    }

    private double calcOc(List<Ledger> data, Ltype lobj) {
        double ret = 0;
        for (Ledger l : data) {
            if ((l.getLtype().getId() == lobj.getId() && (l.getAmount() > 0))) {
                ret += l.getAmount();
            }
        }
        return Utils.convertDouble(ret);
    }

    private void checkIn(int ltype)
    {
        LedgerRepository lrepo = repos.getLedgerRepository();
        List<Statement> stmts = getStmts(ltype);
        for (Statement s : stmts) {
            Statements st = s.getStatements();
            if (st.getId() == 96)
                continue;
            double t = 0;
            List<Ledger> data = lrepo.findAllByStatement(s);
            for (Ledger l : data) {
                if (l.getAmount() > 0) {
                    t += l.getAmount();
                }
            }
            t = Utils.convertDouble(t);
            if (t != s.getIna()) {
                System.out.println("NO " + s.getId() + " " + st.getStmtdate().toString());
            }
        }

    }
    private void checkOut(int ltype)
    {
        LedgerRepository lrepo = repos.getLedgerRepository();
        List<Statement> stmts = getStmts(ltype);
        for (Statement s : stmts) {
            Statements st = s.getStatements();
            if (st.getId() == 96)
                continue;
            double t = 0;
            List<Ledger> data = lrepo.findAllByStatement(s);
            for (Ledger l : data) {
                if (l.getAmount() < 0) {
                    t += l.getAmount();
                }
            }
            t = t * (-1);
            t = Utils.convertDouble(t - s.getFee());
            if (t != s.getOuta()) {
                System.out.println("NO " + s.getId() + " " + st.getStmtdate().toString() + " " + s.getOuta() + " " + t);
            }
        }

    }

    private boolean onep(Oc obj, Ltype ltypeObj, int pdl, Integer[] fv,Statement s, Statement prev) {
        LedgerRepository lrepo = repos.getLedgerRepository();
        List<Ledger> data = lrepo.findAllByStatement(s);
        double totalPaid = getPdl(data,pdl);
        double sb = s.getSbalance();

        Statements st = s.getStatements();
        if (st.getId() == 96)
            return true;

        System.out.println("Processing " + s.getId());
        double free = freeValueD(data, ltypeObj, fv,true);
        double ocv = calcOc(data,ltypeObj);
        double tst = Utils.convertDouble(totalPaid + free + ocv);
        if (tst != s.getIna()) {
            System.out.println("Fail In " + s.getId() + " " + tst + " " + totalPaid + " " + free + " " + ocv + " "  + s.getIna());
            return false;
        }

        obj.setSdate(st.getStmtdate());
        obj.setLtype(ltypeObj);
        obj.setStmt(s);
        obj.setFree(free);
        obj.setOcv(ocv);
        obj.setOwed(s.getSbalance());
        obj.setPaid(totalPaid);

        if (prev == null) {
            obj.setNewa(s.getSbalance());
            obj.setDebt(0);
            obj.setFc(0);
            obj.setFee(0);
            obj.setDr(0);

            if (sb == totalPaid) {
                obj.setOverv(0);
                obj.setUnder(0);
            } else {
                if (s.getIna() == sb) {
                    obj.setUnder(0);
                    obj.setOverv(0);
                } else {
                    if (s.getIna() > sb) {
                        obj.setUnder(0);
                        if (totalPaid > sb) {
                            obj.setOverv(Utils.convertDouble(totalPaid - sb));
                        }
                    } else {
                        obj.setOverv(0);
                        obj.setUnder(Utils.convertDouble(sb - totalPaid));
                    }
                }
            }
        } else {
            double outaf = prev.getOuta() - prev.getFee();
            double old = Utils.convertDouble(s.getSbalance() - outaf);
            if (old > 0) {
                obj.setFc(0);
                obj.setDebt(old);
            } else {
                obj.setFc(old * (-1));
                obj.setDebt(0);
            }

            obj.setNewa(prev.getOuta());
            obj.setFee(prev.getFee());

            if (totalPaid == sb) {
                obj.setOverv(0);
                obj.setUnder(0);
                if (old > 0) {
                    obj.setDr(old);
                } else {
                    obj.setDr(0);
                }
            } else {
                if (totalPaid > sb) {
                    obj.setUnder(0);
                    if (old > 0) {
                        obj.setDr(old);
                    } else {
                        obj.setDr(0);
                    }
                    obj.setOverv(Utils.convertDouble(totalPaid - sb));
                } else {
                    obj.setOverv(0);
                    if (s.getIna() >= sb) {
                        obj.setUnder(0);
                        if (old > 0) {
                            obj.setDr(old);
                        } else {
                            obj.setDr(0);
                        }
                    } else {
                        obj.setUnder(Utils.convertDouble(sb - s.getIna()));
                        if (s.getIna() <= outaf) {
                            obj.setDr(0);
                        } else {
                            if (old > 0) {
                                obj.setDr(Utils.convertDouble(s.getIna() - outaf));
                            } else {
                                obj.setDr(0);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private boolean one(int ltype, int pdl, Integer[] fv,int sid) {
        StatementsRepository stmtsRepo = repos.getStatementsRepository();
        StatementRepository stmtRepo = repos.getStatementRepository();
        Ltype l = getLtype(ltype);

        Optional<Statements> oss = stmtsRepo.findById(sid);
        if (!oss.isPresent()) {
            System.out.println("No.");
            return false;
        }

        Statements ss = oss.get();
        Statement s = stmtRepo.findAllByStatementsAndLtype(ss,l);

        List<Oc> ret = ocr.findAllByStmt(s);
        Oc obj = null;
        if (ret.isEmpty()) {
            obj = new Oc();
        } else {
            obj = ret.get(0);
        }

        Statement prev = findPrev(ltype, s.getId());
        if (prev == null) {
            System.out.println("NO PREV...");
        }
        boolean r =  onep(obj, getLtype(ltype), pdl, fv,s, prev);
        if (r)
            ocr.save(obj);
        return r;
    }

    private Statement findPrev(int ltype, int sid) {
        List<Statement> stmts = getStmts(ltype);
        Statement prev = null;
        for (Statement s : stmts) {
            if (s.getId() == sid)  {
                if (prev == null)
                    return null;
                return prev;
            }
            prev = s;
        }
        return null;
    }
    private boolean all(int ltype,int pdl, Integer[] fv) {
        List<Oc> ocl = new ArrayList<Oc>();
        LedgerRepository lrepo = repos.getLedgerRepository();
        List<Statement> stmts = getStmts(ltype);
        Ltype l = getLtype(ltype);

        Statement prev = null;
        double free = 0;
        double ocv = 0;

        for (Statement s : stmts) {
            List<Oc> ret = ocr.findAllByStmt(s);
            Oc obj = null;
            if (ret.isEmpty()) {
                obj = new Oc();
            } else {
                obj = ret.get(0);
            }

            boolean r = onep(obj, l, pdl, fv,s, prev);
            if (!r)
                return r;

            prev = s;
            if (obj.getSdate() != null) {
                ocl.add(obj);
            }
        }
        for (Oc o : ocl) {
            ocr.save(o);
        }
        return true;
    }

    private Ltype getLtype(int ltype) {
        LtypeRepository lrepo = repos.getLtypeRepository();
        Optional<Ltype> lo = lrepo.findById(ltype);
        if (!lo.isPresent())
            return null;
        return lo.get();
    }
    private List<Statement> getStmts(int ltype) {
        Ltype l = getLtype(ltype);
        StatementRepository stmtRepo = repos.getStatementRepository();
        return stmtRepo.findAllByLtypeOrderByStatements(l);
    }

    private double freeValueD(List<Ledger> data, Ltype lobj, Integer[] fv,boolean d) {
        double ret = 0;
        List<Ledger> death = new Vector<Ledger>();
        List<Integer> lv = Arrays.asList(fv);
        for (Ledger l : data) {
            if (l.getLtype().getId() != lobj.getId())
                continue;
            if (lv.contains(l.getLabel().getId())) {
                ret += l.getAmount();
                death.add(l);
            }
        }
        if (d) {
            data.removeAll(death);
        }
        return Utils.convertDouble(ret);
    }
}
