package com.example.demo.dto.ui;

import com.example.demo.bean.Chart;
import com.example.demo.bean.ChartMs;
import com.example.demo.chart.dataobj.Categories;
import com.example.demo.chart.dataobj.Dataset;

import java.util.List;

public class DatasourceMsDTO {
    private ChartMs chart;

    private List<Categories> categories;

    private List<Dataset> dataset;

    public void setChart(ChartMs c) { chart = c; }
    public ChartMs getChart() { return chart; }

    public void setCategories(List<Categories> d) { this.categories = d; }
    public List<Categories> getCategories() { return this.categories; }

    public void setDataset(List<Dataset> d) { this.dataset = d; }
    public List<Dataset> getDataset() { return this.dataset; }

}
