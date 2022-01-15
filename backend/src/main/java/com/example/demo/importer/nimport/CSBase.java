package com.example.demo.importer.nimport;

import java.util.StringTokenizer;

public class CSBase extends BaseNImport {
	
	public CSBase(String dir,String csv) { 
		super(dir,csv); 
		numLines = 4;
		numColumns = 9;
	}
	
	public void go()
	{	
		int i=0;
		int idx = 0;
		for (String line : lines) {
			idx = 0;
			StringTokenizer st = new StringTokenizer(line,",");
			String desc = null;
			String memo = null;
			Integer check = null;
			
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				
				switch(idx) {
				case 0:
					break;
				case 1:
					dates.add(token);
					break;
				case 2:
					desc = token.substring(0,token.length()-1);
					break;
				case 3:
					memo = token.substring(1);
					break;
				case 4:
					if (token.equals("X"))
						debit.add(new Double(0));
					else
						debit.add(Double.valueOf(token));
					break;
				case 5:
					if (token.equals("X"))
						credit.add(new Double(0));
					else
						credit.add(Double.valueOf(token));
					break;
				case 6:
					break;
				case 7:
					try {
						check = Integer.valueOf(token);
					} catch (Exception ex) {
						// ignore
					}
					break;
				}
				
				idx++;
			}
			NData nd = new NData();
			
			if (check == null) { 
				String spc = "";
				if (!memo.equals("\"")) 
					spc = " ";
				labels.add(desc + spc + memo);
			}
			else {
				nd.setCheck(check);
				labels.add("\"" + check + "  Check\"");
			}
			
			nd.setDate(dates.get(i));
			nd.setLabel(labels.get(i));
			
			if (debit.get(i).doubleValue() < 0) { 
				nd.setDebit(debit.get(i));
				out.add(dates.get(i) + "," + labels.get(i) + "," + debit.get(i));
			} else {
				nd.setCredit(credit.get(i));
				out.add(dates.get(i) + "," + labels.get(i) + ",+" + credit.get(i));
			}
			ndata.add(nd);
			i++;
		}
		
	}
			

}
