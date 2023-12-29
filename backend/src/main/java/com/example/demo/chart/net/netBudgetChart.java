package com.example.demo.chart.net;

import com.example.demo.chart.regBudgetChart;
import com.example.demo.repository.LedgerRepository;

public class netBudgetChart extends regBudgetChart {

   public netBudgetChart(String sessionId, LedgerRepository l)
   {
       super(sessionId, l);
       this.setNetMod(Double.valueOf(4840));
   }
}
