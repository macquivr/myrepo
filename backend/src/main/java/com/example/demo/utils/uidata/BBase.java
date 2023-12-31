package com.example.demo.utils.uidata;

import java.util.HashMap;

public abstract class BBase<T> extends Base<T> {
    protected final HashMap<String, Integer> map;

    public abstract Object factory();
    public BBase(HashMap<String, Integer> m) {
        this.map = m;
    }
}
