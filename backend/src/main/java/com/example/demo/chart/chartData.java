package com.example.demo.chart;

import com.example.demo.state.Consolidate;
import com.example.demo.utils.idata.baseIData;
import com.example.demo.utils.idata.Ldvil;
import java.util.List;

public interface chartData<T> {

    baseIData<T> getIData();
    void filterSpecific(Consolidate type, Ldvil<T> data);

    List<T> getChartData(List<T> base);
    boolean dontFlip();
    Double getNetMod();
}
