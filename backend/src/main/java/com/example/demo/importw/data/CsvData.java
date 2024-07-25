package com.example.demo.importw.data;

import com.example.demo.bean.StartStop;
import com.example.demo.importer.data.NData;
import com.example.demo.importw.IBasew;
import com.example.demo.importw.data.csv.PLines;
import com.example.demo.utils.mydate.DUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.importer.BadDataException;
import com.example.demo.importer.CsbEType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CsvData extends PLines {
	private static final Logger log = LoggerFactory.getLogger(CsvData.class);
	private final HashMap<String,String> map;

	private final IData idata;
	private final CsbEType etype;

	private boolean skip = false;

	public CsvData(IBasew obj, IData data, CsbEType et) throws BadDataException {
		super(obj);

		map = new HashMap<>();
		etype = et;

		idata = data;
		initMap();
	}

	public boolean isSkip() { return this.skip; }
	private void initMap()
	{
		map.put("2104","Main");
		map.put("2859","Slush");
		map.put("8528","Annual");
	}
	public String getLabel() { return "CSB"; }

	public void go() throws BadDataException
	{
		ledger();
		//processChecks();
	}

	private String changeLabelToTransfer(String data)
	{
		String estr = etype.toString().charAt(0) + etype.toString().substring(1).toLowerCase();
		String str = data.replace("Internet Transfer ", "");
		int idx = str.indexOf(' ');
		String dw = str.substring(0,idx);
		String rest = str.substring(idx+1);
		idx = rest.indexOf(' ');
		String tof = rest.substring(0,idx);
		rest = rest.substring(idx+1);
		idx = rest.indexOf(' ');

		String tgt = rest.substring(1,5);

		String target = map.get(tgt);
		if (target == null) {
			log.info("TAG: bad target " + data + " TGT: #" + tgt + "#");
			target = "BAD";
		}

		return dw + " " + estr + " " + tof + " " + target;
	}

	private void ledgerLine(String[] tokens)  {
	/*
	Transaction Number,Date,Description,Memo,Amount Debit,Amount Credit,Balance,Check Number,Fees
		0 - fnord
		1 - tdate
		2 - label
		3 - label
		4 - debit
		5 - credit
		6 - balance
		7 - check num
		8 - fee
	 */
		LocalDate ds = DUtil.getStdDate(tokens[1]);
		if (dates.getStart() == null) {
			initStartStop();
			if (this.dates.getStart() == null) {
				System.out.println("EEK START");
				return;
			}
		}
		if ((ds.isBefore(this.dates.getStart())) ||
				(ds.isAfter(this.dates.getStop()))) {
			return;
		}

		NData nd = new NData();

		nd.setDate(tokens[1]);

		String credit = tokens[5];
		String debit = tokens[4];

		if (!credit.equals("0")) {
			nd.setCredit(Double.parseDouble(credit));
		} else {
			nd.setDebit(Double.parseDouble(debit));
		}

		String check = tokens[7];
		int cnum = Integer.parseInt(check);

		if (cnum != 0) {
			nd.setCheck(cnum);
		} else {
			nd.setCheck(-1);
		}

		String label = tokens[2];
		if (!tokens[3].equals("X")) {
			label = label.concat( " " + tokens[3]);
		}

		if (label.contains("Internet Transfer") && !label.contains("3031"))
			label = changeLabelToTransfer(label);

		nd.setLabel(label);

		/*
		String amts = tokens[6];
		double amt = Double.parseDouble(amts);
		if (amt > 0) {
			nd.setCredit(amt);
		} else {
			nd.setDebit(amt);
		}
		*/
		idata.getData().add(nd);
	}

	private void ledger() throws BadDataException
	{
		Iterator<String> iter = lines.iterator();

		System.out.println("Processing...");
		while (iter.hasNext()) {
			String istr = iter.next();

			if (istr.equals("EMPTY")) {
				this.skip = true;
				return;
			}
			String[] tokens = new String[9];
			String[] atokens = istr.split(",");
			if (atokens.length != 1) {
				int i = 0;
				for (i=0;i<9;i++) {
					if (i < atokens.length) {
						tokens[i] = atokens[i];
					} else {
						tokens[i] = new String("0");
					}
				}
				ledgerLine(tokens);
			}
		}
	}
}
