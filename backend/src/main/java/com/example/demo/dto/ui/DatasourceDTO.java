package com.example.demo.dto.ui;

import com.example.demo.bean.Chart;
import com.example.demo.bean.Lv;
import java.util.List;

public class DatasourceDTO {
    private Chart chart;
    private List<Lv> data;

    public void setChart(Chart c) { chart = c; }
    public Chart getChart() { return chart; }

    public void setData(List<Lv> d) { data = d; }
    public List<Lv> getData() { return data; }
}
