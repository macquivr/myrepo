package com.example.demo.bean;

import com.example.demo.domain.Ledger;
import com.example.demo.domain.Payee;

public class OutMapKey {
    private final OutMapKeyType type;

    OutMapStaticKey staticType;
    private int payee;

    private String name;

    private String alabel;

    OutMapKey(OutMapKeyType t) {
        this.type = t;
    }

    public static OutMapKey makeKey(Ledger l) {
        OutMapStaticKey skey = OutMapStaticKey.makeKey(l);
        if (skey != null) {
            OutMapKey ret = new OutMapKey(OutMapKeyType.STATICTYPE);
            ret.setStaticType(skey);
            return ret;
        }

        if (l.getStype().getId() == 6) {
            OutMapKey ret = new OutMapKey(OutMapKeyType.ANNUAL);
            if (l.getChecks() != null) {
                Payee p = l.getChecks().getPayee();
                ret.setAlabel(p.getName());
                return ret;
            }
            ret.setAlabel(l.getLabel().getNames().getName());
            return ret;
        }

        if (l.getChecks() != null) {
            Payee p = l.getChecks().getPayee();
            OutMapKey ret = new OutMapKey(OutMapKeyType.PAYEE);
            ret.setPayee(p.getId());
            return ret;
        }

        OutMapKey ret = new OutMapKey(OutMapKeyType.NAME);
        ret.setName(l.getLabel().getNames().getName());
        return ret;
    }

    public Lvd makeLvd(Ledger l) {
        Lvd ret = new Lvd();

        ret.setValue(l.getAmount());
        if (this.type == OutMapKeyType.STATICTYPE) {
            ret.setLabel(this.staticType.toString());
        }
        if (this.type == OutMapKeyType.NAME) {
            ret.setLabel(this.name);
        }
        if (this.type == OutMapKeyType.PAYEE) {
            ret.setLabel(l.getChecks().getPayee().getName());
        }
        if (this.type == OutMapKeyType.ANNUAL) {
           ret.setLabel(this.alabel);
        }
        return ret;
    }
    public OutMapKeyType getType() { return type; }
    public void setPayee(int p) {
        this.payee = p;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setStaticType(OutMapStaticKey s) {
        this.staticType = s;
    }

    public int getPayee() { return this.payee; }

    public String getName() { return this.name; }

    public String getAlabel() { return this.alabel; }

    public void setAlabel(String a) { this.alabel = a; }

    public OutMapStaticKey getStaticType() { return this.staticType; }

    @Override
    public String toString()
    {
        if (this.type == OutMapKeyType.STATICTYPE) {
            return this.staticType.toString();
        }

        if (this.type == OutMapKeyType.PAYEE) {
            return String.valueOf(this.payee);
        }

        if (this.type == OutMapKeyType.NAME) {
            return this.name;
        }

        if (this.type == OutMapKeyType.ANNUAL) {
            return this.alabel;
        }
        return "";
    }

    @Override
    public int hashCode()
    {
        return 1;
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof OutMapKey))
            return false;

        OutMapKey key = (OutMapKey) obj;
        OutMapKeyType type = key.getType();

        if (type != this.type)
            return false;

        if (this.type == OutMapKeyType.STATICTYPE) {
            return this.staticType == key.getStaticType();
        }

        if (this.type == OutMapKeyType.PAYEE) {
            return this.payee == key.getPayee();
        }

        if (this.type == OutMapKeyType.NAME) {
            return this.name.equals(key.getName());
        }

        if (this.type == OutMapKeyType.ANNUAL) {
            return this.alabel.equals(key.getAlabel());
        }
        return false;
    }
}
