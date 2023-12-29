package com.example.demo.bean;

import com.example.demo.domain.Ledger;
import com.example.demo.domain.Category;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public enum OutMapStaticKey {
    UTILS {
        public String toString() {
            return "Utils";
        }
    },
    POS {
        public String toString() {
            return "Pos";
        }
    },
    ATM {
        public String toString() {
            return "Atm";
        }
    },
    DOG {
        public String toString() {
            return "Dog";
        }
    },
    USAA {
        public String toString() {
            return "Usaa";
        }
    },
    AAA {
        public String toString() {
            return "Aaa";
        }
    },
    CAPONE {
        public String toString() {
            return "CapOne";
        }
    },
    AMAZON {
        public String toString() {
            return "Amazon";
        }
    },
    SEARS {
        public String toString() {
            return "Sears";
        }
    },
    MORTGAGE {
        public String toString() {
            return "Mortgage";
        }
    },
    LIFEINS {
        public String toString() {
            return "LifeIns";
        }
    },
    MEDICAL {
        public String toString() {
            return "Medical";
        }
    },

    SNOW {
        public String toString() {
            return "Snow";
        }
    },
    CONTRACTORS {
        public String toString() {
            return "Contractors";
        }
    },

    FEES {
        public String toString() {
            return "Fees";
        }
    };

    public static void fillInMissing(List<Lvd> data, HashMap<OutMapKey, Lvd> hmap) {
        OutMapStaticKey[] v = OutMapStaticKey.values();
        HashMap<OutMapStaticKey,Boolean> bmap = new HashMap<>();
        Set<OutMapKey> keys = hmap.keySet();

        for (OutMapKey key : keys) {
            if (key.getType() == OutMapKeyType.STATICTYPE) {
                OutMapStaticKey ty = key.getStaticType();
                bmap.put(ty,Boolean.TRUE);
            }
        }
        for (int i = 0;i<v.length;i++) {
            Boolean b = bmap.get(v[i]);
            if (b == null) {
                Lvd l = new Lvd();
                l.setValue(0.0);
                l.setLabel(v[i].toString());
                data.add(l);
            }
        }
    }
    public static OutMapStaticKey makeKey(Ledger l) {
        if (l.getLabel().getId() == 10344)
            return OutMapStaticKey.UTILS;
        if ((l.getLabel().getId() == 11281) ||
                (l.getLabel().getId() == 10064)) {
            LocalDate d = LocalDate.ofYearDay(2016,1);
            if (l.getTransdate().isBefore(d)) {
                return OutMapStaticKey.UTILS;
            }
        }
        if (l.getStype().getId() == 3)
            return OutMapStaticKey.POS;
        if (l.getStype().getId() == 4)
            return OutMapStaticKey.ATM;
        if (l.getChecks() != null) {
            if (l.getChecks().getPayee().getId() == 60) {
                LocalDate d = LocalDate.ofYearDay(2016,1);
                if (l.getTransdate().isBefore(d)) {
                    return OutMapStaticKey.UTILS;
                }
            }
            if (l.getChecks().getPayee().getId() == 71)
                return OutMapStaticKey.DOG;
        }
        if (l.getLabel().getId() == 11209)
            return OutMapStaticKey.USAA;
        if (l.getLabel().getId() == 12933)
            return OutMapStaticKey.AAA;
        if (l.getLabel().getId() == 10264)
            return OutMapStaticKey.CAPONE;
        if (l.getLabel().getId() == 10019)
            return OutMapStaticKey.AMAZON;
        if (l.getChecks() != null) {
            if ((l.getChecks().getPayee().getId() == 75) ||
                (l.getChecks().getPayee().getId() == 64))
                return OutMapStaticKey.UTILS;
            if (l.getChecks().getPayee().getId() == 116)
                return OutMapStaticKey.SEARS;
            if (l.getChecks().getPayee().getId() == 121) 
                return OutMapStaticKey.SNOW;
            if ((l.getChecks().getPayee().getId() == 52) ||
                    (l.getChecks().getPayee().getId() == 68) ||
                    (l.getChecks().getPayee().getId() == 72) ||
                    (l.getChecks().getPayee().getId() == 56) ||
                    (l.getChecks().getPayee().getId() == 136) ||
                    (l.getChecks().getPayee().getId() == 137))
                return OutMapStaticKey.CONTRACTORS;
        }
        if ((l.getLabel().getId() == 12712) ||
                (l.getLabel().getId() == 11182) ||
                (l.getLabel().getId() == 10949))
            return OutMapStaticKey.MORTGAGE;
        if (l.getLabel().getId() == 11451)
            return OutMapStaticKey.LIFEINS;

        if (l.getChecks() != null) {
            if (l.getChecks().getPayee().getCheckType().getId() == 7)
                return OutMapStaticKey.MEDICAL;
        } else {
            Category c = l.getLabel().getCategory();
            if (c.getId() == 12) {
                int lid = l.getLtype().getId();
                if ((lid == 3) ||
                        (lid == 5) ||
                        (lid == 6) ||
                        (lid == 11) ||
                        (lid == 12) ||
                        (lid == 14)) {
                    return OutMapStaticKey.MEDICAL;
                }
            }
        }
        if (l.getLabel().getId() == 10067)
            return OutMapStaticKey.FEES;
        return null;
    }
}
