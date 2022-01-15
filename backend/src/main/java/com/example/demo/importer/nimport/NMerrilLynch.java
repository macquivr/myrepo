package com.example.demo.importer.nimport;

import java.util.StringTokenizer;

public class NMerrilLynch extends BaseNImport {
	
	public NMerrilLynch(String dir,String csv) { 
		super(dir,csv); 
		numLines = 5;
		numColumns = 9;
		elines = 4;
	}
	
	
	public void go()
	{	
		int i = 0;
		
		int idx = 0;
		i = 0;
		for (String line : lines) {
			idx = 0;
			line = line.replace("$", "");
			StringTokenizer st = new StringTokenizer(line,",");
			
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				
				switch(idx) {
				case 1:
					dates.add(token);
					break;
				case 2:
					labels.add(token.substring(1));
					break;
				case 7:
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
			String lbl = labels.get(i);
			
			if (debit.get(i).doubleValue() < 0) { 
				nd.setDebit(debit.get(i));
				if (labels.get(i).contains("Check")) {
					int sidx = labels.get(i).indexOf(' ');
					String c = labels.get(i).substring(0,sidx).substring(5);
					lbl = "\"" + c + "  Check\"";
					try {
						nd.setCheck(Integer.valueOf(c).intValue());
					} catch (NumberFormatException ex) {
						nd.setCheck(-1);
					}
					out.add(dates.get(i) + "," + lbl + "," + debit.get(i));
				}
				else {
					lbl = "\"External Withdrawal " + labels.get(i);
					out.add(dates.get(i) + "," + lbl + "," + debit.get(i));
				}
			}
			else {
				nd.setCredit(credit.get(i));
				lbl = "\"Deposit " + labels.get(i);
				out.add(dates.get(i) + "," + lbl + "," + credit.get(i));
			}
			nd.setLabel(lbl);
			ndata.add(nd);
			i++;
			
		}
	}
			

}
