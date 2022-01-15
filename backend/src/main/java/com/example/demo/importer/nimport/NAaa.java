package com.example.demo.importer.nimport;

import java.util.StringTokenizer;

public class NAaa extends BaseNImport {
	
	public NAaa(String dir,String csv) { 
		super(dir,csv);
		numLines = 1;
		numColumns = 5;
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
					dates.add(token);
					break;
				case 2:
					labels.add(token);
					break;
				case 4:
					token = token.replace("\"","");
					double d = Double.valueOf(token).doubleValue();
					if (d < 0) {
						credit.add(new Double(0));
						debit.add(d);
					}
					else {
						debit.add(new Double(0));
						credit.add(d);
					}
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
