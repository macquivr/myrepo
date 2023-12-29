package com.example.demo.chart;

import com.example.demo.state.Consolidate;
import com.example.demo.utils.idata.baseIData;
import com.example.demo.utils.idata.Ldvil;
import java.util.List;

public interface chartData<T> {

    public baseIData getIData();
    public void filterSpecific(Consolidate type, Ldvil data);

    public List<T> getChartData(List<T> base);
    public boolean dontFlip();
    public Double getNetMod();
}
