package com.example.demo.importer.nimport;

import java.util.StringTokenizer;

public class NCapOne extends BaseNImport {
	
	public NCapOne(String dir,String csv) {
		super(dir,csv);
		numLines = 1;
		numColumns = 7;
	}
	
	public void go()
	{	
		int i = 0;
		int idx = 0;
		for (String line : lines) {
			idx = 0;
			StringTokenizer st = new StringTokenizer(line,",");
			
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				switch(idx) {
				case 0:
				case 2:
				case 4:
					break;
				case 1:
					dates.add(token);
					break;
				case 3:
					labels.add(token);
					break;
				case 5:
					if (token.equals("X"))
						debit.add(new Double(0));
					else {
						double d = Double.valueOf(token).doubleValue();
						debit.add(d * (-1));
					}
					break;
				case 6:
					if (token.equals("X"))
						credit.add(new Double(0));
					else
						credit.add(Double.valueOf(token));
					break;
				}
				
				idx++;
			}
			
			NData nd = new NData();
			nd.setDate(dates.get(i));
			nd.setLabel(labels.get(i));
			
			if (debit.get(i).doubleValue() < 0) {
				nd.setDebit(debit.get(i));
				out.add(dates.get(i) + "," + labels.get(i) + "," + debit.get(i));
			} else {
				nd.setCredit(credit.get(i));
				out.add(dates.get(i) + "," + labels.get(i) + "," + credit.get(i));
			}
			ndata.add(nd);
			i++;
		}
		
	}
			

}
